package se.loan.service.dto;

import java.math.BigDecimal;
import java.util.TreeSet;
import java.util.function.Function;

public class LoanSchedule {

    private TreeSet<LoanScheduleItem> items;
    private BigDecimal totalPrincipal;
    private BigDecimal totalCommission;

    public LoanSchedule() {
    }

    public LoanSchedule(TreeSet<LoanScheduleItem> items) {
        this.items = items;
        this.totalPrincipal = sumItems(LoanScheduleItem::getPrincipal);
        this.totalCommission = sumItems(LoanScheduleItem::getCommission);
    }

    BigDecimal sumItems(Function<LoanScheduleItem, BigDecimal> mapper) {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return items.stream().map(mapper).reduce((x, y) -> x.add(y)).get();
    }

    public TreeSet<LoanScheduleItem> getItems() {
        return items;
    }

    public void setItems(TreeSet<LoanScheduleItem> items) {
        this.items = items;
    }

    public BigDecimal getTotalPrincipal() {
        return totalPrincipal;
    }

    public void setTotalPrincipal(BigDecimal totalPrincipal) {
        this.totalPrincipal = totalPrincipal;
    }

    public BigDecimal getTotalCommission() {
        return totalCommission;
    }

    public void setTotalCommission(BigDecimal totalCommission) {
        this.totalCommission = totalCommission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoanSchedule that = (LoanSchedule) o;

        if (items != null ? !items.equals(that.items) : that.items != null) return false;
        if (totalCommission != null ? !totalCommission.equals(that.totalCommission) : that.totalCommission != null)
            return false;
        if (totalPrincipal != null ? !totalPrincipal.equals(that.totalPrincipal) : that.totalPrincipal != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = items != null ? items.hashCode() : 0;
        result = 31 * result + (totalPrincipal != null ? totalPrincipal.hashCode() : 0);
        result = 31 * result + (totalCommission != null ? totalCommission.hashCode() : 0);
        return result;
    }
}
