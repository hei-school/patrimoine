package school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin;

import static school.hei.patrimoine.patrilang.PatriLangTranspiler.*;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin.DialogMode.SYNC;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.html.ViewMode.EDIT;
import static school.hei.patrimoine.visualisation.swing.ihm.google.config.EnvironmentConfig.isOfflineMode;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.Api.driveApi;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.MessageDialog.*;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.FileCategory.*;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContentManager.getAllModifiedFiles;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangSavingFileManager.save;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangStagingFileManager.*;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import javax.swing.*;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import school.hei.patrimoine.google.exception.GoogleIntegrationException;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.LocalCommentManager;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.html.HtmlViewer;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.html.ViewMode;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.popup.PopupItem;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.popup.PopupMenuButton;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.AsyncTask;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkList;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkList.NamedID;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.MessageDialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.CommentSynchronizer;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.FileCategory;

@Slf4j
public class SaveAndSyncFileButton extends PopupMenuButton {
  private static final String MIME_TYPE = "application/octet-stream";

  private static PopupItem saveLocalButton(State state, HtmlViewer htmlViewer) {
    return new PopupItem(
        "Tout Sauvegarder localement",
        event -> saveAllModifiedFiles(state.get("viewMode"), htmlViewer));
  }

  private static PopupItem driveSyncButton(Callable<Void> onSuccess) {
    return new PopupItem("Tout Synchroniser avec Drive", e -> syncFilesWithDrive(onSuccess));
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

  private static void sync(List<File> files, FileCategory category) {
    for (var file : files) {
      var id = getDriveIdOf(file, category);
      if (id.isEmpty()) {
        throw new RuntimeException("Aucun ID Drive trouvé pour : " + file.getName());
      }

      try {
        driveApi().update(id.get(), MIME_TYPE, file);
      } catch (GoogleIntegrationException e) {
        throw new RuntimeException(e);
      }
    }
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
              AppContext.getDefault().globalState().update("isAnyFileModified", true);
              showInfo("Succès", "Vous pouvez maintenant synchroniser avec Google Drive.");
            })
        .onError(MessageDialog::showError)
        .build()
        .execute();
  }

  private static void syncFilesWithDrive(Callable<Void> onSuccess) {
    var confirmDialog = new SyncConfirmDialog(SYNC);
    if (!confirmDialog.isConfirmed()) return;

    AsyncTask.<Void>builder()
        .task(
            () -> {
              sync(getStagedDoneFiles(), DONE);
              sync(getStagedPlannedFiles(), PLANNED);
              sync(getStagedJustificativeFiles(), JUSTIFICATIVE);
              CommentSynchronizer.getInstance().sync();
              return null;
            })
        .loadingMessage("Synchronisation avec Drive...")
        .onSuccess(
            ignored -> {
              clearAllStagedFiles();
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

  private static Optional<String> getDriveIdOf(File file, FileCategory category) {
    GoogleLinkList<NamedID> ids = AppContext.getDefault().getData("named-ids");
    if (file == null) return Optional.empty();

    var filename = getFileNameWithoutExtension(file);

    var listToSearch =
        switch (category) {
          case JUSTIFICATIVE -> ids.justificative();
          case PLANNED -> ids.planned();
          case DONE -> ids.done();
        };

    return listToSearch.stream()
        .filter(n -> n.name().equals(filename))
        .map(NamedID::id)
        .findFirst();
  }

  private static @NonNull String getFileNameWithoutExtension(File file) {
    return file.getName()
        .replace(TOUT_CAS_FILE_EXTENSION, "")
        .replace(CAS_FILE_EXTENSION, "")
        .replace(PJ_FILE_EXTENSION, "");
  }
}
