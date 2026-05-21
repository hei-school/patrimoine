package school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin;

import java.util.List;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.comment.PendingCommentManager;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangStagingFileManager;

public class SyncConfirmDialog extends ConfirmDialog {
  static final String FILE_ICON_PATH = "/icons/file.png";

  public SyncConfirmDialog() {
    super(
        List.of(
            new FileDialogSection(
                "Planifiés", PatriLangStagingFileManager.getPlannedFiles(), FILE_ICON_PATH),
            new FileDialogSection(
                "Réalisés", PatriLangStagingFileManager.getDoneFiles(), FILE_ICON_PATH),
            new FileDialogSection(
                "Pièces justificatives", PatriLangStagingFileManager.getPJFiles(), FILE_ICON_PATH),
            new CommentDialogSection(PendingCommentManager.getPendings())),
        "Confirmer la synchronisation",
        "Voulez-vous synchroniser ces modifications avec Google Drive ?",
        "Synchroniser");
  }
}
