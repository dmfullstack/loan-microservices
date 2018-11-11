package se.loan.repository.domain.loanapplication;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class LoanApplication {
    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private String registrationNumber;
    @NotNull
    private String email;
    @NotNull
    private String phoneNo;
    private BigDecimal yearlyTurnover;
    @NotNull
    private Integer term;
    private String companyName;
    private CompanyType companyType;
    @NotNull
    private LoanApplicationStatus status;
    @NotNull
    private Date createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public BigDecimal getYearlyTurnover() {
        return yearlyTurnover;
    }

    public void setYearlyTurnover(BigDecimal yearlyTurnover) {
        this.yearlyTurnover = yearlyTurnover;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public CompanyType getCompanyType() {
        return companyType;
    }

    public void setCompanyType(CompanyType companyType) {
        this.companyType = companyType;
    }

    public LoanApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(LoanApplicationStatus status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoanApplication that = (LoanApplication) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getAmount() != null ? !getAmount().equals(that.getAmount()) : that.getAmount() != null) return false;
        if (getRegistrationNumber() != null ? !getRegistrationNumber().equals(that.getRegistrationNumber()) : that.getRegistrationNumber() != null)
            return false;
        if (getEmail() != null ? !getEmail().equals(that.getEmail()) : that.getEmail() != null) return false;
        if (getPhoneNo() != null ? !getPhoneNo().equals(that.getPhoneNo()) : that.getPhoneNo() != null) return false;
        if (getYearlyTurnover() != null ? !getYearlyTurnover().equals(that.getYearlyTurnover()) : that.getYearlyTurnover() != null)
            return false;
        if (getTerm() != null ? !getTerm().equals(that.getTerm()) : that.getTerm() != null) return false;
        if (getCompanyName() != null ? !getCompanyName().equals(that.getCompanyName()) : that.getCompanyName() != null)
            return false;
        if (getCompanyType() != that.getCompanyType()) return false;
        if (getStatus() != that.getStatus()) return false;
        return getCreatedDate() != null ? getCreatedDate().equals(that.getCreatedDate()) : that.getCreatedDate() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getAmount() != null ? getAmount().hashCode() : 0);
        result = 31 * result + (getRegistrationNumber() != null ? getRegistrationNumber().hashCode() : 0);
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        result = 31 * result + (getPhoneNo() != null ? getPhoneNo().hashCode() : 0);
        result = 31 * result + (getYearlyTurnover() != null ? getYearlyTurnover().hashCode() : 0);
        result = 31 * result + (getTerm() != null ? getTerm().hashCode() : 0);
        result = 31 * result + (getCompanyName() != null ? getCompanyName().hashCode() : 0);
        result = 31 * result + (getCompanyType() != null ? getCompanyType().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (getCreatedDate() != null ? getCreatedDate().hashCode() : 0);
        return result;
    }
}
