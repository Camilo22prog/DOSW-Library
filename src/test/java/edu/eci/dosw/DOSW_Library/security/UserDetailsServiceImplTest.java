package edu.eci.dosw.DOSW_Library.security;

import edu.eci.dosw.DOSW_Library.core.model.User;
import edu.eci.dosw.DOSW_Library.core.repository.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepositoryPort userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void testLoadUserByUsername_regularUser_returnsUserRole() {
        var user = User.builder().id("U1").name("Alice").username("alice")
                .password("encodedPass").librarian(false).build();
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));

        var result = userDetailsService.loadUserByUsername("alice");

        assertEquals("alice", result.getUsername());
        assertEquals("encodedPass", result.getPassword());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testLoadUserByUsername_librarian_returnsLibrarianRole() {
        var user = User.builder().id("U2").name("Lib").username("lib")
                .password("pass").librarian(true).build();
        when(userRepository.findByUsername("lib")).thenReturn(Optional.of(user));

        var result = userDetailsService.loadUserByUsername("lib");

        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_LIBRARIAN")));
    }

    @Test
    void testLoadUserByUsername_notFound_throwsUsernameNotFoundException() {
        when(userRepository.findByUsername("nobody")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("nobody"));
    }
}
