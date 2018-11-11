package se.loan.service.dto;

import java.math.BigDecimal;
import java.util.Date;

public class LoanScheduleItem implements Comparable<LoanScheduleItem> {
    private Integer no;
    private Date termDate;
    private BigDecimal principal;
    private BigDecimal commission;

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

        LoanScheduleItem item = (LoanScheduleItem) o;

        if (commission != null ? !commission.equals(item.commission) : item.commission != null) return false;
        if (no != null ? !no.equals(item.no) : item.no != null) return false;
        if (principal != null ? !principal.equals(item.principal) : item.principal != null) return false;
        if (termDate != null ? !termDate.equals(item.termDate) : item.termDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = no != null ? no.hashCode() : 0;
        result = 31 * result + (termDate != null ? termDate.hashCode() : 0);
        result = 31 * result + (principal != null ? principal.hashCode() : 0);
        result = 31 * result + (commission != null ? commission.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(LoanScheduleItem o) {
        int thisNo = no == null ? 0 : no;
        int thatNo = o.no == null ? 0 : o.no;
        return thisNo - thatNo;
    }

    @Override
    public String toString() {
        return "LoanScheduleItem{" +
                "no=" + no +
                ", termDate=" + termDate +
                ", principal=" + principal +
                ", commission=" + commission +
                '}';
    }
}
