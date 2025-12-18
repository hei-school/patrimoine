package school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin;

import static school.hei.patrimoine.patrilang.PatriLangTranspiler.CAS_FILE_EXTENSION;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.TOUT_CAS_FILE_EXTENSION;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.Api.driveApi;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.MessageDialog.*;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.PatriLangStagingFileManager.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Supplier;
import lombok.SneakyThrows;
import school.hei.patrimoine.google.api.CommentApi;
import school.hei.patrimoine.google.exception.GoogleIntegrationException;
import school.hei.patrimoine.patrilang.files.PatriLangFileContext;
import school.hei.patrimoine.patrilang.files.PatriLangFileWritter;
import school.hei.patrimoine.patrilang.files.PatriLangFileWritter.FileWritterInput;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.HtmlViewer;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.AppBar;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.LocalCommentManager;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.popup.PopupItem;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.popup.PopupMenuButton;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.AsyncTask;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.ConfirmDialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkList;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;

public class SaveAndSyncFileButton extends PopupMenuButton {
  private static final String MIME_TYPE = "application/octet-stream";

  public SaveAndSyncFileButton(
      State state,
      HtmlViewer htmlViewer,
      Supplier<String> getNewContent,
      Callable<Void> onSuccess) {
    super(
        "Sauvegarder / Synchroniser",
        List.of(
            new PopupItem(
                "Sauvegarder localement",
                e ->
                    saveAllModifiedFiles(
                        state.get("viewMode"), htmlViewer, state.get("selectedFile"))),
            new PopupItem(
                "Synchroniser avec Drive",
                e -> syncFilesWithDrive(getStagedPlannedFiles(), getStagedDoneFiles(), onSuccess)),
            new PopupItem(
                "Sauvegarder et synchroniser",
                e -> {
                  saveAndSyncSelectedFile(
                      state.get("viewMode"),
                      state.get("selectedFile"),
                      state.get("selectedCasSetFile"),
                      state.get("isPlannedSelectedFile"),
                      getNewContent.get(),
                      onSuccess);
                })));
  }

  @SneakyThrows
  private static void syncFiles(List<File> plannedFiles, List<File> doneFiles) {
    for (File file : plannedFiles) {
      getDriveIdOf(file, true)
          .ifPresentOrElse(
              id -> {
                try {
                  driveApi().update(id, MIME_TYPE, file);
                } catch (GoogleIntegrationException e) {
                  throw new RuntimeException(e);
                }
              },
              () -> System.out.println("Aucun ID Drive trouvé pour planned : " + file.getName()));
    }

    for (File file : doneFiles) {
      getDriveIdOf(file, false)
          .ifPresentOrElse(
              id -> {
                try {
                  driveApi().update(id, MIME_TYPE, file);
                } catch (GoogleIntegrationException e) {
                  throw new RuntimeException(e);
                }
              },
              () -> System.out.println("Aucun ID Drive trouvé pour done : " + file.getName()));
    }
  }

  private static boolean validateBeforeSave(AppBar.ViewMode currentMode, File selectedFile) {
    if (selectedFile == null) {
      showError("Erreur", "Veuillez sélectionner un fichier avant de sauvegarder.");
      return true;
    }

    if (!AppBar.ViewMode.EDIT.equals(currentMode)) {
      showError("Erreur", "Vous devez être en mode édition pour sauvegarder.");
      return true;
    }

    return false;
  }

  private static void saveAllModifiedFiles(
      AppBar.ViewMode currentMode, HtmlViewer htmlViewer, File selectedFile) {

    if (validateBeforeSave(currentMode, selectedFile)) return;

    htmlViewer.saveCurrentContentToMemory();

    Map<File, FileWritterInput> modifiedFilesData = htmlViewer.getModifiedFilesData();

    if (modifiedFilesData.isEmpty()) {
      showInfo("Information", "Aucune modification à sauvegarder.");
      return;
    }

    AsyncTask.<Void>builder()
        .loadingMessage("Validation et sauvegarde des modifications...")
        .task(
            () -> {
              for (FileWritterInput input : modifiedFilesData.values()) {

                new PatriLangFileWritter().write(input);

                var file = new PatriLangFileContext(input);
                saveToStaged(input.file(), file.isPlanned());
              }
              return null;
            })
        .onSuccess(
            result -> {
              AppContext.getDefault().globalState().update("isAnyFileModified", true);

              for (File file : modifiedFilesData.keySet()) {
                if (file != null && file.exists()) {
                  try {
                    String savedContent = Files.readString(file.toPath());
                    htmlViewer.getOriginalContents().put(file, savedContent);
                  } catch (IOException e) {
                    throw new RuntimeException(e);
                  }
                }
              }

              htmlViewer.clearModifiedFiles();
              showInfo("Succès", "Vous pouvez maintenant synchroniser avec Google Drive.");
            })
        .onError(
            error -> {
              if (showExceptionMessageIfRecognizedException(error)) return;
              showError("Erreur", "Une erreur est survenue lors de l'enregistrement");
            })
        .build()
        .execute();
  }

  private static void syncFilesWithDrive(
      List<File> plannedFiles, List<File> doneFiles, Callable<Void> onSuccess) {
    boolean confirmed = SyncConfirmDialog.forSync();
    if (!confirmed) return;

    AsyncTask.<Void>builder()
        .task(
            () -> {
              syncFiles(plannedFiles, doneFiles);
              syncAllPendingComments();
              return null;
            })
        .loadingMessage("Synchronisation avec Drive...")
        .onSuccess(
            ignored -> {
              clearAllStaged();
              LocalCommentManager.getInstance().clearAllPendingComments();
              showInfo("Succès", "Le fichier a été synchronisé avec succès sur Google Drive.");
              try {
                onSuccess.call();
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            })
        .onError(error -> showError("Erreur", "Échec de la synchronisation"))
        .build()
        .execute();
  }

  private static void saveAndSyncSelectedFile(
      AppBar.ViewMode currentMode,
      File selectedFile,
      File selectedCasSetFile,
      Boolean isPlanned,
      String content,
      Callable<Void> onSuccess) {

    if (validateBeforeSave(currentMode, selectedFile)) return;

    var saveConfirmed =
        ConfirmDialog.ask(
            "Confirmer la sauvegarde",
            "Sauvegarder les modifications localement avant la synchronisation.\n"
                + "Voulez-vous continuer ?");
    if (!saveConfirmed) return;

    try {
      new PatriLangFileWritter()
          .write(
              FileWritterInput.builder()
                  .casSet(selectedCasSetFile)
                  .file(selectedFile)
                  .content(content)
                  .build());
      saveToStaged(selectedFile, isPlanned != null ? isPlanned : false);
    } catch (Exception e) {
      if (showExceptionMessageIfRecognizedException(e)) return;
      showError("Erreur", "Une erreur est survenue lors de l'enregistrement");
      return;
    }

    var syncConfirmed = SyncConfirmDialog.forSync();
    if (!syncConfirmed) {
      showInfo(
          "Sauvegarde effectuée",
          "Le fichier a été sauvegardé localement. Vous pouvez le synchroniser plus tard.");
      return;
    }

    var confirmed =
        ConfirmDialog.ask(
            "Confirmer la sauvegarde et la synchronisation",
            "Sauvegarder les modifications localement et les synchroniser avec Google"
                + " Drive.\n"
                + "Voulez-vous continuer ?");
    if (!confirmed) return;

    AsyncTask.<Void>builder()
        .loadingMessage("Synchronisation avec Drive...")
        .task(
            () -> {
              List<File> stagedPlanned = getStagedPlannedFiles();
              List<File> stagedDone = getStagedDoneFiles();
              syncFiles(stagedPlanned, stagedDone);
              syncAllPendingComments();
              return null;
            })
        .onSuccess(
            result -> {
              AppContext.getDefault().globalState().update("isAnyFileModified", true);
              clearAllStaged();
              showInfo(
                  "Succès",
                  "Le fichier a été sauvegardé localement et synchronisé avec succès sur"
                      + " Google Drive.");
              try {
                onSuccess.call();
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            })
        .onError(
            error -> {
              if (showExceptionMessageIfRecognizedException(error)) return;
              showError("Erreur", "Une erreur est survenue lors de l'enregistrement");
            })
        .build()
        .execute();
  }

  private static void syncAllPendingComments() {
    CommentApi commentApi = AppContext.getDefault().getData("comment-api");
    LocalCommentManager localManager = LocalCommentManager.getInstance();

    List<String> filesWithPendingComments = localManager.getFilesWithPendingChanges();

    if (filesWithPendingComments.isEmpty()) return;

    boolean hasErrors = false;

    for (String fileId : filesWithPendingComments) {
      try {
        commentApi.syncComments(fileId);
      } catch (GoogleIntegrationException e) {
        hasErrors = true;
      }
    }
    if (hasErrors) {
      throw new RuntimeException("Some comments could not be synchronized.");
    }
  }

  private static Optional<String> getDriveIdOf(File file, boolean isPlanned) {
    GoogleLinkList<GoogleLinkList.NamedID> ids = AppContext.getDefault().getData("named-ids");
    if (file == null) return Optional.empty();

    String filename =
        file.getName().replace(TOUT_CAS_FILE_EXTENSION, "").replace(CAS_FILE_EXTENSION, "");

    var listToSearch = isPlanned ? ids.planned() : ids.done();

    return listToSearch.stream()
        .filter(n -> n.name().equals(filename))
        .map(GoogleLinkList.NamedID::id)
        .findFirst();
  }
}
