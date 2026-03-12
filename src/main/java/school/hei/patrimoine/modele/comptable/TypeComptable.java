package school.hei.patrimoine.modele.comptable;

public enum TypeComptable {
  CCA("486"),
  PCA("487"),
  MATERIEL("2183"),
  BANQUE("512"),
  VIREMENT_INTERNE("580"),
  REMBOURSEMENT_DETTE("164");

  private final String codePCG;

  TypeComptable(String codePCG) {
    this.codePCG = codePCG;
  }

  public String codePCG() {
    return codePCG;
  }
}
