package edu.eci.dosw.DOSW_Library.persistence.relational.repository.impl;

import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.core.repository.BookRepositoryPort;
import edu.eci.dosw.DOSW_Library.persistence.relational.mapper.BookPersistenceMapper;
import edu.eci.dosw.DOSW_Library.persistence.relational.repository.BookRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("relational")
public class BookRepositoryJpaImpl implements BookRepositoryPort {

    private final BookRepository repository;

    public BookRepositoryJpaImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        return BookPersistenceMapper.toDomain(
                repository.save(BookPersistenceMapper.toEntity(book))
        );
    }

    @Override
    public Optional<Book> findById(String id) {
        return repository.findById(id)
                .map(BookPersistenceMapper::toDomain);
    }

    @Override
    public List<Book> findAll() {
        return repository.findAll()
                .stream()
                .map(BookPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return repository.existsById(id);
    }

    @Override
    public int decrementAvailableCopies(String bookId) {
        return repository.decrementAvailableCopies(bookId);
    }

    @Override
    public int incrementAvailableCopies(String bookId) {
        return repository.incrementAvailableCopies(bookId);
    }
}