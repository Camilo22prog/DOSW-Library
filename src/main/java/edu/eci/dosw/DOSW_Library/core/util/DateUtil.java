package edu.eci.dosw.DOSW_Library.core.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@UtilityClass
public class DateUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public String format(LocalDate date) {
        if (date == null) return null;
        return date.format(FORMATTER);
    }

    public LocalDate parse(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) return null;
        return LocalDate.parse(dateStr, FORMATTER);
    }

    public long daysBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) return 0;
        return ChronoUnit.DAYS.between(start, end);
    }

    public boolean isOverdue(LocalDate loanDate, int maxDays) {
        if (loanDate == null) return false;
        return daysBetween(loanDate, LocalDate.now()) > maxDays;
    }

    public LocalDate today() {
        return LocalDate.now();
    }
}