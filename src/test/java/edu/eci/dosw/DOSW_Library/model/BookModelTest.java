package edu.eci.dosw.DOSW_Library.model;

import edu.eci.dosw.DOSW_Library.core.model.Book;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookModelTest {

    @Test
    void testBuilder_allFields() {
        Book book = Book.builder()
                .id("B1").title("Clean Code").author("Martin")
                .totalCopies(5).availableCopies(3)
                .build();

        assertEquals("B1", book.getId());
        assertEquals("Clean Code", book.getTitle());
        assertEquals("Martin", book.getAuthor());
        assertEquals(5, book.getTotalCopies());
        assertEquals(3, book.getAvailableCopies());
        assertTrue(book.isAvailable());
    }

    @Test
    void testIsAvailable_trueWhenCopiesGtZero() {
        Book book = Book.builder().id("B1").title("T").author("A")
                .totalCopies(1).availableCopies(1).build();
        assertTrue(book.isAvailable());
    }

    @Test
    void testIsAvailable_falseWhenCopiesZero() {
        Book book = Book.builder().id("B1").title("T").author("A")
                .totalCopies(1).availableCopies(0).build();
        assertFalse(book.isAvailable());
    }

    @Test
    void testSetters() {
        Book book = new Book();
        book.setId("B2");
        book.setTitle("Refactoring");
        book.setAuthor("Fowler");
        book.setTotalCopies(4);
        book.setAvailableCopies(2);

        assertEquals("B2", book.getId());
        assertEquals("Refactoring", book.getTitle());
        assertEquals("Fowler", book.getAuthor());
        assertEquals(4, book.getTotalCopies());
        assertEquals(2, book.getAvailableCopies());
    }

    @Test
    void testNoArgsConstructor() {
        Book book = new Book();
        assertNull(book.getId());
        assertNull(book.getTitle());
        assertNull(book.getAuthor());
        assertEquals(0, book.getTotalCopies());
        assertEquals(0, book.getAvailableCopies());
    }

    @Test
    void testAllArgsConstructor() {
        Book book = new Book("B1", "Title", "Author", 3, 3);
        assertEquals("B1", book.getId());
        assertEquals(3, book.getTotalCopies());
    }
}
