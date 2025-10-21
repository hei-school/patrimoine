package school.hei.patrimoine.visualisation.swing.ihm.google.pages;

import static school.hei.patrimoine.patrilang.PatriLangTranspiler.transpileToutCas;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.AppBar.builtInUserInfoPanel;
import static school.hei.patrimoine.visualisation.swing.ihm.google.utils.MessageDialog.showError;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.Timer;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.cas.CasSet;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;
import school.hei.patrimoine.modele.recouppement.RecoupementStatus;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Footer;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.PlaceholderTextField;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.LazyPage;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.AppBar;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.NavigateButton;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileListCellRenderer;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileListModel;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileSideBar;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement.AddImprevuDialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement.PossessionRecoupeeListPanel;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;
import school.hei.patrimoine.visualisation.swing.ihm.google.providers.PossessionRecoupeeProvider;
import school.hei.patrimoine.visualisation.swing.ihm.google.providers.model.Pagination;
import school.hei.patrimoine.visualisation.swing.ihm.google.utils.AsyncTask;

public class RecoupementPage extends LazyPage {
  public static final String PAGE_NAME = "recoupement";
  public static final int DEFAULT_TOTAL_PAGES = 1;

  private CasSet plannedCasSet;
  private CasSet doneCasSet;

  private Footer footer;
  private final State state;
  private final PossessionRecoupeeListPanel possessionRecoupeeListPanel;

  public RecoupementPage() {
    super(PAGE_NAME);
    this.state =
        new State(
            Map.of(
                "totalPages",
                1,
                "filterStatus",
                PossessionRecoupeeFilterStatus.TOUT,
                "pagination",
                new Pagination(1, DEFAULT_TOTAL_PAGES)));
    this.possessionRecoupeeListPanel = new PossessionRecoupeeListPanel(state);

    state.subscribe(
        Set.of("filterStatus", "selectedFile", "pagination", "filterName"), this::update);
    globalState()
        .subscribe(
            Set.of("newUpdate"),
            () -> {
              updateCasSet();
              update();
            });

    setLayout(new BorderLayout());
  }

  @Override
  protected void init() {
    updateCasSet();

    addAppBar();
    addMainSplitPane();
    addFooter();
  }

  private void updateCasSet() {
    boolean isNewUpdate =
        globalState().get("newUpdate") == null || (boolean) globalState().get("newUpdate");

    if (!isNewUpdate) {
      return;
    }

    this.plannedCasSet = transpileToutCas(FileSideBar.getPlannedCasSetFile().getAbsolutePath());
    this.doneCasSet = transpileToutCas(FileSideBar.getDoneCasSetFile().getAbsolutePath());
    globalState().update("newUpdate", false);
  }

  private void addAppBar() {
    var statusFilter = new JComboBox<>(PossessionRecoupeeFilterStatus.values());
    statusFilter.setSelectedItem(PossessionRecoupeeFilterStatus.TOUT);
    statusFilter.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
    statusFilter.setCursor(new Cursor(Cursor.HAND_CURSOR));
    statusFilter.addActionListener(
        e -> state.update("filterStatus", statusFilter.getSelectedItem()));

    var addImprevuButton =
        new Button(
            "Ajouter un imprévu",
            e -> {
              if (state.get("selectedFile") == null) {
                showError("Erreur", "Veuillez sélectionner un fichier avant d'ajouter un imprévu");
                return;
              }

              new AddImprevuDialog(state);
            });

    var nameFilter = new PlaceholderTextField("Rechercher");
    nameFilter.setPreferredSize(new Dimension(180, 35));
    nameFilter.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

    int debounceDelay = 500;
    Timer debounceTimer =
        new Timer(
            debounceDelay,
            e -> {
              state.update(
                  Map.of(
                      "filterName",
                      nameFilter.getText().trim(),
                      "pagination",
                      new Pagination(1, DEFAULT_TOTAL_PAGES)));
            });
    debounceTimer.setRepeats(false);

    nameFilter.addKeyListener(
        new KeyAdapter() {
          @Override
          public void keyReleased(KeyEvent evt) {
            debounceTimer.restart();
          }
        });

    var appBar =
        new AppBar(
            List.of(
                new NavigateButton("Retour", "patrilang-files"),
                statusFilter,
                addImprevuButton,
                nameFilter),
            List.of(builtInUserInfoPanel()));

    add(appBar, BorderLayout.NORTH);
  }

  private void addFooter() {
    footer = new Footer(state);
    add(footer, BorderLayout.SOUTH);
  }

  private void addMainSplitPane() {
    var fileList = new JList<>(new FileListModel(FileSideBar.getDonePatrilangFilesWithoutCasSet()));
    fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    fileList.setCellRenderer(new FileListCellRenderer());
    fileList.addListSelectionListener(
        e -> state.update("selectedFile", fileList.getSelectedValue()));

    var horizontalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    horizontalSplit.setLeftComponent(new JScrollPane(fileList));
    horizontalSplit.setRightComponent(possessionRecoupeeListPanel.toScrollPane());
    horizontalSplit.setDividerLocation(200);

    add(horizontalSplit, BorderLayout.CENTER);
  }

  private List<PossessionRecoupee> getFilteredPossessionRecoupees() {
    var plannedCas = getCas(state.get("selectedFile"), plannedCasSet);
    var doneCas = getCas(state.get("selectedFile"), doneCasSet);
    var filteredStatus = (PossessionRecoupeeFilterStatus) state.get("filterStatus");

    state.update(Map.of("plannedCas", plannedCas, "doneCas", doneCas));

    Set<RecoupementStatus> statusToKeep = new HashSet<>();
    switch (filteredStatus) {
      case TOUT -> statusToKeep.addAll(Set.of(RecoupementStatus.values()));
      case IMPREVU -> statusToKeep.add(RecoupementStatus.IMPREVU);
      case NON_EXECUTE -> statusToKeep.add(RecoupementStatus.NON_EXECUTE);
      case EXECUTE_AVEC_CORRECTION -> statusToKeep.add(RecoupementStatus.EXECUTE_AVEC_CORRECTION);
      case EXECUTE_SANS_CORRECTION -> statusToKeep.add(RecoupementStatus.EXECUTE_SANS_CORRECTION);
    }

    var provider = new PossessionRecoupeeProvider();
    var meta = new PossessionRecoupeeProvider.Meta(plannedCas, doneCas);
    var filter =
        new PossessionRecoupeeProvider.Filter(
            statusToKeep, state.get("pagination"), state.get("filterName"));

    var result = provider.getList(meta, filter);
    if (result.totalPage() != (int) state.get("totalPages")) {
      state.update("totalPages", result.totalPage());
    }

    return result.possessionRecoupees();
  }

  @Override
  protected void update() {
    if (state.get("selectedFile") == null) {
      return;
    }

    AsyncTask.<List<PossessionRecoupee>>builder()
        .task(this::getFilteredPossessionRecoupees)
        .onSuccess(possessionRecoupeeListPanel::update)
        .withDialogLoading(false)
        .build()
        .execute();
  }

  private static Cas getCas(File file, CasSet casSet) {
    var fileName = file.getName();
    var baseName = fileName.contains(".") ? fileName.substring(0, fileName.indexOf('.')) : fileName;

    return casSet.set().stream()
        .filter(cas -> cas.patrimoine().nom().equals(baseName))
        .findFirst()
        .orElseThrow();
  }

  public enum PossessionRecoupeeFilterStatus {
    TOUT("Tout"),
    IMPREVU("Imprévu"),
    NON_EXECUTE("Non Éxecuté"),
    EXECUTE_AVEC_CORRECTION("Éxecuté avec correction"),
    EXECUTE_SANS_CORRECTION("Éxecuté sans correction");

    public final String label;

    PossessionRecoupeeFilterStatus(String label) {
      this.label = label;
    }

    @Override
    public String toString() {
      return label;
    }
  }
}
