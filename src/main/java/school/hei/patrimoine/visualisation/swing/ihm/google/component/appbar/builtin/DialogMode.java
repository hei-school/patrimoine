package school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin;

public enum DialogMode {
  SYNC(
      "Confirmer la synchronisation",
      "Voulez-vous synchroniser ces modifications avec Google Drive ?",
      "Synchroniser"),
  EXIT(
      "Modifications non synchronisées",
      "Il reste des modifications non synchronisées. Voulez-vous vraiment quitter ?",
      "Quitter");

  public final String title;
  public final String message;
  public final String confirmButtonText;

  DialogMode(String title, String message, String confirmButtonText) {
    this.title = title;
    this.message = message;
    this.confirmButtonText = confirmButtonText;
  }
}
