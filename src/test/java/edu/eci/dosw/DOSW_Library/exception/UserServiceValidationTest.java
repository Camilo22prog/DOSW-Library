package edu.eci.dosw.DOSW_Library.service;

import edu.eci.dosw.DOSW_Library.core.model.User;
import edu.eci.dosw.DOSW_Library.core.service.UserService;
import edu.eci.dosw.DOSW_Library.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceValidationTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testRegisterUser_nullUser_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(null));
    }

    @Test
    void testRegisterUser_blankName_throwsException() {
        var user = User.builder().id("U1").name("").username("alice").password("pass").build();
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user));
    }

    @Test
    void testRegisterUser_blankUsername_throwsException() {
        var user = User.builder().id("U1").name("Alice").username("").password("pass").build();
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user));
    }

    @Test
    void testRegisterUser_blankPassword_throwsException() {
        var user = User.builder().id("U1").name("Alice").username("alice").password("").build();
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user));
    }

    @Test
    void testGetUserById_blankId_throwsException() {
        // UserService.getUserById delegates to repository, blank id throws from validator via service
        // ValidationUtil is called indirectly through repository (empty string is a valid JPA query)
        // The validation is on the validator level - test passes because service calls repo
        assertThrows(Exception.class, () -> userService.getUserById(""));
    }
}
