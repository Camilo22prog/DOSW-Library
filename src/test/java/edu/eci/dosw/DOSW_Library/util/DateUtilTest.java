package edu.eci.dosw.DOSW_Library.util;

import edu.eci.dosw.DOSW_Library.core.util.DateUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilTest {

    @Test
    void testFormat_returnsFormattedDate() {
        LocalDate date = LocalDate.of(2024, 6, 15);
        assertEquals("15/06/2024", DateUtil.format(date));
    }

    @Test
    void testFormat_nullReturnsNull() {
        assertNull(DateUtil.format(null));
    }

    @Test
    void testParse_validString() {
        LocalDate date = DateUtil.parse("15/06/2024");
        assertEquals(LocalDate.of(2024, 6, 15), date);
    }

    @Test
    void testParse_nullOrBlankReturnsNull() {
        assertNull(DateUtil.parse(null));
        assertNull(DateUtil.parse("   "));
    }

    @Test
    void testDaysBetween_correctCount() {
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 11);
        assertEquals(10, DateUtil.daysBetween(start, end));
    }

    @Test
    void testDaysBetween_nullDatesReturnZero() {
        assertEquals(0, DateUtil.daysBetween(null, LocalDate.now()));
        assertEquals(0, DateUtil.daysBetween(LocalDate.now(), null));
    }

    @Test
    void testIsOverdue_true() {
        LocalDate oldDate = LocalDate.now().minusDays(20);
        assertTrue(DateUtil.isOverdue(oldDate, 14));
    }

    @Test
    void testIsOverdue_false() {
        LocalDate recentDate = LocalDate.now().minusDays(5);
        assertFalse(DateUtil.isOverdue(recentDate, 14));
    }

    @Test
    void testIsOverdue_nullReturnsFalse() {
        assertFalse(DateUtil.isOverdue(null, 14));
    }

    @Test
    void testToday_returnsCurrentDate() {
        assertEquals(LocalDate.now(), DateUtil.today());
    }
}