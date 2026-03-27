package edu.eci.dosw.DOSW_Library.service;

import edu.eci.dosw.DOSW_Library.core.exception.BookNotAvailableException;
import edu.eci.dosw.DOSW_Library.core.exception.BookNotFoundException;
import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.core.service.BookService;
import edu.eci.dosw.DOSW_Library.persistence.entity.BookEntity;
import edu.eci.dosw.DOSW_Library.persistence.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book buildBook(String id, String title, String author, int total, int available) {
        return Book.builder().id(id).title(title).author(author)
                .totalCopies(total).availableCopies(available).build();
    }

    private BookEntity buildEntity(String id, String title, String author, int total, int available) {
        return BookEntity.builder().id(id).title(title).author(author)
                .totalCopies(total).availableCopies(available).build();
    }

    @Test
    void testAddBook_newBook_success() {
        var book = buildBook("B1", "Clean Code", "Martin", 3, 3);
        var entity = buildEntity("B1", "Clean Code", "Martin", 3, 3);
        when(bookRepository.existsById("B1")).thenReturn(false);
        when(bookRepository.save(any())).thenReturn(entity);

        var result = bookService.addBook(book);

        assertEquals("B1", result.getId());
        assertEquals(3, result.getTotalCopies());
        verify(bookRepository).save(any());
    }

    @Test
    void testAddBook_existingBook_accumulates() {
        var book = buildBook("B1", "Clean Code", "Martin", 2, 2);
        var existing = buildEntity("B1", "Clean Code", "Martin", 3, 3);
        var updated = buildEntity("B1", "Clean Code", "Martin", 5, 5);
        when(bookRepository.existsById("B1")).thenReturn(true);
        when(bookRepository.findById("B1")).thenReturn(Optional.of(existing));
        when(bookRepository.save(any())).thenReturn(updated);

        var result = bookService.addBook(book);

        assertEquals(5, result.getTotalCopies());
    }

    @Test
    void testGetAllBooks_returnsList() {
        when(bookRepository.findAll()).thenReturn(List.of(
                buildEntity("B1", "Clean Code", "Martin", 2, 2),
                buildEntity("B2", "Refactoring", "Fowler", 1, 1)
        ));
        assertEquals(2, bookService.getAllBooks().size());
    }

    @Test
    void testGetBookById_found() {
        when(bookRepository.findById("B1"))
                .thenReturn(Optional.of(buildEntity("B1", "Clean Code", "Martin", 2, 2)));
        assertEquals("B1", bookService.getBookById("B1").getId());
    }

    @Test
    void testGetBookById_notFound_throwsException() {
        when(bookRepository.findById("NONE")).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.getBookById("NONE"));
    }

    @Test
    void testUpdateStock_success() {
        var entity = buildEntity("B1", "Title", "Author", 3, 3);
        when(bookRepository.findById("B1")).thenReturn(Optional.of(entity));
        when(bookRepository.save(any())).thenReturn(buildEntity("B1", "Title", "Author", 5, 4));

        var result = bookService.updateStock("B1", 5, 4);

        assertEquals(5, result.getTotalCopies());
        assertEquals(4, result.getAvailableCopies());
    }

    @Test
    void testUpdateStock_zeroTotal_throwsException() {
        var entity = buildEntity("B1", "Title", "Author", 3, 3);
        when(bookRepository.findById("B1")).thenReturn(Optional.of(entity));
        assertThrows(IllegalArgumentException.class, () -> bookService.updateStock("B1", 0, 0));
    }

    @Test
    void testUpdateStock_availableExceedsTotal_throwsException() {
        var entity = buildEntity("B1", "Title", "Author", 3, 3);
        when(bookRepository.findById("B1")).thenReturn(Optional.of(entity));
        assertThrows(IllegalArgumentException.class, () -> bookService.updateStock("B1", 3, 5));
    }

    @Test
    void testUpdateStock_negativeAvailable_throwsException() {
        var entity = buildEntity("B1", "Title", "Author", 3, 3);
        when(bookRepository.findById("B1")).thenReturn(Optional.of(entity));
        assertThrows(IllegalArgumentException.class, () -> bookService.updateStock("B1", 3, -1));
    }

    @Test
    void testHasAvailableCopies_true() {
        when(bookRepository.findById("B1"))
                .thenReturn(Optional.of(buildEntity("B1", "T", "A", 2, 2)));
        assertTrue(bookService.hasAvailableCopies("B1"));
    }

    @Test
    void testHasAvailableCopies_false_whenZero() {
        when(bookRepository.findById("B1"))
                .thenReturn(Optional.of(buildEntity("B1", "T", "A", 1, 0)));
        assertFalse(bookService.hasAvailableCopies("B1"));
    }

    @Test
    void testHasAvailableCopies_false_whenNotFound() {
        when(bookRepository.findById("NONE")).thenReturn(Optional.empty());
        assertFalse(bookService.hasAvailableCopies("NONE"));
    }

    @Test
    void testDecrementCopy_success() {
        when(bookRepository.decrementAvailableCopies("B1")).thenReturn(1);
        assertDoesNotThrow(() -> bookService.decrementCopy("B1"));
    }

    @Test
    void testDecrementCopy_noStock_throwsException() {
        when(bookRepository.decrementAvailableCopies("B1")).thenReturn(0);
        assertThrows(BookNotAvailableException.class, () -> bookService.decrementCopy("B1"));
    }

    @Test
    void testIncrementCopy_success() {
        when(bookRepository.incrementAvailableCopies("B1")).thenReturn(1);
        assertDoesNotThrow(() -> bookService.incrementCopy("B1"));
    }

    @Test
    void testAddBook_nullBook_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(null));
    }

    @Test
    void testAddBook_zeroTotalCopies_throwsException() {
        var book = buildBook("B1", "Title", "Author", 0, 0);
        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(book));
    }
}
