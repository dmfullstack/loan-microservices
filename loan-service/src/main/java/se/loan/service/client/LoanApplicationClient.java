package se.loan.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import se.loan.service.dto.LoanApplication;

import java.util.Date;

import static se.loan.service.LoanServiceApplication.REPOSITORY_BASE_PATH;

@FeignClient("loan-repository")
public interface LoanApplicationClient {

    @GetMapping(REPOSITORY_BASE_PATH + "/loanApplications")
    Resources<LoanApplication> list(@RequestParam("page") Integer page, @RequestParam("size") Integer size, @RequestParam("sort") String sort);

    @GetMapping(REPOSITORY_BASE_PATH + "/loanApplications/search/count")
    Long count(@RequestParam("afterDate") Date afterDate, @RequestParam("regNo") String registrationNumber);

    @PostMapping(REPOSITORY_BASE_PATH + "/loanApplications")
    void save(LoanApplication application);

    @GetMapping(REPOSITORY_BASE_PATH + "/loanApplications/{id}")
    LoanApplication get(@PathVariable("id") Long id);
}

