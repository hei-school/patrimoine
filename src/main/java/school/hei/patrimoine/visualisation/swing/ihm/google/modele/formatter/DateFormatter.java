package school.hei.patrimoine.visualisation.swing.ihm.google.modele.formatter;

import java.time.LocalDate;

public class DateFormatter {
  public static String format(LocalDate date) {
    if (LocalDate.MIN.equals(date)) {
      return "Date indéterminée (début)";
    }

    if (LocalDate.MAX.equals(date)) {
      return "Date indéterminée (fin)";
    }

    return date.toString();
  }
}
