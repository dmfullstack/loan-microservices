package se.loan.service.controller;

import org.springframework.stereotype.Component;
import se.loan.service.dto.LoanScheduleItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class SimpleLoanScheduler {

    public List<LoanScheduleItem> generate(BigDecimal amount, BigDecimal interestRate, Integer term, Date confirmationDate) {
        BigDecimal principal = amount.divide(new BigDecimal(term), 2, RoundingMode.HALF_UP);
        BigDecimal commission = principal.multiply(interestRate).setScale(2, RoundingMode.HALF_UP);
        Calendar cal = Calendar.getInstance();
        cal.setTime(confirmationDate);

        List<LoanScheduleItem> result = new ArrayList<>(term);
        for (int i = 0; i < term; i++) {
            LoanScheduleItem item = new LoanScheduleItem();
            result.add(item);

            if (i != term - 1) {
                item.setPrincipal(principal);
                item.setCommission(commission);
            } else {
                BigDecimal lastPrincipal = lastAlignedPrincipal(principal, amount, term);
                item.setPrincipal(lastPrincipal);
                item.setCommission(lastPrincipal.multiply(interestRate).setScale(2, RoundingMode.HALF_UP));
            }

            item.setTermDate(cal.getTime());
            item.setNo(i + 1);
            cal.add(Calendar.MONTH, 1);
        }
        return result;
    }

    private BigDecimal lastAlignedPrincipal(BigDecimal monthlyPrincipal, BigDecimal totalAmount, int term) {
        BigDecimal beforeLast = monthlyPrincipal.multiply(new BigDecimal(term - 1));
        return totalAmount.subtract(beforeLast).setScale(2, RoundingMode.HALF_UP);
    }
}
