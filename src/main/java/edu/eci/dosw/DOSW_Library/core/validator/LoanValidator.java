package edu.eci.dosw.DOSW_Library.core.validator;

import edu.eci.dosw.DOSW_Library.core.util.ValidationUtil;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LoanValidator {

    public void validateLoanRequest(String userId, String bookId) {
        ValidationUtil.requireNonBlank(userId, "userId");
        ValidationUtil.requireNonBlank(bookId, "bookId");
    }

    public void validateReturnRequest(String loanId) {
        ValidationUtil.requireNonBlank(loanId, "loanId");
    }
}