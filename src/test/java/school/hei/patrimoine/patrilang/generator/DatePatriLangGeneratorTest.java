package school.hei.patrimoine.patrilang.generator;

import static java.time.Month.MAY;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class DatePatriLangGeneratorTest {
  private static final DatePatriLangGenerator subject = new DatePatriLangGenerator();

  @Test
  void apply_date_right_format() {
    var date = LocalDate.of(2024, MAY, 1);
    var actual = subject.apply(date);

    assertEquals("le 1 mai 2024", actual);
  }

  @Test
  void apply_date_bad_format() {
    var date = LocalDate.of(1999, 12, 31);
    var actual = subject.apply(date);

    assertNotEquals("31-12-1999", actual);
    assertNotEquals("1999/12/31", actual);
  }
}
