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
import school.hei.patrimoine.visualisation.swing.ihm.google.component.popup.PopupItem;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.popup.PopupMenuButton;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.AsyncTask;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFilesWatcher;

public class CasSetAnalyzerButton extends PopupMenuButton {
  public CasSetAnalyzerButton() {
    super("Évolution graphique", getItems());
    setToolTipText("Afficher différentes analyses graphiques des cas");
  }

  private static List<JMenuItem> getItems() {
    return List.of(
        new PopupItem("Analyse planifiée uniquement", e -> showPlannedOnly()),
        new PopupItem("Analyse recoupée (Planifié + Réalisé)", e -> showRecouped()));
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
