package school.hei.patrimoine.visualisation.swing.ihm.google.pages;

import java.awt.*;
import javax.swing.*;
import lombok.Getter;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.Page;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.AppBar;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.CommentSideBar;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileSideBar;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.HtmlViewer;

@Getter
public class PatriLangFilesPage extends Page {
  public static final String PAGE_NAME = "patrilang-files";

  private AppBar appBar;
  private HtmlViewer htmlViewer;
  private FileSideBar fileSideBar;
  private CommentSideBar commentSideBar;

  public PatriLangFilesPage() {
    super(PAGE_NAME);

    setLayout(new BorderLayout());
    subscribe("named-ids");
  }

  @Override
  public void update(AppContext appContext) {
    init();
    super.update(appContext);
  }

  private void init() {
    this.fileSideBar = new FileSideBar(this::onStateChange);

    this.commentSideBar =
        new CommentSideBar(
            () -> getFileSideBar().getSelectedFile().orElse(null),
            () -> getFileSideBar().getSelectedFileDriveId().orElse(null));

    this.appBar =
        new AppBar(
            () -> getHtmlViewer().getText(),
            () -> getFileSideBar().getSelectedFileDriveId().orElse(null),
            () -> getFileSideBar().getSelectedFile().orElse(null),
            this::onStateChange);

    this.htmlViewer =
        new HtmlViewer(
            () -> getAppBar().getCurrentMode(),
            () -> getAppBar().getControlledFontSize(),
            () -> getFileSideBar().getSelectedFile().orElse(null));

    add(appBar, BorderLayout.NORTH);
    addMainSplitPane();
  }

  private void addMainSplitPane() {
    var horizontalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    horizontalSplit.setLeftComponent(fileSideBar);

    var rightSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    rightSplit.setLeftComponent(htmlViewer.toScrollPane());
    rightSplit.setRightComponent(commentSideBar.toScrollPane());
    rightSplit.setDividerLocation(700);

    horizontalSplit.setRightComponent(rightSplit);
    horizontalSplit.setDividerLocation(200);

    add(horizontalSplit, BorderLayout.CENTER);
  }

  private void onStateChange() {
    htmlViewer.update();
    commentSideBar.update();
  }
}
