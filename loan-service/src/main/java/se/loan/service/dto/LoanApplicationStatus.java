package se.loan.service.dto;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum LoanApplicationStatus {
    NEW,
    REJECTED,
    VALIDATION_FAIL,
    VALIDATION_PASS,
    CONFIRMED;

    private Set<LoanApplicationStatus> nextStatuses;

    static {
        NEW.nextStatuses = hashSet(REJECTED, VALIDATION_FAIL, VALIDATION_PASS);
        REJECTED.nextStatuses = Collections.emptySet();
        VALIDATION_FAIL.nextStatuses = hashSet(REJECTED);
        VALIDATION_PASS.nextStatuses = hashSet(CONFIRMED, REJECTED);
        CONFIRMED.nextStatuses = Collections.emptySet();
    }


    public boolean isValidTransition(LoanApplicationStatus nextStatus) {
        return nextStatuses.contains(nextStatus);
    }

    private static Set<LoanApplicationStatus> hashSet(LoanApplicationStatus... statuses) {
        return new HashSet<>(Arrays.asList(statuses));
    }
}
