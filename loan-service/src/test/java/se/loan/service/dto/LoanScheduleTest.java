package se.loan.service.dto;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;

public class LoanScheduleTest {

    @Test
    public void sumItems() {
        LoanSchedule ls = new LoanSchedule();
        assertEquals(BigDecimal.ZERO, ls.sumItems(LoanScheduleItem::getPrincipal));

        ls.setItems(new TreeSet<>(Arrays.asList(item("1.1", 1), item("2.2", 2))));
        assertEquals(new BigDecimal("3.3"), ls.sumItems(LoanScheduleItem::getPrincipal));
    }

    private LoanScheduleItem item(String principal, int no) {
        LoanScheduleItem item = new LoanScheduleItem();
        item.setPrincipal(new BigDecimal(principal));
        item.setNo(no);
        return item;
    }

}