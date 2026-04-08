package edu.eci.dosw.DOSW_Library.service;

import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.core.service.BookService;
import edu.eci.dosw.DOSW_Library.persistence.relational.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookServiceValidationTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void testAddBook_nullBook_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(null));
    }

    @Test
    void testAddBook_zeroCopies_throwsException() {
        var book = Book.builder().id("B1").title("Title").author("Author")
                .totalCopies(0).availableCopies(0).build();
        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(book));
    }

    @Test
    void testAddBook_negativeCopies_throwsException() {
        var book = Book.builder().id("B1").title("Title").author("Author")
                .totalCopies(-1).availableCopies(0).build();
        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(book));
    }

    @Test
    void testAddBook_blankId_throwsException() {
        var book = Book.builder().id("").title("Title").author("Author")
                .totalCopies(1).availableCopies(1).build();
        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(book));
    }

    @Test
    void testAddBook_blankTitle_throwsException() {
        var book = Book.builder().id("B1").title("").author("Author")
                .totalCopies(1).availableCopies(1).build();
        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(book));
    }
}
