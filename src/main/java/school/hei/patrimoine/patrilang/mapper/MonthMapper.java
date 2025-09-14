package school.hei.patrimoine.patrilang.mapper;

import static java.time.Month.*;

import java.time.Month;

public class MonthMapper {
  public static Month stringToMonth(String token) {
    return switch (token) {
      case "Janvier", "janvier" -> JANUARY;
      case "Février", "février", "Fevrier", "fevrier" -> FEBRUARY;
      case "Mars", "mars" -> MARCH;
      case "Avril", "avril" -> APRIL;
      case "Mai", "mai" -> MAY;
      case "Juin", "juin" -> JUNE;
      case "Juillet", "juillet" -> JULY;
      case "Août", "août", "Aout", "aout" -> AUGUST;
      case "Septembre", "septembre" -> SEPTEMBER;
      case "Octobre", "octobre" -> OCTOBER;
      case "Novembre", "novembre" -> NOVEMBER;
      case "Décembre", "décembre", "Decembre", "decembre" -> DECEMBER;
      default -> throw new IllegalArgumentException("Mois invalide : " + token);
    };
  }

  public static String monthToString(Month month) {
    return switch (month) {
      case JANUARY -> "janvier";
      case FEBRUARY -> "février";
      case MARCH -> "mars";
      case APRIL -> "avril";
      case MAY -> "mai";
      case JUNE -> "juin";
      case JULY -> "juillet";
      case AUGUST -> "août";
      case SEPTEMBER -> "septembre";
      case OCTOBER -> "octobre";
      case NOVEMBER -> "novembre";
      case DECEMBER -> "décembre";
    };
  }
}
