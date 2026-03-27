package edu.eci.dosw.DOSW_Library.persistence.repository;

import edu.eci.dosw.DOSW_Library.persistence.entity.LoanEntity;
import edu.eci.dosw.DOSW_Library.persistence.entity.LoanStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, String> {

    List<LoanEntity> findByUserId(String userId);

    long countByUserIdAndStatus(String userId, LoanStatusEntity status);

    Optional<LoanEntity> findByIdAndStatus(String id, LoanStatusEntity status);
}
