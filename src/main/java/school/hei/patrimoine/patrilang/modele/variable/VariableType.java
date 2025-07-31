package school.hei.patrimoine.patrilang.modele.variable;

import lombok.Getter;

@Getter
public enum VariableType {
  CAS("Cas"),
  OPERATION_TEMPLATE("Templates"),
  DATE("Dates"),
  NOMBRE("Nombres"),
  ARGENT("Argents"),
  PERSONNE("Personnes"),
  TRESORERIES("Trésoreries"),
  CREANCE("Créances"),
  DETTE("Dettes"),
  PERSONNE_MORALE("PersonnesMorales"),
  MATERIEL("Matériel");

  private final String value;

  VariableType(String value) {
    this.value = value;
  }

  public static VariableType fromString(String value) {
    return switch (value) {
      case "Templates" -> OPERATION_TEMPLATE;
      case "Dates" -> DATE;
      case "Nombres" -> NOMBRE;
      case "Argents" -> ARGENT;
      case "Personnes" -> PERSONNE;
      case "Cas" -> CAS;
      case "Trésoreries" -> TRESORERIES;
      case "Créances" -> CREANCE;
      case "Dettes" -> DETTE;
      case "PersonnesMorales" -> PERSONNE_MORALE;
      case "Matériel" -> MATERIEL;
      default -> throw new IllegalArgumentException("Type de variable inconnu " + value);
    };
  }
}
