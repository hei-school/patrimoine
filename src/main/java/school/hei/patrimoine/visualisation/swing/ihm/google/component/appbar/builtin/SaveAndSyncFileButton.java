package school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin;

import static school.hei.patrimoine.visualisation.swing.ihm.google.config.EnvironmentConfig.isOfflineMode;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.MessageDialog.*;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContentManager.getAllModifiedFiles;

import java.util.List;
import javax.swing.*;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.html.HtmlViewer;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.html.ViewMode;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.popup.PopupItem;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.popup.PopupMenuButton;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.PendingCommentManager;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.PendingCommentSynchronizer;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFilesSynchronizer;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFilesWatcher;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangSavingFileManager;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangStagingFileManager;

@Slf4j
public class SaveAndSyncFileButton extends PopupMenuButton {
  private static PopupItem saveLocalButton(State state, HtmlViewer htmlViewer) {
    return new PopupItem(
        "Tout Sauvegarder localement",
        event -> saveAllModifiedFiles(state.get("viewMode"), htmlViewer));
  }

  private static PopupItem driveSyncButton(Runnable onSuccess) {
    return new PopupItem(
        "Tout Synchroniser avec Drive", e -> syncFilesAndCommentsWithDrive(onSuccess));
  }

  private static List<JMenuItem> getItems(State state, HtmlViewer htmlViewer, Runnable onSuccess) {
    if (isOfflineMode()) {
      return List.of(saveLocalButton(state, htmlViewer));
    }
    return List.of(saveLocalButton(state, htmlViewer), driveSyncButton(onSuccess));
  }

  public SaveAndSyncFileButton(State state, HtmlViewer htmlViewer, Runnable onSuccess) {
    super("Sauvegarder / Synchroniser", getItems(state, htmlViewer, onSuccess));
  }

  private static void saveAllModifiedFiles(ViewMode currentMode, HtmlViewer htmlViewer) {
    var modifiedFilesData = getAllModifiedFiles();
    if (modifiedFilesData.isEmpty()) {
      showInfo("Information", "Aucune modification à sauvegarder.");
      return;
    }

    AsyncTask.<Void>builder()
        .loadingMessage("Validation et sauvegarde des modifications...")
        .logError(false)
        .task(
            () -> {
              PatriLangSavingFileManager.save(modifiedFilesData);
              return null;
            })
        .onSuccess(
            result -> {
              PatriLangFilesWatcher.dispatch();
              showInfo("Succès", "Vous pouvez maintenant synchroniser avec Google Drive.");
            })
        .onError(MessageDialog::showError)
        .build()
        .execute();
  }

  private static void syncFilesAndCommentsWithDrive(Runnable onSuccess) {
    var stagingFiles = PatriLangStagingFileManager.getFiles();
    var pendingComments = PendingCommentManager.getPendings();

    if (stagingFiles.isEmpty() && pendingComments.isEmpty()) {
      showInfo("Erreur", "Aucune modification en attente de synchronisation.");
      return;
    }

    var confirmDialog = new SyncConfirmDialog();
    if (!confirmDialog.isConfirmed()) {
      return;
    }

    AsyncTask.<Void>builder()
        .task(
            () -> {
              PatriLangFilesSynchronizer.sync();
              PendingCommentSynchronizer.sync();
              return null;
            })
        .loadingMessage("Synchronisation avec Drive...")
        .onSuccess(
            ignored -> {
              showInfo(
                  "Succès",
                  "Les fichiers et Commentaires ont été synchronisés avec succès sur Google"
                      + " Drive.");

              try {
                onSuccess.run();
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            })
        .onError(MessageDialog::showError)
        .build()
        .execute();
  }
}
