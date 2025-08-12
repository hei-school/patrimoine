package school.hei.patrimoine.visualisation.swing.ihm.google.component;

import java.awt.*;
import java.io.File;
import java.util.List;
import javax.swing.*;
import school.hei.patrimoine.google.DriveApi;
import school.hei.patrimoine.google.exception.GoogleIntegrationException;
import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.google.provider.CommentProvider;

public class CommentSideBar extends JPanel {
  private final CommentProvider commentProvider;
  private final DefaultListModel<String> commentListModel;

  public CommentSideBar(DriveApi driveApi) {
    super(new BorderLayout());

    this.commentProvider = new CommentProvider(driveApi);
    this.commentListModel = new DefaultListModel<>();

    var scrollPane = new JScrollPane(new JList<>(commentListModel));
    scrollPane.setBorder(BorderFactory.createTitledBorder("Commentaires"));

    add(scrollPane, BorderLayout.CENTER);
  }

  public void update(File currentFile, String currentFileDriveId) {

    if (currentFile == null || !currentFile.exists()) {
      commentListModel.clear();
      return;
    }

    if (currentFileDriveId == null) {
      commentListModel.clear();
      commentListModel.addElement("Pas d'ID Google Drive pour ce fichier.");
      return;
    }

    SwingWorker<java.util.List<String>, Void> commentWorker =
        new SwingWorker<>() {
          @Override
          protected List<String> doInBackground() throws GoogleIntegrationException {
            return commentProvider.getByFileId(currentFileDriveId).stream()
                .map(Comment::content)
                .toList();
          }

          @Override
          protected void done() {
            try {
              var comments = get();
              commentListModel.clear();
              for (var comment : comments) {
                commentListModel.addElement(comment);
              }
            } catch (Exception ex) {
              commentListModel.clear();
              commentListModel.addElement("Erreur lors du chargement des commentaires.");
              throw new RuntimeException(ex);
            }
          }
        };
    commentWorker.execute();
  }
}
