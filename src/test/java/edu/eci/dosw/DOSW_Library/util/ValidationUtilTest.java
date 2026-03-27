package edu.eci.dosw.DOSW_Library.util;

import edu.eci.dosw.DOSW_Library.core.util.ValidationUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilTest {

    @Test
    void testIsNullOrBlank_null() {
        assertTrue(ValidationUtil.isNullOrBlank(null));
    }

    @Test
    void testIsNullOrBlank_blank() {
        assertTrue(ValidationUtil.isNullOrBlank("   "));
    }

    @Test
    void testIsNullOrBlank_empty() {
        assertTrue(ValidationUtil.isNullOrBlank(""));
    }

    @Test
    void testIsNullOrBlank_validValue() {
        assertFalse(ValidationUtil.isNullOrBlank("valor"));
    }

    @Test
    void testRequireNonBlank_valid_noException() {
        assertDoesNotThrow(() -> ValidationUtil.requireNonBlank("valor", "campo"));
    }

    @Test
    void testRequireNonBlank_blank_throwsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.requireNonBlank("", "titulo"));
        assertTrue(ex.getMessage().contains("titulo"));
    }

    @Test
    void testRequirePositive_valid_noException() {
        assertDoesNotThrow(() -> ValidationUtil.requirePositive(5, "copias"));
    }

    @Test
    void testRequirePositive_zero_throwsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.requirePositive(0, "copias"));
        assertTrue(ex.getMessage().contains("copias"));
    }

    @Test
    void testRequirePositive_negative_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.requirePositive(-3, "copias"));
    }

    @Test
    void testRequireNonNull_valid_noException() {
        assertDoesNotThrow(() -> ValidationUtil.requireNonNull("objeto", "campo"));
    }

    @Test
    void testRequireNonNull_null_throwsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.requireNonNull(null, "libro"));
        assertTrue(ex.getMessage().contains("libro"));
    }
}