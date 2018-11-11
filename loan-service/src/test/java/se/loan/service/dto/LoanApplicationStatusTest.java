package se.loan.service.dto;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static se.loan.service.dto.LoanApplicationStatus.*;


public class LoanApplicationStatusTest {

    @Test
    public void testIsValidTransition() throws Exception {
        //TODO capture all state transitions here
        assertTrue(NEW.isValidTransition(REJECTED));
        assertFalse(NEW.isValidTransition(CONFIRMED));

        assertTrue(VALIDATION_PASS.isValidTransition(CONFIRMED));
        assertFalse(VALIDATION_FAIL.isValidTransition(CONFIRMED));
    }
}