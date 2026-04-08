package edu.eci.dosw.DOSW_Library.persistence.relational.repository.impl;

import edu.eci.dosw.DOSW_Library.core.model.Loan;
import edu.eci.dosw.DOSW_Library.core.model.LoanStatus;
import edu.eci.dosw.DOSW_Library.core.repository.LoanRepositoryPort;
import edu.eci.dosw.DOSW_Library.persistence.relational.entity.LoanStatusEntity;
import edu.eci.dosw.DOSW_Library.persistence.relational.mapper.LoanPersistenceMapper;
import edu.eci.dosw.DOSW_Library.persistence.relational.repository.LoanRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("relational")
public class LoanRepositoryJpaImpl implements LoanRepositoryPort {

    private final LoanRepository repository;

    public LoanRepositoryJpaImpl(LoanRepository repository) {
        this.repository = repository;
    }

    @Override
    public Loan save(Loan loan) {
        return LoanPersistenceMapper.toDomain(
                repository.save(LoanPersistenceMapper.toEntity(loan))
        );
    }

    @Override
    public Optional<Loan> findById(String id) {
        return repository.findById(id)
                .map(LoanPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Loan> findByIdAndStatus(String id, LoanStatus status) {
        LoanStatusEntity statusEntity = status == LoanStatus.ACTIVE
                ? LoanStatusEntity.ACTIVE : LoanStatusEntity.RETURNED;
        return repository.findByIdAndStatus(id, statusEntity)
                .map(LoanPersistenceMapper::toDomain);
    }

    @Override
    public List<Loan> findAll() {
        return repository.findAll()
                .stream()
                .map(LoanPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Loan> findByUserId(String userId) {
        return repository.findByUserId(userId)
                .stream()
                .map(LoanPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public long countByUserIdAndStatus(String userId, LoanStatus status) {
        LoanStatusEntity statusEntity = status == LoanStatus.ACTIVE
                ? LoanStatusEntity.ACTIVE : LoanStatusEntity.RETURNED;
        return repository.countByUserIdAndStatus(userId, statusEntity);
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }
}
