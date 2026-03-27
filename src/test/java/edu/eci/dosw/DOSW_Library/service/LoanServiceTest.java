package edu.eci.dosw.DOSW_Library.service;

import edu.eci.dosw.DOSW_Library.core.exception.BookNotAvailableException;
import edu.eci.dosw.DOSW_Library.core.exception.LoadLimitExceededException;
import edu.eci.dosw.DOSW_Library.core.exception.LoanNotFoundException;
import edu.eci.dosw.DOSW_Library.core.exception.UserNotFoundException;
import edu.eci.dosw.DOSW_Library.core.model.*;
import edu.eci.dosw.DOSW_Library.core.service.BookService;
import edu.eci.dosw.DOSW_Library.core.service.LoanService;
import edu.eci.dosw.DOSW_Library.core.service.UserService;
import edu.eci.dosw.DOSW_Library.persistence.entity.*;
import edu.eci.dosw.DOSW_Library.persistence.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

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

    private LoanEntity buildLoanEntity(String id, String userId, String bookId, LoanStatusEntity status) {
        var userEntity = UserEntity.builder().id(userId).name("Test").username("u")
                .password("p").role(UserRole.USER).build();
        var bookEntity = BookEntity.builder().id(bookId).title("Title").author("Author")
                .totalCopies(5).availableCopies(3).build();
        return LoanEntity.builder().id(id).user(userEntity).book(bookEntity)
                .loanDate(LocalDate.now()).status(status).build();
    }

    @Test
    void testCreateLoan_success() {
        when(userService.getUserById("U1")).thenReturn(buildUser("U1"));
        when(bookService.getBookById("B1")).thenReturn(buildBook("B1", 2));
        when(loanRepository.countByUserIdAndStatus("U1", LoanStatusEntity.ACTIVE)).thenReturn(0L);
        when(bookService.hasAvailableCopies("B1")).thenReturn(true);
        when(loanRepository.save(any())).thenReturn(buildLoanEntity("L1", "U1", "B1", LoanStatusEntity.ACTIVE));

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
        when(loanRepository.countByUserIdAndStatus("U1", LoanStatusEntity.ACTIVE)).thenReturn(0L);
        when(bookService.hasAvailableCopies("B1")).thenReturn(false);

        assertThrows(BookNotAvailableException.class, () -> loanService.createLoan("U1", "B1"));
        verify(bookService, never()).decrementCopy(any());
    }

    @Test
    void testCreateLoan_exceedsMaxLimit() {
        when(userService.getUserById("U1")).thenReturn(buildUser("U1"));
        when(bookService.getBookById("B1")).thenReturn(buildBook("B1", 2));
        when(loanRepository.countByUserIdAndStatus("U1", LoanStatusEntity.ACTIVE)).thenReturn(3L);

        assertThrows(LoadLimitExceededException.class, () -> loanService.createLoan("U1", "B1"));
        verify(bookService, never()).decrementCopy(any());
    }

    @Test
    void testReturnLoan_success() {
        var entity = buildLoanEntity("L1", "U1", "B1", LoanStatusEntity.ACTIVE);
        when(loanRepository.findByIdAndStatus("L1", LoanStatusEntity.ACTIVE))
                .thenReturn(Optional.of(entity));
        entity.setStatus(LoanStatusEntity.RETURNED);
        entity.setReturnDate(LocalDate.now());
        when(loanRepository.save(any())).thenReturn(entity);

        var result = loanService.returnLoan("L1", "U1", false);

        assertEquals(LoanStatus.RETURNED, result.getStatus());
        assertNotNull(result.getReturnDate());
        verify(bookService).incrementCopy("B1");
    }

    @Test
    void testReturnLoan_alreadyReturned_throwsException() {
        when(loanRepository.findByIdAndStatus("L1", LoanStatusEntity.ACTIVE))
                .thenReturn(Optional.empty());
        assertThrows(LoanNotFoundException.class, () -> loanService.returnLoan("L1", "U1", true));
    }

    @Test
    void testGetAllLoans_returnsList() {
        when(loanRepository.findAll()).thenReturn(List.of(
                buildLoanEntity("L1", "U1", "B1", LoanStatusEntity.ACTIVE),
                buildLoanEntity("L2", "U2", "B2", LoanStatusEntity.RETURNED)
        ));
        assertEquals(2, loanService.getAllLoans().size());
    }

    @Test
    void testGetLoansByUser_filtersCorrectly() {
        when(userService.getUserById("U1")).thenReturn(buildUser("U1"));
        when(loanRepository.findByUserId("U1")).thenReturn(List.of(
                buildLoanEntity("L1", "U1", "B1", LoanStatusEntity.ACTIVE)
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
}
