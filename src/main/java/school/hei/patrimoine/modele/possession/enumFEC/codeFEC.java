package school.hei.patrimoine.modele.possession.enumFEC;

public enum codeFEC {
  CCA_FR("401"),
  CCA_MG("411"),

  PRODUIT("701"),
  CHARGE("601"),
  IMMOBILISATION("215"),

  AUTRE("000");

  private final String code;

  codeFEC(String number) {
    this.code = number;
  }

  public String code() {
    return code;
  }
}
