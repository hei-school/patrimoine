package school.hei.patrimoine.patrilang.mapper;

import static school.hei.patrimoine.modele.Devise.*;

import school.hei.patrimoine.modele.Devise;

public class DeviseMapper {
  public static String deviseToString(Devise devise) {
    if (MGA.equals(devise)) {
      return MGA.symbole();
    }

    if (EUR.equals(devise)) {
      return EUR.symbole();
    }

    if (CAD.equals(devise)) {
      return CAD.symbole();
    }

    return devise.nom();
  }

  public static Devise stringToDevise(String devise) {
    if (MGA.symbole().equals(devise)) {
      return MGA;
    }

    if (EUR.symbole().equals(devise)) {
      return EUR;
    }

    if (CAD.symbole().equals(devise)) {
      return CAD;
    }

    throw new IllegalArgumentException("Devise inconnu: " + devise);
  }
}
