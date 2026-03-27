package edu.eci.dosw.DOSW_Library.validator;

import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.core.validator.BookValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookValidatorTest {

    private Book buildBook(String id, String title, String author, int total, int available) {
        return Book.builder().id(id).title(title).author(author)
                .totalCopies(total).availableCopies(available).build();
    }

    @Test
    void testValidate_validBook_noException() {
        assertDoesNotThrow(() -> BookValidator.validate(buildBook("B1", "Clean Code", "Martin", 3, 3)));
    }

    @Test
    void testValidate_nullBook_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> BookValidator.validate(null));
    }

    @Test
    void testValidate_blankId_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> BookValidator.validate(buildBook("", "Clean Code", "Martin", 1, 1)));
    }

    @Test
    void testValidate_blankTitle_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> BookValidator.validate(buildBook("B1", "", "Martin", 1, 1)));
    }

    @Test
    void testValidate_blankAuthor_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> BookValidator.validate(buildBook("B1", "Title", "", 1, 1)));
    }

    @Test
    void testValidate_zeroTotalCopies_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> BookValidator.validate(buildBook("B1", "Title", "Author", 0, 0)));
    }

    @Test
    void testValidate_negativeTotalCopies_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> BookValidator.validate(buildBook("B1", "Title", "Author", -1, 0)));
    }

    @Test
    void testValidate_negativeAvailableCopies_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> BookValidator.validate(buildBook("B1", "Title", "Author", 3, -1)));
    }

    @Test
    void testValidate_availableExceedsTotal_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> BookValidator.validate(buildBook("B1", "Title", "Author", 2, 5)));
    }

    @Test
    void testValidate_zeroAvailableAllowed() {
        assertDoesNotThrow(() -> BookValidator.validate(buildBook("B1", "Title", "Author", 3, 0)));
    }
}
