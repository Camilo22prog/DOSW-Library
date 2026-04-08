package edu.eci.dosw.DOSW_Library.persistence.nonrelational.repository;

import edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.BookDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.util.List;
import java.util.Optional;

public interface BookMongoRepository extends MongoRepository<BookDocument, String> {
    Optional<BookDocument> findByIsbn(String isbn);
    boolean existsByIsbn(String isbn);

    @Query("{ 'categories': ?0 }")
    List<BookDocument> findByCategory(String category);

    @Query("{ 'availability.availableCopies': { $gt: 0 } }")
    List<BookDocument> findAvailableBooks();

    List<BookDocument> findByPublicationType(String publicationType);

    @Query("{ '_id': ?0, 'availability.availableCopies': { $gt: 0 } }")
    @Update("{ '$inc': { 'availability.availableCopies': -1, 'availability.loanedCopies': 1 } }")
    long decrementAvailableCopies(String bookId);

    @Query("{ '_id': ?0 }")
    @Update("{ '$inc': { 'availability.availableCopies': 1, 'availability.loanedCopies': -1 } }")
    long incrementAvailableCopies(String bookId);
}
