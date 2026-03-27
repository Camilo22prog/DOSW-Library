package edu.eci.dosw.DOSW_Library.core.exception;

import lombok.Getter;

@Getter
public class BookNotAvailableException extends RuntimeException {
    private final String bookId;

    public BookNotAvailableException(String bookId) {
        super("El libro con ID '" + bookId + "' no está disponible o no existe.");
        this.bookId = bookId;
    }
}