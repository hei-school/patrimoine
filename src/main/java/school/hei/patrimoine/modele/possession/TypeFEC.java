package school.hei.patrimoine.modele.possession;

public enum TypeFEC {
  CCA("CCA"),
  PRODUIT("PRD"),
  IMMOBILISATION("IMMO"),
  CHARGE("CHG"),
  AUTRE("AUTRE");

  private final String abrev;

  TypeFEC(String abrev) {
    this.abrev = abrev;
  }

  public String abrev() {
    return abrev;
  }
}
