package edu.eci.dosw.DOSW_Library.core.repository;

import edu.eci.dosw.DOSW_Library.core.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepositoryPort {
    Book save(Book book);
    Optional<Book> findById(String id);
    List<Book> findAll();
    void delete(String id);
    boolean existsById(String id);
    int decrementAvailableCopies(String bookId); // ← devuelve filas afectadas
    int incrementAvailableCopies(String bookId);
}
