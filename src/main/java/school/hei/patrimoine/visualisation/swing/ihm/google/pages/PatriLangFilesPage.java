package school.hei.patrimoine.visualisation.swing.ihm.google.pages;

import static school.hei.patrimoine.patrilang.PatriLangTranspiler.TOUT_CAS_FILE_EXTENSION;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.AppBar.*;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.html.ViewMode.VIEW;
import static school.hei.patrimoine.visualisation.swing.ihm.google.mode.config.EnvironnementConfigMode.getCurrentMode;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.MessageDialog.showError;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.*;
import java.util.List;
import javax.swing.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.google.model.Pagination;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.PlaceholderTextField;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.LazyPage;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.AppBar;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin.CasSetAnalyzerButton;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin.SaveAndSyncFileButton;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.CommentSideBar;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.LocalCommentActions;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.LocalCommentManager;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileSideBar;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.html.HtmlViewer;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement.AddImprevuDialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.CasSetSetter;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.Debouncer;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;

@Getter
@Slf4j
public class PatriLangFilesPage extends LazyPage {
  public static final String PAGE_NAME = "patrilang-files";
  private static final int COMMENT_PAGE_SIZE = 100;

  private final State state;
  private final CasSetSetter casSetSetter;
  private final LocalCommentActions localCommentActions;

  private Button addImprevuButton;
  private final HtmlViewer htmlViewer;
  private CommentSideBar commentSideBar;

  public PatriLangFilesPage() {
    super(PAGE_NAME);
    this.casSetSetter = CasSetSetter.getInstance();
    this.localCommentActions = new LocalCommentActions(LocalCommentManager.getInstance());

    state =
        new State(
            Map.of(
                "viewMode",
                VIEW,
                "fontSize",
                12,
                "commentPagination",
                new Pagination(COMMENT_PAGE_SIZE, null)));

    this.htmlViewer = new HtmlViewer(state);

    state.subscribe(
        "selectedFile",
        () -> {
          this.updateCas();
          this.updateAddImprevuButtonVisibility();
        });

    casSetSetter.addObserver(
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
    casSetSetter.updatedCasSet();

    addAppBar();
    addMainSplitPane();
  }

  private SaveAndSyncFileButton saveAndSyncFileButton() {
    return new SaveAndSyncFileButton(
        state,
        htmlViewer,
        () -> {
          getCommentSideBar().refreshCommentsCache();
          return null;
        });
  }

  private void addAppBar() {
    this.addImprevuButton = addImprevuButton(state);
    addImprevuButton.setVisible(false);

    var appBar =
        new AppBar(
            List.of(
                builtInViewModeSelect(state),
                saveAndSyncFileButton(),
                new CasSetAnalyzerButton(casSetSetter),
                recoupementButton(),
                addImprevuButton,
                addSearchTextBar()),
            getCurrentMode().appBarRightComponents(state));

    add(appBar, BorderLayout.NORTH);
  }

  private void addMainSplitPane() {
    var horizontalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    horizontalSplit.setLeftComponent(new FileSideBar(state));

    var rightSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    rightSplit.setLeftComponent(new JScrollPane(htmlViewer));

    if (getCurrentMode().enableComments()) {
      this.commentSideBar = new CommentSideBar(state, localCommentActions);
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
          state.invalidate(
              Set.of(
                  "selectedFile", "selectedFileCasSet", "isPlannedSelectedFile", "selectedFileId"));
          pageManager().navigate(RecoupementPage.PAGE_NAME);
        });
  }

  static Button addImprevuButton(State state) {
    return new Button(
        "Ajouter un imprévu",
        e -> {
          if (state.get("selectedFile") == null) {
            showError("Erreur", "Veuillez sélectionner un fichier avant d'ajouter un imprévu");
            return;
          }
          new AddImprevuDialog(state);
        });
  }

  private PlaceholderTextField addSearchTextBar() {
    var searchBar = new PlaceholderTextField("Rechercher");
    searchBar.setPreferredSize(new Dimension(180, 35));
    searchBar.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

    var contentSearchDebouncer =
        new Debouncer(() -> state.update(Map.of("searchText", searchBar.getText().trim())));

    searchBar.addKeyListener(
        new KeyAdapter() {
          @Override
          public void keyReleased(KeyEvent evt) {
            contentSearchDebouncer.restart();
          }
        });
    return searchBar;
  }

  private void updateAddImprevuButtonVisibility() {
    var optionalSelectedFile = selectedFile();

    if (optionalSelectedFile.isEmpty() || isToutCasFile(optionalSelectedFile.get())) {
      addImprevuButton.setVisible(false);
      return;
    }

    addImprevuButton.setVisible(!isPlannedSelectedFile());
  }

  private void updateCas() {
    File selectedFile = state.get("selectedFile");
    if (isPlannedSelectedFile() || isToutCasFile(selectedFile)) {
      return;
    }

    var doneCas = casSetSetter.getCas(selectedFile, casSetSetter.doneCasSet());
    var plannedCas = casSetSetter.getCas(selectedFile, casSetSetter.plannedCasSet());
    state.update(Map.of("plannedCas", plannedCas, "doneCas", doneCas));
  }

  private Optional<File> selectedFile() {
    return Optional.ofNullable(state.get("selectedFile"));
  }

  private static boolean isToutCasFile(File file) {
    return file.getName().endsWith(TOUT_CAS_FILE_EXTENSION);
  }

  private boolean isPlannedSelectedFile() {
    return state.get("isPlannedSelectedFile") == null
        || (boolean) state.get("isPlannedSelectedFile");
  }
}
