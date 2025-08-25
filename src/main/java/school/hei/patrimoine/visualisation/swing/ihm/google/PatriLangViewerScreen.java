package school.hei.patrimoine.visualisation.swing.ihm.google;

import java.awt.*;
import javax.swing.*;
import lombok.Getter;
import school.hei.patrimoine.google.api.DriveApi;
import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.google.model.User;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkList;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.NamedID;

public class PatriLangViewerScreen extends Screen {
  private final AppBar appBar;
  private final HtmlViewer htmlViewer;
  private final FileSideBar fileSideBar;
  private final CommentSideBar commentSideBar;

  public PatriLangViewerScreen(GoogleLinkList<NamedID> ids, DriveApi driveApi, User currentUser) {
    super("PatriLang Viewer", 1_300, 800);

    this.htmlViewer = new HtmlViewer();
    this.fileSideBar = new FileSideBar(ids, this::updateUI);
    this.commentSideBar = new CommentSideBar(this, driveApi);
    this.appBar =
        new AppBar(this, currentUser, driveApi, this.fileSideBar, this.htmlViewer, this::updateUI);

    setLayout(new BorderLayout());
    add(this.appBar, BorderLayout.NORTH);
    add(createMainSplitPane(), BorderLayout.CENTER);
  }

  private JPanel renderComment(Comment c) {
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

    JLabel header = new JLabel(c.author().displayName() + " - " + c.createdAt());
    JTextArea content = new JTextArea(c.content());
    content.setLineWrap(true);
    content.setWrapStyleWord(true);
    content.setEditable(false);

    JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
    buttons.add(new JButton("Répondre"));
    if (!c.replies().isEmpty()) {
      buttons.add(new JButton("Réponses (" + c.replies().size() + ")"));
    }

    panel.add(header, BorderLayout.NORTH);
    panel.add(content, BorderLayout.CENTER);
    panel.add(buttons, BorderLayout.SOUTH);

    return panel;
  }

  private JSplitPane createMainSplitPane() {
    var horizontalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    horizontalSplit.setLeftComponent(fileSideBar.toScrollPane());

    // Split de droite : HTML en haut + Commentaires en bas ou à droite
    var rightSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    rightSplit.setLeftComponent(htmlViewer.toScrollPane());
    rightSplit.setRightComponent(commentSideBar.toScrollPane());
    rightSplit.setDividerLocation(700);

    horizontalSplit.setRightComponent(rightSplit);
    horizontalSplit.setDividerLocation(200);

    return horizontalSplit;
  }

  private void updateUI() {
    var currentMode = appBar.getCurrentMode();
    var currentFontSize = appBar.getControlledFontSize();
    var currentFile = fileSideBar.getSelectedFile().orElse(null);
    var currentFileDriveId = fileSideBar.getSelectedFileDriveId().orElse(null);

    commentSideBar.update(currentFileDriveId);
    htmlViewer.update(currentMode, currentFile, currentFontSize);
  }

  @Getter
  public enum ViewMode {
    VIEW("Affichage"),
    EDIT("Édition");

    private final String label;

    ViewMode(String label) {
      this.label = label;
    }

    @Override
    public String toString() {
      return label;
    }
  }
}
