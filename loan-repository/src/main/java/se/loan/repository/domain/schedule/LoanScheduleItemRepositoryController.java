package se.loan.repository.domain.schedule;

import org.springframework.web.bind.annotation.*;
import se.loan.repository.LoanRepositoryApplication;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(LoanRepositoryApplication.BASE_PATH + "/LoanScheduleItemsCustom")
public class LoanScheduleItemRepositoryController {

    private final LoanScheduleItemRepository repository;

    public LoanScheduleItemRepositoryController(LoanScheduleItemRepository repository) {
        this.repository = repository;
    }

    @PostMapping(path = "{id}")
    public void save(@PathVariable("id") Long loanApplicationId,
                     @Valid @RequestBody List<LoanScheduleItem> items) {
        items.forEach(it -> {
            it.setLoanApplicationId(loanApplicationId);
        });
        repository.saveAll(items);
    }
}
