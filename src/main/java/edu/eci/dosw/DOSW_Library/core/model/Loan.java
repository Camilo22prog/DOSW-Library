package edu.eci.dosw.DOSW_Library.core.model;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {
    private String id;
    private Book book;
    private User user;
    private LocalDate loanDate;
    private LocalDate returnDate;

    @Builder.Default
    private LoanStatus status = LoanStatus.ACTIVE;
}