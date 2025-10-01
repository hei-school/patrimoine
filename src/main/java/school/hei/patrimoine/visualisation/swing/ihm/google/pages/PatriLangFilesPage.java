package school.hei.patrimoine.visualisation.swing.ihm.google.pages;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.transpileToutCas;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.AppBar.*;
import static school.hei.patrimoine.visualisation.swing.ihm.google.utils.MessageDialog.showError;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.*;
import lombok.Getter;
import school.hei.patrimoine.cas.CasSet;
import school.hei.patrimoine.cas.CasSetAnalyzer;
import school.hei.patrimoine.google.model.Pagination;
import school.hei.patrimoine.modele.recouppement.RecoupeurDeCasSet;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.AppBar;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.HtmlViewer;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.LazyPage;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.CommentSideBar;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileSideBar;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;
import school.hei.patrimoine.visualisation.swing.ihm.google.utils.AsyncTask;

@Getter
public class PatriLangFilesPage extends LazyPage {
  public static final String PAGE_NAME = "patrilang-files";
  private static final Integer COMMENT_PAGE_SIZE = 2;

  private final State state;
  private HtmlViewer htmlViewer;

  public PatriLangFilesPage() {
    super(PAGE_NAME);

    state =
        new State(
            Map.of(
                "viewMode",
                ViewMode.VIEW,
                "fontSize",
                14,
                "commentPagination",
                new Pagination(COMMENT_PAGE_SIZE, null)));

    setLayout(new BorderLayout());
  }

  @Override
  protected void init() {
    addAppBar();
    addMainSplitPane();
  }

  private void addAppBar() {
    var appBar =
        new AppBar(
            List.of(
                builtInViewModeSelect(state),
                builtInSaveButton(state, () -> getHtmlViewer().getText()),
                builtInSyncButton(state),
                evolutionGraphicButton(),
                recoupementButton()),
            List.of(builtInFontSizeControllerButton(state), builtInUserInfoPanel()));

    add(appBar, BorderLayout.NORTH);
  }

  private void addMainSplitPane() {
    var horizontalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    horizontalSplit.setLeftComponent(new FileSideBar(state));

    this.htmlViewer = new HtmlViewer(state);
    var rightSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    rightSplit.setLeftComponent(htmlViewer.toScrollPane());
    rightSplit.setRightComponent(new CommentSideBar(state));
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
                  "selectedFile", "selectedCasSetFile", "isPlannedSelectedFile", "selectedFileId"));
          pageManager().navigate(RecoupementPage.PAGE_NAME);
        });
  }

  public Button evolutionGraphicButton() {
    return new Button("Évolution graphique", e -> showCasSetAnalyser());
  }

  private static void showCasSetAnalyser() {
    AsyncTask.<CasSet>builder()
        .task(
            () -> {
              var plannedCasSet =
                  transpileToutCas(FileSideBar.getPlannedCasSetFile().getAbsolutePath());
              var doneCasSet = transpileToutCas(FileSideBar.getDoneCasSetFile().getAbsolutePath());
              return RecoupeurDeCasSet.of(plannedCasSet, doneCasSet).getRecouped();
            })
        .onSuccess(casSet -> new CasSetAnalyzer(DISPOSE_ON_CLOSE).accept(casSet))
        .onError(
            error -> {
              if (error.getMessage().contains("Objectif")) {
                showError("Erreur", "Certains objectifs ne sont pas atteints");
                return;
              }

              showError(
                  "Erreur",
                  "Une erreur s'est produite lors de la génération de l'évolution graphique");
            })
        .build()
        .execute();
  }
}
