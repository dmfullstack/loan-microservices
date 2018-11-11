package se.loan.repository.domain.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface LoanScheduleItemRepository extends JpaRepository<LoanScheduleItem, Long> {

    @Query("select lsi from LoanScheduleItem lsi where lsi.loanApplicationId=:loanApplicationId")
    List<LoanScheduleItem> byLoanApplication(@Param("loanApplicationId") Long loanApplicationId);
}