package edu.eci.dosw.DOSW_Library.validator;

import edu.eci.dosw.DOSW_Library.core.model.User;
import edu.eci.dosw.DOSW_Library.core.validator.UserValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

    private User buildUser(String id, String name, String username, String password) {
        return User.builder().id(id).name(name).username(username).password(password).build();
    }

    @Test
    void testValidate_validUser_noException() {
        assertDoesNotThrow(() -> UserValidator.validate(buildUser("U1", "Alice", "alice", "pass123")));
    }

    @Test
    void testValidate_nullUser_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> UserValidator.validate(null));
    }

    @Test
    void testValidate_blankId_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> UserValidator.validate(buildUser("", "Alice", "alice", "pass")));
    }

    @Test
    void testValidate_blankName_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> UserValidator.validate(buildUser("U1", "", "alice", "pass")));
    }

    @Test
    void testValidate_blankUsername_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> UserValidator.validate(buildUser("U1", "Alice", "", "pass")));
    }

    @Test
    void testValidate_blankPassword_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> UserValidator.validate(buildUser("U1", "Alice", "alice", "")));
    }

    @Test
    void testValidate_nullUsername_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> UserValidator.validate(buildUser("U1", "Alice", null, "pass")));
    }

    @Test
    void testValidate_nullPassword_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> UserValidator.validate(buildUser("U1", "Alice", "alice", null)));
    }
}
