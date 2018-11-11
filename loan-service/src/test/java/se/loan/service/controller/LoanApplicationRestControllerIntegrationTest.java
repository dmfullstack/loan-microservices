package se.loan.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import se.loan.service.client.BlacklistClient;
import se.loan.service.client.LoanApplicationClient;
import se.loan.service.dto.LoanApplication;
import se.loan.service.dto.LoanApplicationStatus;
import se.loan.service.dto.LoanSchedule;
import se.loan.service.dto.LoanScheduleItem;

import java.math.BigDecimal;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoanApplicationRestControllerIntegrationTest extends TestCase {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private LoanApplicationClient loanApplicationClient;

    @Autowired
    private BlacklistClient blacklistClient;

    @Autowired
    private LoanApplicationRestController controller;

    private ObjectMapper om = new ObjectMapper();

    @Test
    public void newApplicationStoredAndListed_pass() throws Exception {
        Date testStarted = new Date();
        String registrationNo = "newApplicationStoredAndListed_pass";
        LoanApplication newApp = loanApplication(registrationNo);
        newApp.setTerm(12);

        mvc.perform(post(LoanApplicationRestController.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(newApp)))
                .andExpect(status().is(200));

        Optional<LoanApplication> storedApp = findApplication(getStoredApplicationsViaRest(), testStarted, registrationNo);
        assertTrue(storedApp.isPresent());
        assertEquals(LoanApplicationStatus.NEW, storedApp.get().getStatus());
    }

    private List<LoanApplication> getStoredApplicationsViaRest() throws Exception {
        List<LoanApplication> result = new ArrayList<>();
        mvc.perform(get(LoanApplicationRestController.PATH + "?size=" + Integer.MAX_VALUE))
                .andExpect(status().is(200))
                .andDo(r -> {
                    result.addAll(Arrays.asList(om.readValue(r.getResponse().getContentAsString(), LoanApplication[].class)));
                });

        return result;
    }

    private Optional<LoanApplication> findApplication(Collection<LoanApplication> apps, Date fromDate, String registrationNo) {
        return apps.stream().filter(a ->
                (fromDate.before(a.getCreatedDate()) || fromDate.equals(a.getCreatedDate()))
                        && registrationNo.equals(a.getRegistrationNumber())).findAny();
    }

    @Test
    public void newApplication_fails_fieldValidation() throws Exception {
        LoanApplication la = new LoanApplication();
        la.setEmail("email[at]box.com");
        la.setTerm(50);
        la.setCreatedDate(new Date());
        la.setStatus(LoanApplicationStatus.VALIDATION_PASS);

        mvc.perform(post(LoanApplicationRestController.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(la)))
                .andExpect(status().is(400))
                .andDo(result -> {
                    assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException);
                    MethodArgumentNotValidException e = (MethodArgumentNotValidException) result.getResolvedException();
                    assertEquals(7, e.getBindingResult().getErrorCount());

                    List<ObjectError> errors = e.getBindingResult().getAllErrors();
                    hasFieldError(errors, "amount", "must not be null");
                    hasFieldError(errors, "phoneNo", "must not be null");
                    hasFieldError(errors, "registrationNumber", "must not be null");
                    hasFieldError(errors, "term", "must be between 1 and 12");
                    hasFieldError(errors, "email", "must be a well-formed email address");
                    hasFieldError(errors, "createdDate", "must be null");
                    hasFieldError(errors, "status", "must be null");
                });
    }

    @Test
    public void rejectTest() throws Exception {
        Date testStared = new Date();
        String regNo = "rejectTest";
        LoanApplication newApp = loanApplication(regNo);
        newApp.setCreatedDate(testStared);
        controller.create(newApp);
        LoanApplication storedApp = findApplication(getStoredApplications(), testStared, regNo).get();

        mvc.perform(post(LoanApplicationRestController.PATH + "/" + storedApp.getId() + "/reject"))
                .andExpect(status().is(200));

        LoanApplication rejected = loanApplicationClient.get(storedApp.getId());
        assertEquals(LoanApplicationStatus.REJECTED, rejected.getStatus());
    }

    @Test
    public void validate_pass() throws Exception {
        Date testStared = new Date();
        String regNo = "validate_pass";
        LoanApplication newApp = loanApplication(regNo);
        controller.create(newApp);
        LoanApplication storedApp = findApplication(getStoredApplications(), testStared, regNo).get();

        mvc.perform(post(LoanApplicationRestController.PATH + "/" + storedApp.getId() + "/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(newApp)))
                .andExpect(status().is(200));

        LoanApplication validated = loanApplicationClient.get(storedApp.getId());
        assertEquals(LoanApplicationStatus.VALIDATION_PASS, validated.getStatus());
    }

    @Test
    public void validate_failBlacklist() throws Exception {
        Date testStared = new Date();
        String regNo = "validate_failBlacklist";
        LoanApplication newApp = loanApplication(regNo);
        saveViaController(newApp);
        saveViaController(newApp);
        saveViaController(newApp);
        LoanApplication storedApp = findApplication(getStoredApplications(), testStared, regNo).get();

        mvc.perform(post(LoanApplicationRestController.PATH + "/" + storedApp.getId() + "/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(newApp)))
                .andExpect(status().is(200));

        LoanApplication validated = loanApplicationClient.get(storedApp.getId());
        assertEquals(LoanApplicationStatus.VALIDATION_FAIL, validated.getStatus());
        assertTrue(blacklistClient.exists(regNo));
    }

    @Test
    public void validate_failMonthlyExpenses() throws Exception {
        Date testStared = new Date();
        String regNo = "validate_failMonthlyExpenses";
        LoanApplication newApp = loanApplication(regNo);
        newApp.setAmount(BigDecimal.TEN);
        newApp.setYearlyTurnover(BigDecimal.ONE);
        controller.create(newApp);
        LoanApplication storedApp = findApplication(getStoredApplications(), testStared, regNo).get();

        mvc.perform(post(LoanApplicationRestController.PATH + "/" + storedApp.getId() + "/validate"))
                .andExpect(status().is(200));

        LoanApplication validated = loanApplicationClient.get(storedApp.getId());
        assertEquals(LoanApplicationStatus.VALIDATION_FAIL, validated.getStatus());
        assertFalse(blacklistClient.exists(regNo));
    }

    @Test
    public void confirm_fail_validationFailed() throws Exception {
        Date testStared = new Date();
        String regNo = "confirm_fail_validationFailed";
        LoanApplication newApp = loanApplication(regNo);
        newApp.setAmount(BigDecimal.TEN);
        newApp.setYearlyTurnover(BigDecimal.ONE);
        controller.create(newApp);
        LoanApplication storedApp = findApplication(getStoredApplications(), testStared, regNo).get();
        controller.validate(storedApp.getId());

        mvc.perform(post(LoanApplicationRestController.PATH + "/" + storedApp.getId() + "/confirm"))
                .andExpect(status().is(403));

        LoanApplication afterConfirmApp = loanApplicationClient.get(storedApp.getId());
        assertEquals(LoanApplicationStatus.VALIDATION_FAIL, afterConfirmApp.getStatus());
    }

    @Test
    public void confirmAndSchedule_pass() throws Exception {
        Date testStared = new Date();
        String regNo = "confirm_pass";
        LoanApplication newApp = loanApplication(regNo);
        controller.create(newApp);
        LoanApplication storedApp = findApplication(getStoredApplications(), testStared, regNo).get();
        controller.validate(storedApp.getId());

        mvc.perform(post(LoanApplicationRestController.PATH + "/" + storedApp.getId() + "/confirm"))
                .andExpect(status().is(200))
                .andDo(result -> {
                    LoanSchedule ls = om.readValue(result.getResponse().getContentAsString(), LoanSchedule.class);
                    assertEquals(6, ls.getItems().size());
                    assertEquals(new BigDecimal("0.36"), ls.getTotalCommission());
                    assertEquals(new BigDecimal("12.12"), ls.getTotalPrincipal());
                    LoanScheduleItem item = ls.getItems().iterator().next();
                    assertEquals(new BigDecimal("0.06"), item.getCommission());
                    assertEquals(new BigDecimal("2.02"), item.getPrincipal());
                    assertStoredScheduleViaRest(ls, storedApp.getId());
                });
        LoanApplication afterConfirmApp = loanApplicationClient.get(storedApp.getId());
        assertEquals(LoanApplicationStatus.CONFIRMED, afterConfirmApp.getStatus());
    }

    private void assertStoredScheduleViaRest(LoanSchedule expected, Long applicationId) throws Exception {
        mvc.perform(get(LoanApplicationRestController.PATH + "/" + applicationId + "/schedule"))
                .andExpect(status().is(200))
                .andDo(result -> {
                    LoanSchedule actual = om.readValue(result.getResponse().getContentAsString(), LoanSchedule.class);
                    assertEquals(expected, actual);
                });
    }

    private void saveViaController(LoanApplication la) throws Exception {
        mvc.perform(post(LoanApplicationRestController.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(la)))
                .andExpect(status().is(200));
    }

    private void hasFieldError(List<ObjectError> errors, String fieldName, String message) {
        assertTrue(String.format("Expected field error. Field name: [%s], message [%s]", fieldName, message),
                errors.stream().anyMatch(e -> isError(e, fieldName, message)));
    }

    private boolean isError(ObjectError e, String fieldName, String message) {
        if (!(e instanceof FieldError)) {
            return false;
        }
        FieldError fe = (FieldError) e;
        return fieldName.equals(fe.getField()) && message.equals(fe.getDefaultMessage());
    }

    private LoanApplication loanApplication(String registrationNo) {
        LoanApplication loanApp = new LoanApplication();
        loanApp.setAmount(new BigDecimal("12.12"));
        loanApp.setEmail("email@box.com");
        loanApp.setRegistrationNumber(registrationNo);
        loanApp.setPhoneNo("+371 123 123");
        return loanApp;
    }

    private Collection<LoanApplication> getStoredApplications() {
        return loanApplicationClient.list(0, Integer.MAX_VALUE, "").getContent();
    }
}