package school.hei.patrimoine.patrilang.modele;

import lombok.Getter;

@Getter
public enum CorrectionQualificateurType {
  POSITIVEMENT(1),
  NEGATIVEMENT(-1);

  private final int facteur;

  CorrectionQualificateurType(int facteur) {
    this.facteur = facteur;
  }

  public static CorrectionQualificateurType fromString(String text) {
    return switch (text) {
      case "positivement" -> POSITIVEMENT;
      case "négativement" -> NEGATIVEMENT;
      default ->
          throw new IllegalArgumentException("Type de correction qualificateur inconnu: " + text);
    };
  }
}
