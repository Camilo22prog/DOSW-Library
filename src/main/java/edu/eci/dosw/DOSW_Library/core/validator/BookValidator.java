package edu.eci.dosw.DOSW_Library.core.validator;

import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.core.util.ValidationUtil;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BookValidator {

    public void validate(Book book) {
        ValidationUtil.requireNonNull(book, "book");
        ValidationUtil.requireNonBlank(book.getId(), "id");
        ValidationUtil.requireNonBlank(book.getTitle(), "title");
        ValidationUtil.requireNonBlank(book.getAuthor(), "author");
        ValidationUtil.requirePositive(book.getTotalCopies(), "totalCopies");
        if (book.getAvailableCopies() < 0) {
            throw new IllegalArgumentException("El campo 'availableCopies' no puede ser menor a 0.");
        }
        if (book.getAvailableCopies() > book.getTotalCopies()) {
            throw new IllegalArgumentException(
                    "Las copias disponibles no pueden superar el total de copias.");
        }
    }
}