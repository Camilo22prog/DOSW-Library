package edu.eci.dosw.DOSW_Library.core.repository;

import edu.eci.dosw.DOSW_Library.core.model.Loan;
import edu.eci.dosw.DOSW_Library.core.model.LoanStatus;

import java.util.List;
import java.util.Optional;

public interface LoanRepositoryPort {
    Loan save(Loan loan);
    Optional<Loan> findById(String id);
    Optional<Loan> findByIdAndStatus(String id, LoanStatus status); // ← usa modelo de dominio
    List<Loan> findAll();
    List<Loan> findByUserId(String userId);
    long countByUserIdAndStatus(String userId, LoanStatus status);  // ← usa modelo de dominio
    void delete(String id);
}