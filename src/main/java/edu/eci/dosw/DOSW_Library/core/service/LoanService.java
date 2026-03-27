package edu.eci.dosw.DOSW_Library.core.service;

import edu.eci.dosw.DOSW_Library.core.exception.BookNotAvailableException;
import edu.eci.dosw.DOSW_Library.core.exception.LoadLimitExceededException;
import edu.eci.dosw.DOSW_Library.core.exception.LoanNotFoundException;
import edu.eci.dosw.DOSW_Library.core.model.*;
import edu.eci.dosw.DOSW_Library.core.util.IdGeneratorUtil;
import edu.eci.dosw.DOSW_Library.core.validator.LoanValidator;
import edu.eci.dosw.DOSW_Library.persistence.entity.LoanStatusEntity;
import edu.eci.dosw.DOSW_Library.persistence.mapper.LoanPersistenceMapper;
import edu.eci.dosw.DOSW_Library.persistence.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanService {

    private static final int MAX_LOANS_PER_USER = 3;

    private final LoanRepository loanRepository;
    private final BookService bookService;
    private final UserService userService;

    @Transactional
    public Loan createLoan(String userId, String bookId) {
        LoanValidator.validateLoanRequest(userId, bookId);

        var user = userService.getUserById(userId);
        var book = bookService.getBookById(bookId);

        long activeLoans = loanRepository.countByUserIdAndStatus(userId, LoanStatusEntity.ACTIVE);
        if (activeLoans >= MAX_LOANS_PER_USER) {
            throw new LoadLimitExceededException(userId);
        }

        if (!bookService.hasAvailableCopies(bookId)) {
            throw new BookNotAvailableException(bookId);
        }

        bookService.decrementCopy(bookId);

        var loan = Loan.builder()
                .id(IdGeneratorUtil.generateId())
                .book(book)
                .user(user)
                .loanDate(LocalDate.now())
                .status(LoanStatus.ACTIVE)
                .build();

        var saved = loanRepository.save(LoanPersistenceMapper.toEntity(loan));
        log.info("Préstamo creado: usuario '{}' tomó el libro '{}'.", userId, bookId);
        return LoanPersistenceMapper.toDomain(saved);
    }

    @Transactional
    public Loan returnLoan(String loanId, String callerId, boolean isLibrarian) {
        LoanValidator.validateReturnRequest(loanId);

        var entity = loanRepository.findByIdAndStatus(loanId, LoanStatusEntity.ACTIVE)
                .orElseThrow(() -> new LoanNotFoundException(loanId));

        if (!isLibrarian && !entity.getUser().getId().equals(callerId)) {
            throw new AccessDeniedException("No tienes permisos para devolver un préstamo ajeno");
        }

        entity.setStatus(LoanStatusEntity.RETURNED);
        entity.setReturnDate(LocalDate.now());
        var saved = loanRepository.save(entity);

        bookService.incrementCopy(entity.getBook().getId());
        log.info("Préstamo '{}' devuelto.", loanId);
        return LoanPersistenceMapper.toDomain(saved);
    }

    @Transactional(readOnly = true)
    public List<Loan> getAllLoans() {
        return loanRepository.findAll().stream()
                .map(LoanPersistenceMapper::toDomain)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Loan> getLoansByUser(String userId) {
        userService.getUserById(userId);
        return loanRepository.findByUserId(userId).stream()
                .map(LoanPersistenceMapper::toDomain)
                .toList();
    }
}
