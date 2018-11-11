package se.loan.service.controller;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import se.loan.service.client.BlacklistClient;
import se.loan.service.client.LoanApplicationClient;
import se.loan.service.client.LoanScheduleItemClient;
import se.loan.service.dto.LoanApplication;
import se.loan.service.dto.LoanApplicationStatus;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoanApplicationRestControllerTest {

    @InjectMocks
    private LoanApplicationRestController controller;

    @Mock
    private LoanApplicationClient loanApplicationClient;

    @Mock
    private BlacklistClient blacklistClient;

    @Mock
    private SimpleLoanScheduler loanScheduler;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private LoanScheduleItemClient loanScheduleItemClient;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void maxAllowed() {
        assertEqualsBD(new BigDecimal("0.3"), controller.maxAllowed(BigDecimal.TEN));
        assertEqualsBD(new BigDecimal("2.4"), controller.maxAllowed(new BigDecimal(100)));

        assertEqualsBD(BigDecimal.ZERO, controller.maxAllowed(BigDecimal.ZERO));
        assertEqualsBD(BigDecimal.ZERO, controller.maxAllowed(BigDecimal.ONE));
    }

    @Test
    public void expenses() {
        assertEqualsBD(new BigDecimal("1.03"), controller.expenses(BigDecimal.ONE, 1));
        assertEqualsBD(new BigDecimal(103), controller.expenses(new BigDecimal(100), 1));
        assertEqualsBD(new BigDecimal("8.24"), controller.expenses(new BigDecimal(100), 12));

        assertEqualsBD(BigDecimal.ZERO, controller.expenses(BigDecimal.ZERO, 1));
    }

    @Test
    public void monthlyExpenseViolated() throws Exception {
        assertFalse("pass, no yearly term supplied", controller.monthlyExpenseViolated(new LoanApplication()));
        assertFalse("pass", controller.monthlyExpenseViolated(loanApplication(1, 1, 100)));
        assertTrue("violate", controller.monthlyExpenseViolated(loanApplication(12, 100, 100)));
    }

    @Test
    public void reject_invalidStateTransition() {
        thrown.expect(ForbiddenException.class);
        repositoryReturnsLA(1L, LoanApplicationStatus.CONFIRMED, "regNo");
        controller.reject(1L);
    }

    @Test
    public void reject_validStateTransition() {
        repositoryReturnsLA(1L, LoanApplicationStatus.VALIDATION_PASS, "regNo");
        controller.reject(1L);
        verify(loanApplicationClient).save(ArgumentMatchers.any());
    }

    @Test
    public void validate_validStateTransition() {
        repositoryReturnsLA(1L, LoanApplicationStatus.NEW, "regNo");
        when(blacklistClient.exists("regNo")).thenReturn(false);

        controller.validate(1L);

        verify(loanApplicationClient).save(ArgumentMatchers.any());
    }

    @Test
    public void validate_invalidStateTransition() {
        repositoryReturnsLA(1L, LoanApplicationStatus.VALIDATION_PASS, "regNo");
        when(blacklistClient.exists("regNo")).thenReturn(false);

        thrown.expect(ForbiddenException.class);
        controller.validate(1L);
    }

    @Test
    public void confirm_validStateTransition() {
        repositoryReturnsLA(1L, LoanApplicationStatus.VALIDATION_PASS, "regNo");
        controller.confirm(1L);
        verify(loanApplicationClient).save(ArgumentMatchers.any());
    }

    @Test
    public void confirm_invalidStateTransition() {
        repositoryReturnsLA(1L, LoanApplicationStatus.NEW, "regNo");
        thrown.expect(ForbiddenException.class);
        controller.confirm(1L);
    }

    @Test
    public void schedule_validState() {
        repositoryReturnsLA(1L, LoanApplicationStatus.CONFIRMED, "regNo");
        controller.schedule(1L);
    }

    @Test
    public void confirm_invalidState() {
        repositoryReturnsLA(1L, LoanApplicationStatus.NEW, "regNo");
        thrown.expect(ForbiddenException.class);
        controller.schedule(1L);
    }

    private void repositoryReturnsLA(Long id, LoanApplicationStatus status, String regNo) {
        when(loanApplicationClient.get(id)).thenReturn(loanApplication(status, regNo));
    }

    private LoanApplication loanApplication(LoanApplicationStatus status, String regNo) {
        LoanApplication la = new LoanApplication();
        la.setStatus(status);
        la.setRegistrationNumber(regNo);
        return la;
    }

    private LoanApplication loanApplication(int term, int amount, int yearlyTurnover) {
        LoanApplication la = new LoanApplication();
        la.setYearlyTurnover(new BigDecimal(yearlyTurnover));
        la.setTerm(term);
        la.setAmount(new BigDecimal(amount));
        return la;
    }

    private void assertEqualsBD(BigDecimal expected, BigDecimal actual) {
        if (expected.compareTo(actual) != 0) {
            fail(String.format("Expected [%s], got [%s]", expected, actual));
        }
    }
}