package se.loan.repository.domain.schedule;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"loanApplicationId", "no"})
)
public class LoanScheduleItem {
    @Id
    @GeneratedValue
    private Long id;
    //TODO maybe @OneToMany from LoanApplication entity, but need to explore how to incorporate this into spring-repo-rest framework
    //otherwise need explicitly create FK constraint at initial schema creation
    @NotNull
    private Long loanApplicationId;
    @NotNull
    private Integer no;
    @NotNull
    private Date termDate;
    @NotNull
    private BigDecimal principal;
    @NotNull
    private BigDecimal commission;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(Long loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public Date getTermDate() {
        return termDate;
    }

    public void setTermDate(Date termDate) {
        this.termDate = termDate;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoanScheduleItem that = (LoanScheduleItem) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getLoanApplicationId() != null ? !getLoanApplicationId().equals(that.getLoanApplicationId()) : that.getLoanApplicationId() != null)
            return false;
        if (getNo() != null ? !getNo().equals(that.getNo()) : that.getNo() != null) return false;
        if (getTermDate() != null ? !getTermDate().equals(that.getTermDate()) : that.getTermDate() != null)
            return false;
        if (getPrincipal() != null ? !getPrincipal().equals(that.getPrincipal()) : that.getPrincipal() != null)
            return false;
        return getCommission() != null ? getCommission().equals(that.getCommission()) : that.getCommission() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getLoanApplicationId() != null ? getLoanApplicationId().hashCode() : 0);
        result = 31 * result + (getNo() != null ? getNo().hashCode() : 0);
        result = 31 * result + (getTermDate() != null ? getTermDate().hashCode() : 0);
        result = 31 * result + (getPrincipal() != null ? getPrincipal().hashCode() : 0);
        result = 31 * result + (getCommission() != null ? getCommission().hashCode() : 0);
        return result;
    }
}
