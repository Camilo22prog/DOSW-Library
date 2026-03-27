package edu.eci.dosw.DOSW_Library.core.exception;

import lombok.Getter;

@Getter
public class LoadLimitExceededException extends RuntimeException {
    private final String userId;

    public LoadLimitExceededException(String userId) {
        super("El usuario con ID '" + userId + "' ha superado el límite de préstamos activos.");
        this.userId = userId;
    }
}