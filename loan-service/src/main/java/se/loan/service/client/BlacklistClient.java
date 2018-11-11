package se.loan.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static se.loan.service.LoanServiceApplication.REPOSITORY_BASE_PATH;

@FeignClient("loan-repository")
public interface BlacklistClient {

    @PutMapping(path = REPOSITORY_BASE_PATH + "/blacklists/{regNo}")
    void addIfAbsent(@PathVariable("regNo") String registrationNumber);

    @GetMapping(path = REPOSITORY_BASE_PATH + "/blacklists/search/exists")
    boolean exists(@RequestParam("regNo") String registrationNumber);
}