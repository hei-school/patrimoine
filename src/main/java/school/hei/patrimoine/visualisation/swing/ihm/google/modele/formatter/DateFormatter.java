package school.hei.patrimoine.visualisation.swing.ihm.google.modele.formatter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateFormatter {
  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault());

  public static String format(LocalDate date) {
    if (LocalDate.MIN.equals(date)) {
      return "Date indéterminée (début)";
    }

    if (LocalDate.MAX.equals(date)) {
      return "Date indéterminée (fin)";
    }

    return date.toString();
  }

  public static String format(Instant format) {
    return DATE_TIME_FORMATTER.format(format);
  }
}
