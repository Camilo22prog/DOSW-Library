package edu.eci.dosw.DOSW_Library.core.service;

import edu.eci.dosw.DOSW_Library.core.exception.BookNotAvailableException;
import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.core.model.Loan;
import edu.eci.dosw.DOSW_Library.core.model.LoanStatus;
import edu.eci.dosw.DOSW_Library.core.model.User;
import edu.eci.dosw.DOSW_Library.core.model.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanService {

    private final BookService bookService;
    private final UserService userService;
    private final List<Loan> loans = new ArrayList<>();

    public LoanService(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    public Loan borrowBook(Long bookId, Long userId) {
        Book book = bookService.getBookById(bookId);
        if (!book.isAvailable()) {
            throw new BookNotAvailableException("El libro no está disponible");
        }
        User user = userService.getUserById(userId);
        book.setAvailable(false);
        Loan loan = new Loan(book, user, LocalDate.now(), null, LoanStatus.ACTIVE);
        loans.add(loan);
        return loan;
    }
}