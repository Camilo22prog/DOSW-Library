package edu.eci.dosw.DOSW_Library.persistence.nonrelational.mapper;

import edu.eci.dosw.DOSW_Library.core.model.Loan;
import edu.eci.dosw.DOSW_Library.core.model.LoanStatus;
import edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.LoanDocument;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;

@UtilityClass
public class LoanMongoMapper {

    public LoanDocument toDocument(Loan loan) {
        return LoanDocument.builder()
                .id(loan.getId())
                .userId(loan.getUser() != null ? loan.getUser().getId() : null)
                .bookId(loan.getBook() != null ? loan.getBook().getId() : null)
                .status(toDocStatus(loan.getStatus()))
                .loanDate(loan.getLoanDate() != null
                        ? loan.getLoanDate().atStartOfDay() : null)
                .returnDate(loan.getReturnDate() != null
                        ? loan.getReturnDate().atStartOfDay() : null)
                .build();
    }

    public Loan toDomain(LoanDocument document) {
        // Reconstruimos User y Book mínimos desde los IDs guardados
        var user = edu.eci.dosw.DOSW_Library.core.model.User.builder()
                .id(document.getUserId())
                .build();

        var book = edu.eci.dosw.DOSW_Library.core.model.Book.builder()
                .id(document.getBookId())
                .build();

        return Loan.builder()
                .id(document.getId())
                .user(user)
                .book(book)
                .status(toDomainStatus(document.getStatus()))
                .loanDate(toLocalDate(document.getLoanDate()))
                .returnDate(toLocalDate(document.getReturnDate()))
                .build();
    }

    // ── helpers de conversión de enum ──────────────────────────────────────

    private edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.enums.LoanStatus toDocStatus(LoanStatus status) {
        if (status == null) return edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.enums.LoanStatus.ACTIVE;
        return edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.enums.LoanStatus.valueOf(status.name());
    }

    private LoanStatus toDomainStatus(edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.enums.LoanStatus status) {
        if (status == null) return LoanStatus.ACTIVE;
        return LoanStatus.valueOf(status.name());
    }

    private LocalDate toLocalDate(LocalDateTime dt) {
        return dt != null ? dt.toLocalDate() : null;
    }
}