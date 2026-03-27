package edu.eci.dosw.DOSW_Library.validator;

import edu.eci.dosw.DOSW_Library.core.validator.LoanValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoanValidatorTest {

    @Test
    void testValidateLoanRequest_valid_noException() {
        assertDoesNotThrow(() -> LoanValidator.validateLoanRequest("U1", "B1"));
    }

    @Test
    void testValidateLoanRequest_blankUserId_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> LoanValidator.validateLoanRequest("", "B1"));
    }

    @Test
    void testValidateLoanRequest_blankBookId_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> LoanValidator.validateLoanRequest("U1", ""));
    }

    @Test
    void testValidateLoanRequest_nullUserId_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> LoanValidator.validateLoanRequest(null, "B1"));
    }

    @Test
    void testValidateReturnRequest_valid_noException() {
        assertDoesNotThrow(() -> LoanValidator.validateReturnRequest("loan-123"));
    }

    @Test
    void testValidateReturnRequest_blank_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> LoanValidator.validateReturnRequest(""));
    }

    @Test
    void testValidateReturnRequest_null_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> LoanValidator.validateReturnRequest(null));
    }
}