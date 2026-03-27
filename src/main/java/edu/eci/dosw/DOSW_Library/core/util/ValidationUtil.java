package edu.eci.dosw.DOSW_Library.core.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationUtil {

    public boolean isNullOrBlank(String value) {
        return value == null || value.isBlank();
    }

    public void requireNonBlank(String value, String fieldName) {
        if (isNullOrBlank(value)) {
            throw new IllegalArgumentException("El campo '" + fieldName + "' no puede estar vacío.");
        }
    }

    public void requirePositive(int value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException(
                    "El campo '" + fieldName + "' debe ser mayor a 0. Valor recibido: " + value);
        }
    }

    public void requireNonNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException("El campo '" + fieldName + "' no puede ser nulo.");
        }
    }
}