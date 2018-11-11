package se.loan.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.*;
import se.loan.service.dto.LoanScheduleItem;

import java.util.List;

import static se.loan.service.LoanServiceApplication.REPOSITORY_BASE_PATH;

@FeignClient("loan-repository")
public interface LoanScheduleItemClient {

    @GetMapping(REPOSITORY_BASE_PATH + "/loanScheduleItems/search/byLoanApplication")
    Resources<LoanScheduleItem> get(@RequestParam("loanApplicationId") Long loanApplicationId);

    @PostMapping(REPOSITORY_BASE_PATH + "/LoanScheduleItemsCustom/{loanApplicationId}")
    void save(@PathVariable("loanApplicationId") Long loanApplicationId, @RequestBody List<LoanScheduleItem> items);

}
