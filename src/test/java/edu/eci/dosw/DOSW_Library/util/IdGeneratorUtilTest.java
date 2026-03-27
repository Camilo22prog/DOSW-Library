package edu.eci.dosw.DOSW_Library.util;

import edu.eci.dosw.DOSW_Library.core.util.IdGeneratorUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdGeneratorUtilTest {

    @Test
    void testGenerateId_notNull() {
        assertNotNull(IdGeneratorUtil.generateId());
    }

    @Test
    void testGenerateId_isUnique() {
        String id1 = IdGeneratorUtil.generateId();
        String id2 = IdGeneratorUtil.generateId();
        assertNotEquals(id1, id2);
    }

    @Test
    void testGeneratePrefixedId_containsPrefix() {
        String id = IdGeneratorUtil.generatePrefixedId("book");
        assertTrue(id.startsWith("BOOK-"));
    }

    @Test
    void testGeneratePrefixedId_nullPrefix_returnsUUID() {
        String id = IdGeneratorUtil.generatePrefixedId(null);
        assertNotNull(id);
        assertFalse(id.startsWith("-"));
    }

    @Test
    void testGeneratePrefixedId_blankPrefix_returnsUUID() {
        String id = IdGeneratorUtil.generatePrefixedId("  ");
        assertNotNull(id);
    }

    @Test
    void testGeneratePrefixedId_isUnique() {
        String id1 = IdGeneratorUtil.generatePrefixedId("loan");
        String id2 = IdGeneratorUtil.generatePrefixedId("loan");
        assertNotEquals(id1, id2);
    }
}