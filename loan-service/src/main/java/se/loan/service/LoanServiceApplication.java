package se.loan.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class LoanServiceApplication {

    public static final String BASE_PATH = "/api/v1";
    public static final String REPOSITORY_BASE_PATH = "/api/v1";

    public static void main(String[] args) {
        SpringApplication.run(LoanServiceApplication.class, args);
    }
}



