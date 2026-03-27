package edu.eci.dosw.DOSW_Library.core.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
    private final String userId;

    public UserNotFoundException(String userId) {
        super("El usuario con ID '" + userId + "' no fue encontrado.");
        this.userId = userId;
    }
}