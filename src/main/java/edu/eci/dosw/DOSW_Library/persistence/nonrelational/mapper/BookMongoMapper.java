package edu.eci.dosw.DOSW_Library.persistence.nonrelational.mapper;

import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.BookDocument;
import edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.embedded.BookAvailability;
import edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.enums.BookStatus;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BookMongoMapper {

    public BookDocument toDocument(Book book) {
        return BookDocument.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .availability(BookAvailability.builder()
                        .totalCopies(book.getTotalCopies())
                        .availableCopies(book.getAvailableCopies())
                        .loanedCopies(book.getTotalCopies() - book.getAvailableCopies())
                        .status(book.getAvailableCopies() > 0 ? BookStatus.AVAILABLE : BookStatus.UNAVAILABLE)
                        .build())
                .build();
    }

    public Book toDomain(BookDocument document) {
        int total = document.getAvailability() != null && document.getAvailability().getTotalCopies() != null
                ? document.getAvailability().getTotalCopies() : 0;
        int available = document.getAvailability() != null && document.getAvailability().getAvailableCopies() != null
                ? document.getAvailability().getAvailableCopies() : 0;

        return Book.builder()
                .id(document.getId())
                .title(document.getTitle())
                .author(document.getAuthor())
                .totalCopies(total)
                .availableCopies(available)
                .build();
    }
}
