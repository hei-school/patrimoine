package school.hei.patrimoine.patrilang.modele.variable;

import lombok.Getter;

@Getter
public enum VariableType {
  OPERATION_TEMPLATE("Templates"),
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
      case "Templates" -> OPERATION_TEMPLATE;
      case "Dates" -> DATE;
      case "Personnes" -> PERSONNE;
      case "Trésoreries" -> TRESORERIES;
      case "Créances" -> CREANCE;
      case "Dettes" -> DETTE;
      default -> throw new IllegalArgumentException("Type de variable inconnu " + value);
    };
  }
}
