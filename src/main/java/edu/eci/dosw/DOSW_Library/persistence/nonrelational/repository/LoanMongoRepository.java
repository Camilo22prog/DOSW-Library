package edu.eci.dosw.DOSW_Library.persistence.nonrelational.repository;

import edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.LoanDocument;
import edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.enums.LoanStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface LoanMongoRepository extends MongoRepository<LoanDocument, String> {
    List<LoanDocument> findByUserId(String userId);
    Optional<LoanDocument> findByIdAndStatus(String id, LoanStatus status);
    long countByUserIdAndStatus(String userId, LoanStatus status);
}