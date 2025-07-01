package school.hei.patrimoine.patrilang.mapper;

import static school.hei.patrimoine.patrilang.modele.DurationType.*;

import school.hei.patrimoine.patrilang.modele.DurationType;

public class DurationMapper {
  public static DurationType stringToDurationType(String token) {
    return switch (token) {
      case "en années", "en Années", "années de", "année de", "Année de", "Années de" -> YEARS;
      case "en mois", "en Mois", "mois de", "Mois de" -> MONTH;
      case "en jours", "en Jours", "jours de", "Jour de", "jour de", "Jours de" -> DAYS;
      default -> throw new IllegalArgumentException("Type de durée inconnu : " + token);
    };
  }
}
