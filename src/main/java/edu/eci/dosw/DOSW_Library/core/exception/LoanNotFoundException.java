package edu.eci.dosw.DOSW_Library.core.exception;

import lombok.Getter;

@Getter
public class LoanNotFoundException extends RuntimeException {
    private final String loanId;

    public LoanNotFoundException(String loanId) {
        super("El préstamo con ID '" + loanId + "' no fue encontrado o ya fue devuelto.");
        this.loanId = loanId;
    }
}
