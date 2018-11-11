package se.loan.service.controller;

import org.junit.Test;
import se.loan.service.dto.LoanScheduleItem;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SimpleLoanSchedulerTest {

    private SimpleLoanScheduler scheduler = new SimpleLoanScheduler();

    @Test
    public void testGenerate() throws Exception {
        Date confirmationDate = new Date();
        List<LoanScheduleItem> expected = Arrays.asList(
                item(1, "1.00", "2.00", monthFromToday(confirmationDate, 0)),
                item(2, "1.00", "2.00", monthFromToday(confirmationDate, 1))
        );

        List<LoanScheduleItem> actual = scheduler.generate(new BigDecimal(4), new BigDecimal("0.5"), 2, confirmationDate);

        assertEquals(expected, actual);
    }

    @Test
    public void testGenerate_lastAligned() throws Exception {
        Date confirmationDate = new Date();
        List<LoanScheduleItem> expected = Arrays.asList(
                item(1, "0.67", "1.33", monthFromToday(confirmationDate, 0)),
                item(2, "0.67", "1.33", monthFromToday(confirmationDate, 1)),
                item(3, "0.67", "1.34", monthFromToday(confirmationDate, 2))
        );

        List<LoanScheduleItem> actual = scheduler.generate(new BigDecimal(4), new BigDecimal("0.5"), 3, confirmationDate);

        assertEquals(expected, actual);
    }

    Date monthFromToday(Date confirmationDate, int monthCount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(confirmationDate);
        cal.add(Calendar.MONTH, monthCount);
        return cal.getTime();
    }

    private LoanScheduleItem item(int no, String commission, String principal, Date termDate) {
        LoanScheduleItem item = new LoanScheduleItem();
        item.setNo(no);
        item.setCommission(new BigDecimal(commission));
        item.setTermDate(termDate);
        item.setPrincipal(new BigDecimal(principal));
        return item;
    }
}