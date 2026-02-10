package school.hei.patrimoine.modele.series;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DateSeries {
  public static List<LocalDate> byDayOfMonth(LocalDate from, LocalDate to, int dayOfMonth) {
    List<LocalDate> result = new ArrayList<>();
    var current = from;

    while (!current.isAfter(to)) {
      int day = Math.min(dayOfMonth, current.lengthOfMonth());
      var candidate = current.withDayOfMonth(day);

      if (!candidate.isBefore(from) && !candidate.isAfter(to)) {
        result.add(candidate);
      }

      current = current.plusMonths(1).withDayOfMonth(1);
    }

    return result;
  }

  public static List<LocalDate> byInterval(LocalDate from, LocalDate to) {
    return from.datesUntil(to.plusDays(1)).toList();
  }

  public static List<LocalDate> byDayOfWeek(LocalDate from, LocalDate to, DayOfWeek dayOfWeek) {
    List<LocalDate> result = new ArrayList<>();

    int daysUntil = (dayOfWeek.getValue() - from.getDayOfWeek().getValue() + 7) % 7;
    var first = from.plusDays(daysUntil);

    for (var date = first; !date.isAfter(to); date = date.plusWeeks(1)) {
      result.add(date);
    }

    return result;
  }
}
