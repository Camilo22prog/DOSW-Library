package edu.eci.dosw.DOSW_Library.exception;

import edu.eci.dosw.DOSW_Library.core.exception.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionTest {

    @Test
    void testBookNotAvailableException() {
        var ex = new BookNotAvailableException("B1");
        assertTrue(ex.getMessage().contains("B1"));
        assertEquals("B1", ex.getBookId());
        assertInstanceOf(RuntimeException.class, ex);
    }

    @Test
    void testBookNotFoundException() {
        var ex = new BookNotFoundException("B2");
        assertTrue(ex.getMessage().contains("B2"));
        assertEquals("B2", ex.getBookId());
        assertInstanceOf(RuntimeException.class, ex);
    }

    @Test
    void testUserNotFoundException() {
        var ex = new UserNotFoundException("U1");
        assertTrue(ex.getMessage().contains("U1"));
        assertEquals("U1", ex.getUserId());
        assertInstanceOf(RuntimeException.class, ex);
    }

    @Test
    void testLoadLimitExceededException() {
        var ex = new LoadLimitExceededException("U1");
        assertTrue(ex.getMessage().contains("U1"));
        assertEquals("U1", ex.getUserId());
        assertInstanceOf(RuntimeException.class, ex);
    }

    @Test
    void testLoanNotFoundException() {
        var ex = new LoanNotFoundException("L1");
        assertTrue(ex.getMessage().contains("L1"));
        assertEquals("L1", ex.getLoanId());
        assertInstanceOf(RuntimeException.class, ex);
    }

    @Test
    void testUsernameAlreadyExistsException() {
        var ex = new UsernameAlreadyExistsException("alice");
        assertTrue(ex.getMessage().contains("alice"));
        assertEquals("alice", ex.getUsername());
        assertInstanceOf(RuntimeException.class, ex);
    }
}
