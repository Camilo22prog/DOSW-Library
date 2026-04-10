package edu.eci.dosw.DOSW_Library.service;

import edu.eci.dosw.DOSW_Library.core.exception.UserNotFoundException;
import edu.eci.dosw.DOSW_Library.core.exception.UsernameAlreadyExistsException;
import edu.eci.dosw.DOSW_Library.core.model.User;
import edu.eci.dosw.DOSW_Library.core.repository.UserRepositoryPort;
import edu.eci.dosw.DOSW_Library.core.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User buildUser(String id, String name, String username, String password) {
        return User.builder().id(id).name(name).username(username).password(password).build();
    }

    @Test
    void testRegisterUser_success() {
        var user = buildUser("U1", "Alice", "alice", "pass123");
        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPass");
        when(userRepository.save(any())).thenReturn(buildUser("U1", "Alice", "alice", "encodedPass"));

        var result = userService.registerUser(user);

        assertEquals("U1", result.getId());
        assertEquals("Alice", result.getName());
        verify(userRepository).save(any());
    }

    @Test
    void testRegisterUser_duplicateUsername_throwsException() {
        var user = buildUser("U1", "Alice", "alice", "pass123");
        when(userRepository.existsByUsername("alice")).thenReturn(true);

        assertThrows(UsernameAlreadyExistsException.class, () -> userService.registerUser(user));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testRegisterUser_nullUser_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(null));
    }

    @Test
    void testRegisterUser_blankName_throwsException() {
        var user = buildUser("U1", "", "alice", "pass");
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user));
    }

    @Test
    void testRegisterUser_blankUsername_throwsException() {
        var user = buildUser("U1", "Alice", "", "pass");
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user));
    }

    @Test
    void testRegisterUser_blankPassword_throwsException() {
        var user = buildUser("U1", "Alice", "alice", "");
        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user));
    }

    @Test
    void testGetAllUsers_returnsList() {
        when(userRepository.findAll()).thenReturn(List.of(
                buildUser("U1", "Alice", "alice", "p"),
                buildUser("U2", "Bob", "bob", "p")
        ));
        assertEquals(2, userService.getAllUsers().size());
    }

    @Test
    void testGetAllUsers_emptyList() {
        when(userRepository.findAll()).thenReturn(List.of());
        assertTrue(userService.getAllUsers().isEmpty());
    }

    @Test
    void testGetUserById_found() {
        when(userRepository.findById("U1"))
                .thenReturn(Optional.of(buildUser("U1", "Alice", "alice", "p")));
        assertEquals("Alice", userService.getUserById("U1").getName());
    }

    @Test
    void testGetUserById_notFound_throwsException() {
        when(userRepository.findById("NONE")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserById("NONE"));
    }

    @Test
    void testGetUserByUsername_found() {
        when(userRepository.findByUsername("alice"))
                .thenReturn(Optional.of(buildUser("U1", "Alice", "alice", "p")));
        assertEquals("U1", userService.getUserByUsername("alice").getId());
    }

    @Test
    void testGetUserByUsername_notFound_throwsException() {
        when(userRepository.findByUsername("nobody")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername("nobody"));
    }
}
