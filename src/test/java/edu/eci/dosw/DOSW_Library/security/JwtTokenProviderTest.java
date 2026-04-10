package edu.eci.dosw.DOSW_Library.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        JwtProperties properties = new JwtProperties();
        properties.setSecret("dosw-library-super-secret-key-eci-colombia-noveno-semestre-2024");
        properties.setExpiration(86400000L);
        jwtTokenProvider = new JwtTokenProvider(properties);
    }

    @Test
    void testGenerateToken_returnsNonEmptyToken() {
        String token = jwtTokenProvider.generateToken("U1", "alice", "USER");
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void testParseClaims_validToken_returnsCorrectClaims() {
        String token = jwtTokenProvider.generateToken("U1", "alice", "USER");
        var claims = jwtTokenProvider.parseClaims(token);
        assertEquals("U1", claims.getSubject());
        assertEquals("alice", claims.get("username"));
        assertEquals("USER", claims.get("role"));
    }

    @Test
    void testValidateToken_validToken_returnsTrue() {
        String token = jwtTokenProvider.generateToken("U1", "alice", "USER");
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void testValidateToken_invalidToken_returnsFalse() {
        assertFalse(jwtTokenProvider.validateToken("invalid.token.value"));
    }

    @Test
    void testValidateToken_emptyToken_returnsFalse() {
        assertFalse(jwtTokenProvider.validateToken(""));
    }

    @Test
    void testGenerateToken_librarianRole_isEmbedded() {
        String token = jwtTokenProvider.generateToken("U2", "lib", "LIBRARIAN");
        var claims = jwtTokenProvider.parseClaims(token);
        assertEquals("LIBRARIAN", claims.get("role"));
    }

    @Test
    void testGenerateToken_differentUsers_produceDifferentTokens() {
        String t1 = jwtTokenProvider.generateToken("U1", "alice", "USER");
        String t2 = jwtTokenProvider.generateToken("U2", "bob", "LIBRARIAN");
        assertNotEquals(t1, t2);
    }
}
