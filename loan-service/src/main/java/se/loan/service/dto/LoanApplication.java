package se.loan.service.dto;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.util.Date;

public class LoanApplication {

    private Long id;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private String registrationNumber;
    @NotNull
    @Email
    private String email;
    @NotNull
    //TODO additional custom validator for accepted phoneNo formats or if simple enough use @Pattern(regexp=...)
    private String phoneNo;

    private BigDecimal yearlyTurnover;
    @Range(min = 1, max = 12)
    private Integer term = 6;
    private String companyName;
    private CompanyType companyType;
    @Null
    private LoanApplicationStatus status;
    @Null
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

        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        if (companyName != null ? !companyName.equals(that.companyName) : that.companyName != null) return false;
        if (companyType != that.companyType) return false;
        if (createdDate != null ? !createdDate.equals(that.createdDate) : that.createdDate != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (status != that.status) return false;
        if (phoneNo != null ? !phoneNo.equals(that.phoneNo) : that.phoneNo != null) return false;
        if (registrationNumber != null ? !registrationNumber.equals(that.registrationNumber) : that.registrationNumber != null)
            return false;
        if (term != null ? !term.equals(that.term) : that.term != null) return false;
        if (yearlyTurnover != null ? !yearlyTurnover.equals(that.yearlyTurnover) : that.yearlyTurnover != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (registrationNumber != null ? registrationNumber.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (phoneNo != null ? phoneNo.hashCode() : 0);
        result = 31 * result + (yearlyTurnover != null ? yearlyTurnover.hashCode() : 0);
        result = 31 * result + (term != null ? term.hashCode() : 0);
        result = 31 * result + (companyName != null ? companyName.hashCode() : 0);
        result = 31 * result + (companyType != null ? companyType.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LoanApplication{" +
                "id=" + id +
                ", amount=" + amount +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", email='" + email + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", yearlyTurnover=" + yearlyTurnover +
                ", term=" + term +
                ", companyName='" + companyName + '\'' +
                ", companyType=" + companyType +
                ", status=" + status +
                ", createdDate=" + createdDate +
                '}';
    }
}
