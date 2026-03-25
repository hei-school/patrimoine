package school.hei.patrimoine.visualisation.swing.ihm.google.pages;

import static school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileSideBar.getSelectedFile;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.html.ViewMode.VIEW;
import static school.hei.patrimoine.visualisation.swing.ihm.google.config.EnvironmentConfig.isOfflineMode;
import static school.hei.patrimoine.visualisation.swing.ihm.google.config.EnvironmentConfig.isOnlineMode;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContentManager.clearAllTempContents;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContentManager.hasUnsavedChanges;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFilesWatcher.dispatch;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFilesWatcher.getCas;
import static school.hei.patrimoine.visualisation.swing.ihm.google.providers.FilesProvider.getDoneFile;
import static school.hei.patrimoine.visualisation.swing.ihm.google.providers.FilesProvider.getPlannedFile;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.google.model.Pagination;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.LazyPage;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.AppBar;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.CommentSideBar;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileSideBar;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.html.HtmlViewer;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFilesWatcher;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.RestoreConfirmDialog;

@Getter
@Slf4j
public class PatriLangFilesPage extends LazyPage {
  public static final String PAGE_NAME = "patrilang-files";
  private static final int COMMENT_PAGE_SIZE = 100;

  private final State state;
  private Button addImprevuButton;
  private final HtmlViewer htmlViewer;
  private CommentSideBar commentSideBar;
  private final FileSideBar fileSideBar;

  public PatriLangFilesPage() {
    super(PAGE_NAME);

    state =
        new State(
            Map.of(
                "viewMode",
                VIEW,
                "fontSize",
                12,
                "pagination",
                Pagination.builder().pageSize(COMMENT_PAGE_SIZE).build()));

    this.fileSideBar = new FileSideBar(state);
    this.htmlViewer = new HtmlViewer(state, fileSideBar);

    state.subscribe(
        "selectedFile",
        () -> {
          this.updateCas();
          this.updateAddImprevuButtonVisibility();
        });

    PatriLangFilesWatcher.addObserver(
        () -> {
          this.htmlViewer.update();

          this.updateCas();

          if (this.addImprevuButton != null) {
            this.updateAddImprevuButtonVisibility();
          }
        });

    setLayout(new BorderLayout());
  }

  @Override
  protected void init() {
    PatriLangFilesWatcher.dispatch();

    addAppBar();
    addMainSplitPane();
  }

  private SaveAndSyncFileButton saveAndSyncFileButton() {
    return new SaveAndSyncFileButton(state, htmlViewer, () -> commentSideBar.update());
  }

  private List<Component> leftAppBarControls(State state) {
    return List.of(
        new ViewModeSelect(state),
        saveAndSyncFileButton(),
        new CasSetAnalyzerButton(),
        new SearchTextBar(state),
        recoupementButton(),
        addImprevuButton,
        restoreButton());
  }

  private List<Component> rightAppBarControls(State state) {
    if (isOfflineMode()) {
      return List.of(new FontSizeControllerButton(state));
    }
    return List.of(new FontSizeControllerButton(state), new UserInfoPanel());
  }

  private void addAppBar() {
    this.addImprevuButton = new AddImprevuButton(state);
    addImprevuButton.setVisible(false);

    add(new AppBar(leftAppBarControls(state), rightAppBarControls(state)), BorderLayout.NORTH);
  }

  private void addMainSplitPane() {
    var horizontalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    horizontalSplit.setLeftComponent(fileSideBar);

    var rightSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    rightSplit.setLeftComponent(new JScrollPane(htmlViewer));

    if (isOnlineMode()) {
      this.commentSideBar = new CommentSideBar(state);
      rightSplit.setRightComponent(commentSideBar);
    }

    rightSplit.setDividerLocation(700);
    horizontalSplit.setRightComponent(rightSplit);
    horizontalSplit.setDividerLocation(200);

    add(horizontalSplit, BorderLayout.CENTER);
  }

  public Button recoupementButton() {
    return new Button(
        "Recoupement",
        e -> {
          if (hasUnsavedChanges()) {
            var dialog = new UnsavedChangesConfirmDialog("Recoupement");
            if (!dialog.isConfirmed()) {
              return;
            }
          }
          state.invalidate("selectedFile");
          pageManager().navigate(RecoupementPage.PAGE_NAME);
        });
  }

  private Button restoreButton() {
    return new Button(
        "Restaurer",
        e -> {
          var dialog = new RestoreConfirmDialog();
          if (dialog.isConfirmed()) {
            clearAllTempContents();
            htmlViewer.update();
            dispatch();
          }
        });
  }

  private void updateAddImprevuButtonVisibility() {
    var optionalSelectedFile = getSelectedFile(state);

    if (optionalSelectedFile.isEmpty()) {
      addImprevuButton.setVisible(false);
      return;
    }

    var selectedFile = optionalSelectedFile.get();
    if (selectedFile.isTypePJ() || selectedFile.isTypeToutCas()) {
      addImprevuButton.setVisible(false);
      return;
    }

    addImprevuButton.setVisible(selectedFile.isDone());
  }

  private void updateCas() {
    var optionalSelectedFile = getSelectedFile(state);
    if (optionalSelectedFile.isEmpty()) {
      return;
    }

    var selectedFile = optionalSelectedFile.get();
    if (selectedFile.isPlanned()) {
      return;
    }

    if (selectedFile.isTypeToutCas() || selectedFile.isTypeToutCas()) {
      return;
    }

    var doneCas = getCas(getDoneFile(selectedFile));
    var plannedCas = getCas(getPlannedFile(selectedFile));

    if (doneCas == null || plannedCas == null) return;
    state.update(Map.of("plannedCas", plannedCas, "doneCas", doneCas));
  }
}
