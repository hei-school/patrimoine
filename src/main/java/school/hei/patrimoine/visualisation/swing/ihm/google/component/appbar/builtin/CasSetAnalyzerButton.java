package school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin;

import static java.util.stream.Collectors.toSet;
import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.MessageDialog.showError;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContentManager.hasUnsavedChanges;

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
        new PopupItem(
            "Analyse planifiée uniquement",
            e -> {
              if (checkUnsavedAndConfirm("Analyse planifiée uniquement")) {
                showPlannedOnly();
              }
            }),
        new PopupItem(
            "Analyse recoupée (Planifié + Réalisé)",
            e -> {
              if (checkUnsavedAndConfirm("Analyse recoupée (Planifié + Réalisé)")) {
                showRecouped();
              }
            }));
  }

  private static boolean checkUnsavedAndConfirm(String actionLabel) {
    if (!hasUnsavedChanges()) {
      return true;
    }

    return new UnsavedChangesConfirmDialog(actionLabel).isConfirmed();
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
        .onError(error -> handleError(error, List.of("Tout")))
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
    handleError(error, List.of());
  }

  private static void handleError(Exception error, List<String> excludedNames) {
    error = error.getCause() == null ? error : (Exception) error.getCause();
    if (error instanceof ObjectifExeption exception) {
      var objectifs = exception.getObjectifNonAtteints();

      var objectifsFiltrés =
          objectifs.stream()
              .filter(objectif -> !excludedNames.contains(objectif.objectivable().nom()))
              .collect(toSet());
      if (!objectifsFiltrés.isEmpty()) {
        invokeLater(() -> new ObjectifNonAtteintsDialog(objectifsFiltrés));
      }
      return;
    }
    showError(error);
  }
}
