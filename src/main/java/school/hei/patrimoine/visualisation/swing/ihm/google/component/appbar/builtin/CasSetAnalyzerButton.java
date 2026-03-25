package school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin;

import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.MessageDialog.showError;

import java.time.LocalDate;
import java.util.List;
import javax.swing.JMenuItem;
import school.hei.patrimoine.cas.CasSet;
import school.hei.patrimoine.cas.CasSetAnalyzer;
import school.hei.patrimoine.modele.objectif.ObjectifExeption;
import school.hei.patrimoine.modele.recouppement.RecoupeurDeCasSet;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.ObjectifNonAtteintsDialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.html.HtmlViewer;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.html.ViewMode;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.popup.PopupItem;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.popup.PopupMenuButton;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.AsyncTask;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFilesWatcher;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.UnsavedChangesChecker;

public class CasSetAnalyzerButton extends PopupMenuButton {
  public CasSetAnalyzerButton(ViewMode currentMode, HtmlViewer htmlViewer) {
    super("Évolution graphique", getItems(currentMode, htmlViewer));
    setToolTipText("Afficher différentes analyses graphiques des cas");
  }

  private static List<JMenuItem> getItems(ViewMode currentMode, HtmlViewer htmlViewer) {
    return List.of(
        new PopupItem(
            "Analyse planifiée uniquement",
            e -> {
              if (checkUnsavedAndConfirm("Analyse planifiée uniquement", currentMode, htmlViewer)) {
                showPlannedOnly();
              }
            }),
        new PopupItem(
            "Analyse recoupée (Planifié + Réalisé)",
            e -> {
              if (checkUnsavedAndConfirm(
                  "Analyse recoupée (Planifié + Réalisé)", currentMode, htmlViewer)) {
                showRecouped();
              }
            }));
  }

  private static boolean checkUnsavedAndConfirm(
      String actionLabel, ViewMode currentMode, HtmlViewer htmlViewer) {
    if (!UnsavedChangesChecker.hasUnsavedChanges(currentMode, htmlViewer)) {
      return true;
    }
    return new UnsavedChangesConfirmDialog(
            actionLabel, UnsavedChangesChecker.getUnsavedFileNames(currentMode, htmlViewer))
        .isConfirmed();
  }

  private static void showRecouped() {
    AsyncTask.<CasSet>builder()
        .logError(false)
        .task(
            () ->
                RecoupeurDeCasSet.of(
                        LocalDate.MIN,
                        LocalDate.MAX,
                        PatriLangFilesWatcher.getPlannedCasSet(),
                        PatriLangFilesWatcher.getDoneCasSet())
                    .getRecouped())
        .onSuccess(result -> new CasSetAnalyzer(DISPOSE_ON_CLOSE).accept(result))
        .onError(CasSetAnalyzerButton::handleError)
        .build()
        .execute();
  }

  private static void showPlannedOnly() {
    AsyncTask.<CasSet>builder()
        .logError(false)
        .task(PatriLangFilesWatcher::getPlannedCasSet)
        .onSuccess(result -> new CasSetAnalyzer(DISPOSE_ON_CLOSE).accept(result))
        .onError(CasSetAnalyzerButton::handleError)
        .build()
        .execute();
  }

  private static void handleError(Exception error) {
    error = error.getCause() == null ? error : (Exception) error.getCause();
    if (error instanceof ObjectifExeption exception) {
      var objectifs = exception.getObjectifNonAtteints();
      invokeLater(() -> new ObjectifNonAtteintsDialog(objectifs));
      return;
    }
    showError(error);
  }
}
