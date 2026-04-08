package edu.eci.dosw.DOSW_Library.core.service;

import edu.eci.dosw.DOSW_Library.core.exception.BookNotAvailableException;
import edu.eci.dosw.DOSW_Library.core.exception.LoadLimitExceededException;
import edu.eci.dosw.DOSW_Library.core.exception.LoanNotFoundException;
import edu.eci.dosw.DOSW_Library.core.model.Loan;
import edu.eci.dosw.DOSW_Library.core.model.LoanStatus;
import edu.eci.dosw.DOSW_Library.core.repository.LoanRepositoryPort;
import edu.eci.dosw.DOSW_Library.core.util.IdGeneratorUtil;
import edu.eci.dosw.DOSW_Library.core.validator.LoanValidator;
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

    private final LoanRepositoryPort loanRepository; // ← PORT
    private final BookService bookService;
    private final UserService userService;

    @Transactional
    public Loan createLoan(String userId, String bookId) {
        LoanValidator.validateLoanRequest(userId, bookId);

        var user = userService.getUserById(userId);
        var book = bookService.getBookById(bookId);

        // Usamos LoanStatus de dominio, no LoanStatusEntity
        long activeLoans = loanRepository.countByUserIdAndStatus(userId, LoanStatus.ACTIVE);
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

        var saved = loanRepository.save(loan); // el port hace el mapeo internamente
        log.info("Préstamo creado: usuario '{}' tomó el libro '{}'.", userId, bookId);
        return saved;
    }

    @Transactional
    public Loan returnLoan(String loanId, String callerId, boolean isLibrarian) {
        LoanValidator.validateReturnRequest(loanId);

        // Buscamos en dominio con LoanStatus.ACTIVE, no LoanStatusEntity
        var loan = loanRepository.findByIdAndStatus(loanId, LoanStatus.ACTIVE)
                .orElseThrow(() -> new LoanNotFoundException(loanId));

        if (!isLibrarian && !loan.getUser().getId().equals(callerId)) {
            throw new AccessDeniedException("No tienes permisos para devolver un préstamo ajeno");
        }

        // Mutamos el dominio, no la entidad
        var returned = Loan.builder()
                .id(loan.getId())
                .book(loan.getBook())
                .user(loan.getUser())
                .loanDate(loan.getLoanDate())
                .returnDate(LocalDate.now())
                .status(LoanStatus.RETURNED)
                .build();

        var saved = loanRepository.save(returned);
        bookService.incrementCopy(loan.getBook().getId());
        log.info("Préstamo '{}' devuelto.", loanId);
        return saved;
    }

    @Transactional(readOnly = true)
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Loan> getLoansByUser(String userId) {
        userService.getUserById(userId); // valida que el usuario exista
        return loanRepository.findByUserId(userId);
    }
}