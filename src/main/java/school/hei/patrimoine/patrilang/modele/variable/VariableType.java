package school.hei.patrimoine.patrilang.modele.variable;

import lombok.Getter;

@Getter
public enum VariableType {
  DATE("Dates"),
  PERSONNE("Personnes"),
  TRESORERIES("Trésoreries"),
  CREANCE("Créances"),
  DETTE("Dettes");

  private final String value;

  VariableType(String value) {
    this.value = value;
  }

  public static VariableType fromString(String value) {
    return switch (value) {
      case "Dates" -> DATE;
      case "Personnes" -> PERSONNE;
      case "Trésoreries" -> TRESORERIES;
      case "Créances" -> CREANCE;
      case "Dettes" -> DETTE;
      default -> throw new IllegalArgumentException("Type de variable inconnu " + value);
    };
  }
}
