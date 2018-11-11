package se.loan.service.controller;

import org.springframework.web.bind.annotation.*;
import se.loan.service.LoanServiceApplication;
import se.loan.service.client.BlacklistClient;
import se.loan.service.client.LoanApplicationClient;
import se.loan.service.client.LoanScheduleItemClient;
import se.loan.service.dto.LoanApplication;
import se.loan.service.dto.LoanApplicationStatus;
import se.loan.service.dto.LoanSchedule;
import se.loan.service.dto.LoanScheduleItem;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static se.loan.service.controller.LoanApplicationRestController.PATH;

@RestController
@RequestMapping(PATH)
class LoanApplicationRestController {

    public static final String PATH = LoanServiceApplication.BASE_PATH + "/loan";
    private static final BigDecimal ALLOWED_SHARE_PERCENTAGE = new BigDecimal("0.3");
    private static final BigDecimal INTEREST_RATE = new BigDecimal("0.03"); //TODO probably dynamic, i.e., acquired via some service
    private static final int MAX_REQUESTS_PER_MINUTE = 2;

    private final LoanApplicationClient loanApplicationClient;
    private final BlacklistClient blacklistClient;
    private final SimpleLoanScheduler loanScheduler;
    private final LoanScheduleItemClient loanScheduleItemClient;

    public LoanApplicationRestController(LoanApplicationClient loanApplicationClient,
                                         BlacklistClient blacklistClient,
                                         SimpleLoanScheduler loanScheduler,
                                         LoanScheduleItemClient loanScheduleItemClient) {
        this.loanApplicationClient = loanApplicationClient;
        this.blacklistClient = blacklistClient;
        this.loanScheduler = loanScheduler;
        this.loanScheduleItemClient = loanScheduleItemClient;
    }

    @GetMapping
    public Collection<LoanApplication> list(@RequestParam(name = "page", required = false) Integer page,
                                            @RequestParam(name = "size", required = false) Integer size,
                                            @RequestParam(name = "sort", required = false) String sort) {
        return loanApplicationClient.list(page, size, sort).getContent();
    }

    @PostMapping
    public void create(@Valid @RequestBody LoanApplication application) {
        Date createdDate = new Date();
        handleBlacklist(createdDate, application.getRegistrationNumber());

        application.setStatus(LoanApplicationStatus.NEW);
        application.setCreatedDate(createdDate);
        loanApplicationClient.save(application);
    }

    void handleBlacklist(Date lastCreatedDate, String registrationNumber) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(lastCreatedDate);
        cal.add(Calendar.MINUTE, -1);
        Long count = loanApplicationClient.count(cal.getTime(), registrationNumber);
        if (count >= MAX_REQUESTS_PER_MINUTE) {
            blacklistClient.addIfAbsent(registrationNumber);
        }
    }

    @PostMapping(path = "{id}/reject")
    public void reject(@PathVariable("id") Long id) {
        LoanApplication la = loanApplicationClient.get(id);
        if (!la.getStatus().isValidTransition(LoanApplicationStatus.REJECTED)) {
            throwForbidden(la.getStatus(), LoanApplicationStatus.REJECTED);
        }
        la.setStatus(LoanApplicationStatus.REJECTED);
        loanApplicationClient.save(la);
    }

    @PostMapping(path = "{id}/validate")
    public void validate(@PathVariable("id") Long id) {
        LoanApplication la = loanApplicationClient.get(id);
        LoanApplicationStatus validationResult = isBlackListed(la) || monthlyExpenseViolated(la) ?
                LoanApplicationStatus.VALIDATION_FAIL :
                LoanApplicationStatus.VALIDATION_PASS;

        if (!la.getStatus().isValidTransition(validationResult)) {
            throwForbidden(la.getStatus(), validationResult);
        }
        la.setStatus(validationResult);
        loanApplicationClient.save(la);
    }

    @PostMapping(path = "{id}/confirm")
    public LoanSchedule confirm(@PathVariable("id") Long id) {
        LoanApplication la = loanApplicationClient.get(id);
        if (!la.getStatus().isValidTransition(LoanApplicationStatus.CONFIRMED)) {
            throwForbidden(la.getStatus(), LoanApplicationStatus.CONFIRMED);
        }

        List<LoanScheduleItem> items = loanScheduler.generate(la.getAmount(), INTEREST_RATE, la.getTerm(), new Date());
        loanScheduleItemClient.save(la.getId(), items);

        la.setStatus(LoanApplicationStatus.CONFIRMED);
        loanApplicationClient.save(la);

        return new LoanSchedule(new TreeSet<>(items));
    }

    @GetMapping(path = "{id}/schedule")
    public LoanSchedule schedule(@PathVariable("id") Long id) {
        LoanApplication la = loanApplicationClient.get(id);
        if (!LoanApplicationStatus.CONFIRMED.equals(la.getStatus())) {
            throw new ForbiddenException(String.format("Schedule available only for confirmed loans, actual status: %s", la.getStatus()));
        }

        Collection<LoanScheduleItem> items = loanScheduleItemClient.get(la.getId()).getContent();
        return new LoanSchedule(new TreeSet<>(items));
    }

    boolean monthlyExpenseViolated(LoanApplication la) {
        if (la.getYearlyTurnover() == null) {
            return false;
        }

        BigDecimal maxAllowed = maxAllowed(la.getYearlyTurnover());
        BigDecimal expenses = expenses(la.getAmount(), la.getTerm());
        return maxAllowed.compareTo(expenses) < 0;
    }

    BigDecimal maxAllowed(BigDecimal yearlyTurnover) {
        return yearlyTurnover.divide(new BigDecimal(12), 0, RoundingMode.HALF_UP)
                .multiply(ALLOWED_SHARE_PERCENTAGE);
    }

    BigDecimal expenses(BigDecimal creditAmount, int term) {
        return creditAmount.divide(new BigDecimal(term), 0, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(1).add(INTEREST_RATE));
    }

    private boolean isBlackListed(LoanApplication la) {
        return blacklistClient.exists(la.getRegistrationNumber());
    }

    private void throwForbidden(LoanApplicationStatus from, LoanApplicationStatus to) {
        throw new ForbiddenException(String.format("Invalid transition from %s to %s", from, to));
    }
}