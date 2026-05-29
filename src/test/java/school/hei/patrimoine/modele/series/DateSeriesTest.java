package school.hei.patrimoine.modele.series;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.THURSDAY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.series.DateSeries.byDayOfMonth;
import static school.hei.patrimoine.modele.series.DateSeries.byDayOfWeek;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class DateSeriesTest {

  @Test
  void byDayOfMonth_should_include_first_month_from_date() {
    var from = LocalDate.of(2024, 1, 14);
    var to = LocalDate.of(2024, 4, 30);

    var actual = byDayOfMonth(from, to, 14);

    assertEquals(
        List.of(
            LocalDate.of(2024, 1, 14),
            LocalDate.of(2024, 2, 14),
            LocalDate.of(2024, 3, 14),
            LocalDate.of(2024, 4, 14)),
        actual);
  }

  @Test
  void byDayOfMonth_from_and_to_empty() {
    var from = LocalDate.of(2024, 1, 14);
    var to = LocalDate.of(2024, 1, 30);

    var actual = byDayOfMonth(from, to, 4);

    assertEquals(List.of(), actual);
  }

  @Test
  void byDayOfMonth_from_and_to_with_first_month_as_value() {
    var from = LocalDate.of(2024, 1, 14);
    var to = LocalDate.of(2024, 2, 5);

    var actual = byDayOfMonth(from, to, 1);

    assertEquals(List.of(LocalDate.of(2024, 2, 1)), actual);
  }

  @Test
  void byDayOfMonth_from_a_value_to_another() {
    var from = LocalDate.of(2024, 1, 14);
    var to = LocalDate.of(2024, 2, 5);

    var actual = byDayOfMonth(from, to, 1);

    assertEquals(List.of(LocalDate.of(2024, 2, 1)), actual);
  }

  @Test
  void use_end_of_month_as_date() {
    var from = LocalDate.of(2024, 1, 14);
    var to = LocalDate.of(2025, 3, 31);

    var actual = byDayOfMonth(from, to, 31);

    var expected =
        List.of(
            LocalDate.of(2024, 1, 31),
            LocalDate.of(2024, 2, 29),
            LocalDate.of(2024, 3, 31),
            LocalDate.of(2024, 4, 30),
            LocalDate.of(2024, 5, 31),
            LocalDate.of(2024, 6, 30),
            LocalDate.of(2024, 7, 31),
            LocalDate.of(2024, 8, 31),
            LocalDate.of(2024, 9, 30),
            LocalDate.of(2024, 10, 31),
            LocalDate.of(2024, 11, 30),
            LocalDate.of(2024, 12, 31),
            LocalDate.of(2025, 1, 31),
            LocalDate.of(2025, 2, 28),
            LocalDate.of(2025, 3, 31));
    assertEquals(expected, actual);
  }

  @Test
  void byDayOfWeek_same_day() {
    var from = LocalDate.of(2024, 2, 5); // Monday
    var to = LocalDate.of(2024, 2, 5);

    var actual = byDayOfWeek(from, to, MONDAY);

    assertEquals(List.of(LocalDate.of(2024, 2, 5)), actual);
  }

  @Test
  void byDayOfWeek_multiple_weeks() {
    var from = LocalDate.of(2024, 2, 1); // THURSDAY
    var to = LocalDate.of(2024, 2, 29);

    var actual = byDayOfWeek(from, to, MONDAY);

    var expected =
        List.of(
            LocalDate.of(2024, 2, 5),
            LocalDate.of(2024, 2, 12),
            LocalDate.of(2024, 2, 19),
            LocalDate.of(2024, 2, 26));
    assertEquals(expected, actual);
  }

  @Test
  void byDayOfWeek_from_on_target_day() {
    var from = LocalDate.of(2024, 2, 5); // MONDAY
    var to = LocalDate.of(2024, 2, 20);

    var actual = byDayOfWeek(from, to, MONDAY);

    var expected =
        List.of(LocalDate.of(2024, 2, 5), LocalDate.of(2024, 2, 12), LocalDate.of(2024, 2, 19));
    assertEquals(expected, actual);
  }

  @Test
  void byDayOfWeek_from_after_target_day() {
    var from = LocalDate.of(2024, 2, 6);
    var to = LocalDate.of(2024, 2, 15);

    var actual = byDayOfWeek(from, to, MONDAY);

    var expected = List.of(LocalDate.of(2024, 2, 12));
    assertEquals(expected, actual);
  }

  @Test
  void byDayOfWeek_full_week() {
    var from = LocalDate.of(2024, 2, 1);
    var to = LocalDate.of(2024, 2, 7);

    var actual = byDayOfWeek(from, to, THURSDAY);

    var expected = List.of(LocalDate.of(2024, 2, 1));
    assertEquals(expected, actual);
  }
}
