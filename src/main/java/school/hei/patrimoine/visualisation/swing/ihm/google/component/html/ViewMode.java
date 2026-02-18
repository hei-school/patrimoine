package school.hei.patrimoine.visualisation.swing.ihm.google.component.html;

import lombok.Getter;

@Getter
public enum ViewMode {
  VIEW("Affichage"),
  EDIT("Édition");

  private final String label;

  ViewMode(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return label;
  }
}
