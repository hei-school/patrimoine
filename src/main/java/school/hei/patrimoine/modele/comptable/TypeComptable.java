package school.hei.patrimoine.modele.comptable;

public enum TypeComptable {
  CCA(486),
  PCA(487),
  DETTE(164),
  CREANCE(267),
  BANQUE(512),
  CAPITAL(101),
  MATERIEL(2183),
  VIREMENT_INTERNE(580),
  CHARGE_DIVERSE(658),
  PRODUIT_DIVERS(758);

  private final int codePCG;

  TypeComptable(int codePCG) {
    this.codePCG = codePCG;
  }

  public int codePCG() {
    return codePCG;
  }
}
