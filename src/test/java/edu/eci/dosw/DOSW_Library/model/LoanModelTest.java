package edu.eci.dosw.DOSW_Library.model;

import edu.eci.dosw.DOSW_Library.core.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LoanModelTest {

    private Book buildBook() {
        return Book.builder().id("B1").title("Clean Code").author("Martin")
                .totalCopies(3).availableCopies(2).build();
    }

    private User buildUser() {
        return User.builder().id("U1").name("Alice").username("alice")
                .password("pass").build();
    }

    @Test
    void testBuilder_allFields() {
        LocalDate loan = LocalDate.now();
        LocalDate ret = LocalDate.now().plusDays(7);

        Loan l = Loan.builder()
                .id("L1").book(buildBook()).user(buildUser())
                .loanDate(loan).returnDate(ret).status(LoanStatus.RETURNED)
                .build();

        assertEquals("L1", l.getId());
        assertEquals("B1", l.getBook().getId());
        assertEquals("U1", l.getUser().getId());
        assertEquals(loan, l.getLoanDate());
        assertEquals(ret, l.getReturnDate());
        assertEquals(LoanStatus.RETURNED, l.getStatus());
    }

    @Test
    void testBuilder_defaultStatusIsActive() {
        Loan loan = Loan.builder().id("L1").book(buildBook()).user(buildUser())
                .loanDate(LocalDate.now()).build();
        assertEquals(LoanStatus.ACTIVE, loan.getStatus());
    }

    @Test
    void testSetters() {
        Loan loan = new Loan();
        loan.setId("L1");
        loan.setBook(buildBook());
        loan.setUser(buildUser());
        loan.setLoanDate(LocalDate.now());
        loan.setReturnDate(LocalDate.now().plusDays(5));
        loan.setStatus(LoanStatus.RETURNED);

        assertEquals("L1", loan.getId());
        assertNotNull(loan.getBook());
        assertNotNull(loan.getUser());
        assertEquals(LoanStatus.RETURNED, loan.getStatus());
    }

    @Test
    void testNoArgsConstructor() {
        Loan loan = new Loan();
        assertNull(loan.getId());
        assertNull(loan.getBook());
        assertNull(loan.getUser());
    }

    @Test
    void testLoanStatus_values() {
        assertEquals(2, LoanStatus.values().length);
        assertEquals(LoanStatus.ACTIVE, LoanStatus.valueOf("ACTIVE"));
        assertEquals(LoanStatus.RETURNED, LoanStatus.valueOf("RETURNED"));
    }
}
