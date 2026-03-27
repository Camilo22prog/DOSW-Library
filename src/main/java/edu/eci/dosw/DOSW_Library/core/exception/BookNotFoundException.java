package edu.eci.dosw.DOSW_Library.core.exception;

import lombok.Getter;

@Getter
public class BookNotFoundException extends RuntimeException {
    private final String bookId;

    public BookNotFoundException(String bookId) {
        super("El libro con ID '" + bookId + "' no existe.");
        this.bookId = bookId;
    }
}
