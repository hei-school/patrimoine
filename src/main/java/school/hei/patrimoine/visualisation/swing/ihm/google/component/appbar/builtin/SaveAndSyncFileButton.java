package school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin;

import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.Api.driveApi;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.MessageDialog.*;

import java.io.File;
import java.util.List;
import java.util.function.Supplier;
import school.hei.patrimoine.patrilang.files.PatriLangFileWritter;
import school.hei.patrimoine.patrilang.files.PatriLangFileWritter.FileWritterInput;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.AppBar;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.popup.PopupItem;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.popup.PopupMenuButton;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.AsyncTask;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.ConfirmDialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;

public class SaveAndSyncFileButton extends PopupMenuButton {
  private static final String MIME_TYPE = "application/octet-stream";

  public SaveAndSyncFileButton(State state, Supplier<String> getNewContent) {
    super(
        "Sauvegarder / Synchroniser",
        List.of(
            new PopupItem(
                "Sauvegarder localement",
                e ->
                    saveSelectedFileLocally(
                        state.get("viewMode"),
                        state.get("selectedFile"),
                        state.get("selectedCasSetFile"),
                        getNewContent.get())),
            new PopupItem(
                "Synchroniser avec Drive",
                e ->
                    syncSelectedFileWithDrive(
                        state.get("selectedFile"), state.get("selectedFileId")))));
  }

  private static void saveSelectedFileLocally(
      AppBar.ViewMode currentMode, File selectedFile, File selectedCasSetFile, String content) {
    if (!AppBar.ViewMode.EDIT.equals(currentMode)) {
      showError("Erreur", "Vous devez être en mode édition pour sauvegarder.");
      return;
    }

    if (selectedFile == null) {
      showError("Erreur", "Veuillez sélectionner un fichier avant de sauvegarder.");
      return;
    }

    AsyncTask.<Void>builder()
        .loadingMessage("Validation et sauvegarde du fichier...")
        .task(
            () -> {
              new PatriLangFileWritter()
                  .write(
                      FileWritterInput.builder()
                          .casSet(selectedCasSetFile)
                          .file(selectedFile)
                          .content(content)
                          .build());
              return null;
            })
        .onSuccess(
            result -> {
              AppContext.getDefault().globalState().update("isAnyFileModified", true);
              showInfo("Succès", "Vous pouvez maintenant le synchroniser avec Google Drive.");
            })
        .onError(
            error -> {
              if (showExceptionMessageIfRecognizedException(error)) {
                return;
              }
              showError("Erreur", "Une erreur est survenue lors de l'enregistrement");
            })
        .build()
        .execute();
  }

  private static void syncSelectedFileWithDrive(File selectedFile, String selectedFileId) {
    if (selectedFile == null) {
      showError("Erreur", "Veuillez sélectionner un fichier avant de synchroniser avec Drive.");
      return;
    }

    var confirmed =
        ConfirmDialog.ask(
            "Vérification de sauvegarde",
            "Vous devez d'abord sauvegarder votre fichier localement avant de le synchroniser avec"
                + " Drive.\n"
                + "Avez-vous sauvegardé les modifications ?");

    if (!confirmed) {
      return;
    }

    AsyncTask.<Void>builder()
        .task(
            () -> {
              driveApi().update(selectedFileId, MIME_TYPE, selectedFile);
              return null;
            })
        .loadingMessage("Synchronisation avec Drive...")
        .onSuccess(
            ignored ->
                showInfo("Succès", "Le fichier a été synchronisé avec succès sur Google Drive."))
        .onError(error -> showError("Erreur", "Échec de la synchronisation"))
        .build()
        .execute();
  }
}
