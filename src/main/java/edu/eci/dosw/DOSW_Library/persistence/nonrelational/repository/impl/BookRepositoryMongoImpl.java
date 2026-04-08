
package edu.eci.dosw.DOSW_Library.persistence.nonrelational.repository.impl;

import edu.eci.dosw.DOSW_Library.core.model.Book;
import edu.eci.dosw.DOSW_Library.core.repository.BookRepositoryPort;
import edu.eci.dosw.DOSW_Library.persistence.nonrelational.mapper.BookMongoMapper;
import edu.eci.dosw.DOSW_Library.persistence.nonrelational.repository.BookMongoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("mongo")
public class BookRepositoryMongoImpl implements BookRepositoryPort {

    private final BookMongoRepository repository;

    public BookRepositoryMongoImpl(BookMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        return BookMongoMapper.toDomain(
                repository.save(BookMongoMapper.toDocument(book))
        );
    }

    @Override
    public Optional<Book> findById(String id) {
        return repository.findById(id)
                .map(BookMongoMapper::toDomain);
    }

    @Override
    public List<Book> findAll() {
        return repository.findAll()
                .stream()
                .map(BookMongoMapper::toDomain)
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
        return (int) repository.decrementAvailableCopies(bookId);
    }

    @Override
    public int incrementAvailableCopies(String bookId) {
        return (int) repository.incrementAvailableCopies(bookId);
    }
}