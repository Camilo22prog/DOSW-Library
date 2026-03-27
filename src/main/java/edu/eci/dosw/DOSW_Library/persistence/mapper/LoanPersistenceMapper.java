package edu.eci.dosw.DOSW_Library.persistence.mapper;

import edu.eci.dosw.DOSW_Library.core.model.Loan;
import edu.eci.dosw.DOSW_Library.core.model.LoanStatus;
import edu.eci.dosw.DOSW_Library.persistence.entity.LoanEntity;
import edu.eci.dosw.DOSW_Library.persistence.entity.LoanStatusEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LoanPersistenceMapper {

    public LoanEntity toEntity(Loan loan) {
        return LoanEntity.builder()
                .id(loan.getId())
                .book(BookPersistenceMapper.toEntity(loan.getBook()))
                .user(UserPersistenceMapper.toEntity(loan.getUser()))
                .loanDate(loan.getLoanDate())
                .returnDate(loan.getReturnDate())
                .status(loan.getStatus() == LoanStatus.ACTIVE
                        ? LoanStatusEntity.ACTIVE
                        : LoanStatusEntity.RETURNED)
                .build();
    }

    public Loan toDomain(LoanEntity entity) {
        return Loan.builder()
                .id(entity.getId())
                .book(BookPersistenceMapper.toDomain(entity.getBook()))
                .user(UserPersistenceMapper.toDomain(entity.getUser()))
                .loanDate(entity.getLoanDate())
                .returnDate(entity.getReturnDate())
                .status(entity.getStatus() == LoanStatusEntity.ACTIVE
                        ? LoanStatus.ACTIVE
                        : LoanStatus.RETURNED)
                .build();
    }
}
