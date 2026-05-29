package school.hei.patrimoine.visualisation.swing.ihm.google.pages.filters;

public enum PossessionRecoupeeFilterPj {
  TOUS("Tous"),
  AVEC_PJ("Justifié"),
  SANS_PJ("Non justifié");

  public final String label;

  PossessionRecoupeeFilterPj(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return label;
  }
}
