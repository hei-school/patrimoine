package school.hei.patrimoine.patrilang.modele;

import lombok.Getter;

@Getter
public enum MaterielAppreciationType {
  DEPRECIATION(-1),
  APPRECIATION(1);

  private final int facteur;

  MaterielAppreciationType(int facteur) {
    this.facteur = facteur;
  }

  public static MaterielAppreciationType fromString(String text) {
    return switch (text) {
      case "se dépréciant" -> DEPRECIATION;
      case "s'appréciant" -> APPRECIATION;
      default -> throw new IllegalArgumentException("Type d'appréciation inconnu: " + text);
    };
  }
}
