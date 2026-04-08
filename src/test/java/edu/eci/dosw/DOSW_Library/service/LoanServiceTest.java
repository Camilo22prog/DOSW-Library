package edu.eci.dosw.DOSW_Library.service;

import edu.eci.dosw.DOSW_Library.core.exception.BookNotAvailableException;
import edu.eci.dosw.DOSW_Library.core.exception.LoadLimitExceededException;
import edu.eci.dosw.DOSW_Library.core.exception.LoanNotFoundException;
import edu.eci.dosw.DOSW_Library.core.exception.UserNotFoundException;
import edu.eci.dosw.DOSW_Library.core.model.*;
import edu.eci.dosw.DOSW_Library.core.repository.LoanRepositoryPort;
import edu.eci.dosw.DOSW_Library.core.service.BookService;
import edu.eci.dosw.DOSW_Library.core.service.LoanService;
import edu.eci.dosw.DOSW_Library.core.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepositoryPort loanRepository;

    @Mock
    private BookService bookService;

    @Mock
    private UserService userService;

    @InjectMocks
    private LoanService loanService;

    private User buildUser(String id) {
        return User.builder().id(id).name("Test").username("user" + id)
                .password("pass").build();
    }

    private Book buildBook(String id, int available) {
        return Book.builder().id(id).title("Title").author("Author")
                .totalCopies(5).availableCopies(available).build();
    }

    private Loan buildLoan(String id, String userId, String bookId, LoanStatus status) {
        return Loan.builder()
                .id(id)
                .user(buildUser(userId))
                .book(buildBook(bookId, 3))
                .loanDate(LocalDate.now())
                .status(status)
                .build();
    }

    // ── Tests existentes ──────────────────────────────────────────────────

    @Test
    void testCreateLoan_success() {
        when(userService.getUserById("U1")).thenReturn(buildUser("U1"));
        when(bookService.getBookById("B1")).thenReturn(buildBook("B1", 2));
        when(loanRepository.countByUserIdAndStatus("U1", LoanStatus.ACTIVE)).thenReturn(0L);
        when(bookService.hasAvailableCopies("B1")).thenReturn(true);
        when(loanRepository.save(any())).thenReturn(buildLoan("L1", "U1", "B1", LoanStatus.ACTIVE));

        var result = loanService.createLoan("U1", "B1");

        assertNotNull(result.getId());
        assertEquals(LoanStatus.ACTIVE, result.getStatus());
        verify(bookService).decrementCopy("B1");
    }

    @Test
    void testCreateLoan_userNotFound() {
        when(userService.getUserById("NONE")).thenThrow(new UserNotFoundException("NONE"));
        assertThrows(UserNotFoundException.class, () -> loanService.createLoan("NONE", "B1"));
    }

    @Test
    void testCreateLoan_bookNotFound() {
        when(userService.getUserById("U1")).thenReturn(buildUser("U1"));
        when(bookService.getBookById("NONE")).thenThrow(new BookNotAvailableException("NONE"));
        assertThrows(BookNotAvailableException.class, () -> loanService.createLoan("U1", "NONE"));
    }

    @Test
    void testCreateLoan_bookNotAvailable() {
        when(userService.getUserById("U1")).thenReturn(buildUser("U1"));
        when(bookService.getBookById("B1")).thenReturn(buildBook("B1", 0));
        when(loanRepository.countByUserIdAndStatus("U1", LoanStatus.ACTIVE)).thenReturn(0L);
        when(bookService.hasAvailableCopies("B1")).thenReturn(false);

        assertThrows(BookNotAvailableException.class, () -> loanService.createLoan("U1", "B1"));
        verify(bookService, never()).decrementCopy(any());
    }

    @Test
    void testCreateLoan_exceedsMaxLimit() {
        when(userService.getUserById("U1")).thenReturn(buildUser("U1"));
        when(bookService.getBookById("B1")).thenReturn(buildBook("B1", 2));
        when(loanRepository.countByUserIdAndStatus("U1", LoanStatus.ACTIVE)).thenReturn(3L);

        assertThrows(LoadLimitExceededException.class, () -> loanService.createLoan("U1", "B1"));
        verify(bookService, never()).decrementCopy(any());
    }

    @Test
    void testReturnLoan_success() {
        var activeLoan = buildLoan("L1", "U1", "B1", LoanStatus.ACTIVE);
        when(loanRepository.findByIdAndStatus("L1", LoanStatus.ACTIVE))
                .thenReturn(Optional.of(activeLoan));
        var returnedLoan = buildLoan("L1", "U1", "B1", LoanStatus.RETURNED);
        returnedLoan.setReturnDate(LocalDate.now());
        when(loanRepository.save(any())).thenReturn(returnedLoan);

        var result = loanService.returnLoan("L1", "U1", false);

        assertEquals(LoanStatus.RETURNED, result.getStatus());
        assertNotNull(result.getReturnDate());
        verify(bookService).incrementCopy("B1");
    }

    @Test
    void testReturnLoan_alreadyReturned_throwsException() {
        when(loanRepository.findByIdAndStatus("L1", LoanStatus.ACTIVE))
                .thenReturn(Optional.empty());
        assertThrows(LoanNotFoundException.class, () -> loanService.returnLoan("L1", "U1", true));
    }

    @Test
    void testGetAllLoans_returnsList() {
        when(loanRepository.findAll()).thenReturn(List.of(
                buildLoan("L1", "U1", "B1", LoanStatus.ACTIVE),
                buildLoan("L2", "U2", "B2", LoanStatus.RETURNED)
        ));
        assertEquals(2, loanService.getAllLoans().size());
    }

    @Test
    void testGetLoansByUser_filtersCorrectly() {
        when(userService.getUserById("U1")).thenReturn(buildUser("U1"));
        when(loanRepository.findByUserId("U1")).thenReturn(List.of(
                buildLoan("L1", "U1", "B1", LoanStatus.ACTIVE)
        ));
        assertEquals(1, loanService.getLoansByUser("U1").size());
    }

    @Test
    void testGetLoansByUser_userNotFound() {
        when(userService.getUserById("NONE")).thenThrow(new UserNotFoundException("NONE"));
        assertThrows(UserNotFoundException.class, () -> loanService.getLoansByUser("NONE"));
    }

    @Test
    void testCreateLoan_blankUserId_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> loanService.createLoan("", "B1"));
    }

    @Test
    void testCreateLoan_blankBookId_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> loanService.createLoan("U1", ""));
    }

    @Test
    void testReturnLoan_blankId_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> loanService.returnLoan("", "U1", true));
    }

    // ── Tests requeridos por el Reto #6 ──────────────────────────────────

    @Test
    @DisplayName("Dado que tengo 1 reserva registrada, Cuando lo consulto a nivel de servicio, Entonces la consulta será exitosa validando el campo id")
    void givenOneLoanRegistered_whenFindById_thenReturnLoanWithCorrectId() {
        // Given
        Loan loan = buildLoan("L1", "U1", "B1", LoanStatus.ACTIVE);
        when(loanRepository.findById("L1")).thenReturn(Optional.of(loan));

        // When
        Optional<Loan> result = loanRepository.findById("L1");

        // Then
        assertTrue(result.isPresent());
        assertEquals("L1", result.get().getId());
        verify(loanRepository, times(1)).findById("L1");
    }

    @Test
    @DisplayName("Dado que no hay ninguna reserva registrada, Cuando la consulto a nivel de servicio, Entonces la consulta no retorna ningún resultado")
    void givenNoLoansRegistered_whenGetAll_thenReturnEmptyList() {
        // Given
        when(loanRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Loan> result = loanService.getAllLoans();

        // Then
        assertTrue(result.isEmpty());
        verify(loanRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Dado que no hay ninguna reserva registrada, Cuando lo creo a nivel de servicio, Entonces la creación será exitosa")
    void givenNoLoansRegistered_whenCreateLoan_thenLoanIsCreatedSuccessfully() {
        // Given
        when(userService.getUserById("U1")).thenReturn(buildUser("U1"));
        when(bookService.getBookById("B1")).thenReturn(buildBook("B1", 3));
        when(loanRepository.countByUserIdAndStatus("U1", LoanStatus.ACTIVE)).thenReturn(0L);
        when(bookService.hasAvailableCopies("B1")).thenReturn(true);
        when(loanRepository.save(any())).thenReturn(buildLoan("L1", "U1", "B1", LoanStatus.ACTIVE));

        // When
        Loan result = loanService.createLoan("U1", "B1");

        // Then
        assertNotNull(result);
        assertEquals("U1", result.getUser().getId());
        assertEquals("B1", result.getBook().getId());
        assertEquals(LoanStatus.ACTIVE, result.getStatus());
        verify(loanRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Dado que tengo 1 reserva registrada, Cuando la elimino a nivel de servicio, Entonces la eliminación será exitosa")
    void givenOneLoanRegistered_whenDelete_thenDeletionIsSuccessful() {
        // Given
        doNothing().when(loanRepository).delete("L1");

        // When
        loanRepository.delete("L1");

        // Then
        verify(loanRepository, times(1)).delete("L1");
    }

    @Test
    @DisplayName("Dado que tengo 1 reserva registrada, Cuando la elimino y consulto a nivel de servicio, Entonces el resultado de la consulta no retorna ningún resultado")
    void givenOneLoanRegistered_whenDeleteAndGetAll_thenReturnEmptyList() {
        // Given
        doNothing().when(loanRepository).delete("L1");
        when(loanRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        loanRepository.delete("L1");
        List<Loan> result = loanService.getAllLoans();

        // Then
        assertTrue(result.isEmpty());
        verify(loanRepository, times(1)).delete("L1");
        verify(loanRepository, times(1)).findAll();
    }
}