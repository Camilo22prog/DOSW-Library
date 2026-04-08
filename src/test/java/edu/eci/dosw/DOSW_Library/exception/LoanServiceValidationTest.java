package edu.eci.dosw.DOSW_Library.service;

import edu.eci.dosw.DOSW_Library.core.service.BookService;
import edu.eci.dosw.DOSW_Library.core.service.LoanService;
import edu.eci.dosw.DOSW_Library.core.service.UserService;
import edu.eci.dosw.DOSW_Library.persistence.relational.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceValidationTest {

    @Mock private LoanRepository loanRepository;
    @Mock private BookService bookService;
    @Mock private UserService userService;

    @InjectMocks
    private LoanService loanService;

    @Test
    void testCreateLoan_blankUserId_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> loanService.createLoan("", "B1"));
    }

    @Test
    void testCreateLoan_blankBookId_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> loanService.createLoan("U1", ""));
    }

    @Test
    void testCreateLoan_nullUserId_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> loanService.createLoan(null, "B1"));
    }

    @Test
    void testCreateLoan_nullBookId_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> loanService.createLoan("U1", null));
    }

    @Test
    void testReturnLoan_blankId_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> loanService.returnLoan("", "U1", true));
    }

    @Test
    void testReturnLoan_nullId_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> loanService.returnLoan(null, "U1", true));
    }
}
