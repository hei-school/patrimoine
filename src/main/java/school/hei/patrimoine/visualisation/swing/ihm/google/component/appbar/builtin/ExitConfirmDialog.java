package school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin;

public class ExitConfirmDialog extends ConfirmDialog {
  public ExitConfirmDialog() {
    super(
        "Modifications non synchronisées",
        "Il reste des modifications non synchronisées. Voulez-vous vraiment quitter ?",
        "Quitter");
  }
}
