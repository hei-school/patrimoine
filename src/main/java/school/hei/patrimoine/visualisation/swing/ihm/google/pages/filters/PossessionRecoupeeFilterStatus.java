package school.hei.patrimoine.visualisation.swing.ihm.google.pages.filters;

public enum PossessionRecoupeeFilterStatus {
  TOUS("Tous"),
  IMPREVU("Imprévu"),
  NON_EXECUTE("Non Executé"),
  EXECUTE_AVEC_CORRECTION("Executé avec correction"),
  EXECUTE_SANS_CORRECTION("Executé sans correction");

  public final String label;

  PossessionRecoupeeFilterStatus(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return label;
  }
}
