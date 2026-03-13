package edu.eci.dosw.DOSW_Library.core.exception;

public class LoadLimitExceededException extends RuntimeException {
    public LoadLimitExceededException(String message) {
        super(message);
    }
}
