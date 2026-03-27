package edu.eci.dosw.DOSW_Library.core.util;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class IdGeneratorUtil {

    public String generateId() {
        return UUID.randomUUID().toString();
    }

    public String generatePrefixedId(String prefix) {
        if (prefix == null || prefix.isBlank()) {
            return generateId();
        }
        return prefix.toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}