package se.loan.repository.domain.loanapplication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@RepositoryRestResource
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {

    @Query("select count(la) from LoanApplication la where " +
            "la.createdDate>:afterDate and la.registrationNumber=:regNo")
    Long count(@Param("afterDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date afterDate,
               @Param("regNo") String registrationNumber);
}