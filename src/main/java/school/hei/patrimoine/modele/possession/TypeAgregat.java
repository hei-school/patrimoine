package school.hei.patrimoine.modele.possession;

public enum TypeAgregat {
  PATRIMOINE,
  TRESORERIE,
  IMMOBILISATION,
  OBLIGATION,
  FLUX,
  CORRECTION,
  ENTREPRISE;

  public boolean valeurMarcheVariable() {
    return this == IMMOBILISATION || this == ENTREPRISE;
  }
}
