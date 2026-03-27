package edu.eci.dosw.DOSW_Library.controller;

import edu.eci.dosw.DOSW_Library.controller.dto.LoanDTO;
import edu.eci.dosw.DOSW_Library.controller.mapper.LoanMapper;
import edu.eci.dosw.DOSW_Library.core.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    @PreAuthorize("hasRole('LIBRARIAN') or (hasRole('USER') and #userId == authentication.name)")
    public ResponseEntity<LoanDTO> createLoan(
            @RequestParam String userId,
            @RequestParam String bookId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(LoanMapper.toDTO(loanService.createLoan(userId, bookId)));
    }

    @PatchMapping("/{loanId}/return")
    @PreAuthorize("hasAnyRole('USER', 'LIBRARIAN')")
    public ResponseEntity<LoanDTO> returnLoan(@PathVariable String loanId, Authentication authentication) {
        String callerId = (String) authentication.getPrincipal();
        boolean isLibrarian = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_LIBRARIAN"));
        return ResponseEntity.ok(LoanMapper.toDTO(loanService.returnLoan(loanId, callerId, isLibrarian)));
    }

    @GetMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<List<LoanDTO>> getAllLoans() {
        List<LoanDTO> list = loanService.getAllLoans().stream()
                .map(LoanMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('LIBRARIAN') or (hasRole('USER') and #userId == authentication.name)")
    public ResponseEntity<List<LoanDTO>> getLoansByUser(@PathVariable String userId) {
        List<LoanDTO> list = loanService.getLoansByUser(userId).stream()
                .map(LoanMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }
}
