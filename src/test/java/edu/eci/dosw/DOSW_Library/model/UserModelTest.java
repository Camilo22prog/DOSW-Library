package edu.eci.dosw.DOSW_Library.model;

import edu.eci.dosw.DOSW_Library.core.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserModelTest {

    @Test
    void testBuilder_allFields() {
        User user = User.builder()
                .id("U1").name("Alice").username("alice")
                .password("secret").librarian(false)
                .build();

        assertEquals("U1", user.getId());
        assertEquals("Alice", user.getName());
        assertEquals("alice", user.getUsername());
        assertEquals("secret", user.getPassword());
        assertFalse(user.isLibrarian());
    }

    @Test
    void testBuilder_librarianRole() {
        User user = User.builder().id("U1").name("Lib").username("lib")
                .password("pass").librarian(true).build();
        assertTrue(user.isLibrarian());
    }

    @Test
    void testBuilder_defaultLibrarianFalse() {
        User user = User.builder().id("U1").name("Alice").username("alice")
                .password("pass").build();
        assertFalse(user.isLibrarian());
    }

    @Test
    void testSetters() {
        User user = new User();
        user.setId("U2");
        user.setName("Bob");
        user.setUsername("bob");
        user.setPassword("pwd");
        user.setLibrarian(true);

        assertEquals("U2", user.getId());
        assertEquals("Bob", user.getName());
        assertEquals("bob", user.getUsername());
        assertEquals("pwd", user.getPassword());
        assertTrue(user.isLibrarian());
    }

    @Test
    void testNoArgsConstructor() {
        User user = new User();
        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getUsername());
    }

    @Test
    void testAllArgsConstructor() {
        User user = new User("U1", "Alice", "alice", "pass", false);
        assertEquals("U1", user.getId());
        assertEquals("Alice", user.getName());
        assertEquals("alice", user.getUsername());
    }
}
