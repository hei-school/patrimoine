package school.hei.patrimoine.visualisation.swing.ihm.google;

import java.awt.*;
import javax.swing.*;
import school.hei.patrimoine.google.DriveApi;
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
    this.commentSideBar = new CommentSideBar(driveApi);
    this.fileSideBar = new FileSideBar(ids, this::updateUI);
    this.appBar =
        new AppBar(this, currentUser, driveApi, this.fileSideBar, this.htmlViewer, this::updateUI);

    setLayout(new BorderLayout());
    add(this.appBar, BorderLayout.NORTH);
    add(createMainSplitPane(), BorderLayout.CENTER);
  }

  private JSplitPane createMainSplitPane() {
    var horizontalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    horizontalSplit.setLeftComponent(fileSideBar.toScrollPane());
    horizontalSplit.setRightComponent(
        new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT, htmlViewer.toScrollPane(), this.commentSideBar));
    horizontalSplit.setDividerLocation(200);
    ((JSplitPane) horizontalSplit.getRightComponent()).setDividerLocation(700);

    return horizontalSplit;
  }

  private void updateUI() {
    var currentMode = appBar.getCurrentMode();
    var currentFontSize = appBar.getControlledFontSize();
    var currentFile = fileSideBar.getSelectedFile().orElse(null);
    var currentFileDriveId = fileSideBar.getSelectedFileDriveId().orElse(null);

    htmlViewer.update(currentMode, currentFile, currentFontSize);
    commentSideBar.update(currentFile, currentFileDriveId);
  }

  public enum ViewMode {
    VIEW,
    EDIT
  }
}
