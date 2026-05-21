package school.hei.patrimoine.visualisation.swing.ihm.google.pages.filters;

public enum PossessionRecoupeeFilterStatus {
  TOUT("Tout"),
  IMPREVU("Imprévu"),
  NON_EXECUTE("Non Éxecuté"),
  EXECUTE_AVEC_CORRECTION("Éxecuté avec correction"),
  EXECUTE_SANS_CORRECTION("Éxecuté sans correction");

  public final String label;

  PossessionRecoupeeFilterStatus(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return label;
  }
}
