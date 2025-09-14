package school.hei.patrimoine.patrilang.mapper;

import static school.hei.patrimoine.modele.Devise.*;

import school.hei.patrimoine.modele.Devise;

public class DeviseMapper {
  public static String deviseToString(Devise devise) {
    if (MGA.equals(devise)) {
      return "Ar";
    }

    if (EUR.equals(devise)) {
      return "€";
    }

    if (CAD.equals(devise)) {
      return "$";
    }

    return devise.nom();
  }

  public static Devise stringToDevise(String devise) {
    return switch (devise) {
      case "Ar" -> MGA;
      case "€" -> EUR;
      case "$" -> CAD;
      default -> throw new IllegalArgumentException("Devise inconnu: " + devise);
    };
  }
}
