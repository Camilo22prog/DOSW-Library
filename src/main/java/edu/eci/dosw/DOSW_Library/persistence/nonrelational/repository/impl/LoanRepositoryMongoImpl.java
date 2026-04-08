package edu.eci.dosw.DOSW_Library.persistence.nonrelational.repository.impl;

import edu.eci.dosw.DOSW_Library.core.model.Loan;
import edu.eci.dosw.DOSW_Library.core.model.LoanStatus;
import edu.eci.dosw.DOSW_Library.core.repository.LoanRepositoryPort;
import edu.eci.dosw.DOSW_Library.persistence.nonrelational.mapper.LoanMongoMapper;
import edu.eci.dosw.DOSW_Library.persistence.nonrelational.repository.LoanMongoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("mongo")
public class LoanRepositoryMongoImpl implements LoanRepositoryPort {

    private final LoanMongoRepository repository;

    public LoanRepositoryMongoImpl(LoanMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Loan save(Loan loan) {
        return LoanMongoMapper.toDomain(
                repository.save(LoanMongoMapper.toDocument(loan))
        );
    }

    @Override
    public Optional<Loan> findById(String id) {
        return repository.findById(id)
                .map(LoanMongoMapper::toDomain);
    }

    @Override
    public Optional<Loan> findByIdAndStatus(String id, LoanStatus status) {
        var docStatus = edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.enums.LoanStatus.valueOf(status.name());
        return repository.findByIdAndStatus(id, docStatus)
                .map(LoanMongoMapper::toDomain);
    }

    @Override
    public List<Loan> findAll() {
        return repository.findAll()
                .stream()
                .map(LoanMongoMapper::toDomain)
                .toList();
    }

    @Override
    public List<Loan> findByUserId(String userId) {
        return repository.findByUserId(userId)
                .stream()
                .map(LoanMongoMapper::toDomain)
                .toList();
    }

    @Override
    public long countByUserIdAndStatus(String userId, LoanStatus status) {
        var docStatus = edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.enums.LoanStatus.valueOf(status.name());
        return repository.countByUserIdAndStatus(userId, docStatus);
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }
}