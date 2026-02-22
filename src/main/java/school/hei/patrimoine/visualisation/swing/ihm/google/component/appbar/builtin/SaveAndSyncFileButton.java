package school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin;

import static school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin.DialogMode.SYNC;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.html.ViewMode.EDIT;
import static school.hei.patrimoine.visualisation.swing.ihm.google.config.EnvironmentConfig.isOfflineMode;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.Api.driveApi;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.MessageDialog.*;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContentManager.getAllModifiedFiles;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangSavingFileManager.save;

import java.util.List;
import java.util.concurrent.Callable;
import javax.swing.*;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.google.exception.GoogleIntegrationException;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.CommentSynchronizer;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.LocalCommentManager;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.html.HtmlViewer;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.html.ViewMode;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.popup.PopupItem;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.popup.PopupMenuButton;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFilesSynchronizer;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFilesWatcher;

@Slf4j
public class SaveAndSyncFileButton extends PopupMenuButton {
  private static PopupItem saveLocalButton(State state, HtmlViewer htmlViewer) {
    return new PopupItem(
        "Tout Sauvegarder localement",
        event -> saveAllModifiedFiles(state.get("viewMode"), htmlViewer));
  }

  private static PopupItem driveSyncButton(Callable<Void> onSuccess) {
    return new PopupItem(
        "Tout Synchroniser avec Drive", e -> syncFilesAndCommentsWithDrive(onSuccess));
  }

  private static List<JMenuItem> getItems(
      State state, HtmlViewer htmlViewer, Callable<Void> onSuccess) {
    if (isOfflineMode()) {
      return List.of(saveLocalButton(state, htmlViewer));
    }
    return List.of(saveLocalButton(state, htmlViewer), driveSyncButton(onSuccess));
  }

  public SaveAndSyncFileButton(State state, HtmlViewer htmlViewer, Callable<Void> onSuccess) {
    super("Sauvegarder / Synchroniser", getItems(state, htmlViewer, onSuccess));
  }

  private static void saveAllModifiedFiles(ViewMode currentMode, HtmlViewer htmlViewer) {
    if (EDIT.equals(currentMode)) {
      htmlViewer.saveLastFileToTempContent();
    }

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
              save(modifiedFilesData);
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

  private static void syncFilesAndCommentsWithDrive(Callable<Void> onSuccess) {
    var confirmDialog = new SyncConfirmDialog(SYNC);
    if (!confirmDialog.isConfirmed()) return;

    AsyncTask.<Void>builder()
        .task(
            () -> {
              PatriLangFilesSynchronizer.sync();
              syncAllPendingComments();
              return null;
            })
        .loadingMessage("Synchronisation avec Drive...")
        .onSuccess(
            ignored -> {
              LocalCommentManager.getInstance().clearAllPendingComments();
              showInfo("Succès", "Les fichiers ont été synchronisés avec succès sur Google Drive.");

              try {
                onSuccess.call();
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            })
        .onError(MessageDialog::showError)
        .build()
        .execute();
  }

  private static void syncAllPendingComments() {
    var localManager = LocalCommentManager.getInstance();
    var filesWithPendingComments = localManager.getPendingFileIds();
    if (filesWithPendingComments.isEmpty()) return;

    var commentSynchronizer = new CommentSynchronizer(driveApi());
    var hasErrors = false;
    for (var fileId : filesWithPendingComments) {
      try {
        commentSynchronizer.syncComments(fileId);
      } catch (GoogleIntegrationException e) {
        hasErrors = true;
      }
    }
    if (hasErrors) {
      throw new RuntimeException("Certains commentaires n'ont pas pu être synchronisés.");
    }
  }
}
