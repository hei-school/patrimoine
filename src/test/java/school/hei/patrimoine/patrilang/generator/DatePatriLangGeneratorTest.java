package school.hei.patrimoine.patrilang.generator;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static java.time.Month.MAY;
import static org.junit.jupiter.api.Assertions.*;

class DatePatriLangGeneratorTest {
    private final DatePatriLangGenerator subject = new DatePatriLangGenerator();

    @Test
    void apply_format_month_number() {
        var date = LocalDate.of(2023, 3, 15);

        var result = subject.apply(date);

        assertEquals("le 15 mars 2023", result);
    }

    @Test
    void apply_date_format_month_string() {
        var date = LocalDate.of(2024, MAY, 1);

        var result = subject.apply(date);

        assertEquals("le 1 mai 2024", result);
    }

    @Test
    void apply_date_bad_format_or_null() {
        var date = LocalDate.of(1999, 12, 31);
        var result = subject.apply(date);
        assertNotEquals("31-12-1999", result);
        assertNotEquals("1999/12/31", result);
        assertThrows(NullPointerException.class, () -> subject.apply(null));
    }
}