package edu.eci.dosw.DOSW_Library.core.service;

import edu.eci.dosw.DOSW_Library.core.exception.BookNotAvailableException;
import edu.eci.dosw.DOSW_Library.core.exception.BookNotFoundException;
import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.core.repository.BookRepositoryPort;
import edu.eci.dosw.DOSW_Library.core.validator.BookValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepositoryPort bookRepository; // ← PORT, no implementación concreta

    @Transactional
    public Book addBook(Book book) {
        BookValidator.validate(book);

        if (bookRepository.existsById(book.getId())) {
            var existing = bookRepository.findById(book.getId())
                    .orElseThrow(() -> new BookNotFoundException(book.getId()));

            var updated = Book.builder()
                    .id(existing.getId())
                    .title(existing.getTitle())
                    .author(existing.getAuthor())               // sin isbn
                    .totalCopies(existing.getTotalCopies() + book.getTotalCopies())
                    .availableCopies(existing.getAvailableCopies() + book.getAvailableCopies())
                    .build();

            var saved = bookRepository.save(updated);
            log.info("Copias agregadas al libro '{}'. Total: {}", book.getId(), saved.getTotalCopies());
            return saved;
        }

        var saved = bookRepository.save(book);
        log.info("Libro '{}' registrado con {} copias.", book.getId(), book.getTotalCopies());
        return saved;
    }

    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookRepository.findAll(); // el port ya devuelve List<Book>
    }

    @Transactional(readOnly = true)
    public Book getBookById(String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @Transactional
    public Book updateStock(String id, int totalCopies, int availableCopies) {
        var existing = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        if (totalCopies <= 0) {
            throw new IllegalArgumentException("El total de copias debe ser mayor a 0.");
        }
        if (availableCopies < 0 || availableCopies > totalCopies) {
            throw new IllegalArgumentException(
                    "Las copias disponibles deben estar entre 0 y el total de copias.");
        }

        var updated = Book.builder()
                .id(existing.getId())
                .title(existing.getTitle())
                .author(existing.getAuthor())               // sin isbn
                .totalCopies(totalCopies)
                .availableCopies(availableCopies)
                .build();

        var saved = bookRepository.save(updated);
        log.info("Stock del libro '{}' actualizado. Total: {}, Disponibles: {}", id, totalCopies, availableCopies);
        return saved;
    }

    @Transactional(readOnly = true)
    public boolean hasAvailableCopies(String bookId) {
        return bookRepository.findById(bookId)
                .map(b -> b.getAvailableCopies() > 0)
                .orElse(false);
    }

    @Transactional
    public void decrementCopy(String bookId) {
        int updated = bookRepository.decrementAvailableCopies(bookId);
        if (updated == 0) {
            throw new BookNotAvailableException(bookId);
        }
        log.info("Copia del libro '{}' prestada.", bookId);
    }

    @Transactional
    public void incrementCopy(String bookId) {
        int updated = bookRepository.incrementAvailableCopies(bookId);
        if (updated == 0) {
            log.warn("No se pudo incrementar copia del libro '{}': ya está en stock máximo.", bookId);
        } else {
            log.info("Copia del libro '{}' devuelta.", bookId);
        }
    }
}