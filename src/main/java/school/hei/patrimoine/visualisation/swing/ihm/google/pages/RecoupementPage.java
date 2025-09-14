package school.hei.patrimoine.visualisation.swing.ihm.google.pages;

import static school.hei.patrimoine.patrilang.PatriLangTranspiler.transpileToutCas;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.AppBar.builtInUserInfoPanel;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import javax.swing.*;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.cas.CasSet;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee.PossessionRecoupeeStatus;
import school.hei.patrimoine.modele.recouppement.RecoupeurDePossessions;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.AppBar;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.LazyPage;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.NavigateButton;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileListCellRenderer;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileListModel;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileSideBar;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement.PossessionRecoupeeListPanel;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;
import school.hei.patrimoine.visualisation.swing.ihm.google.utils.AsyncTask;

public class RecoupementPage extends LazyPage {
  public static final String PAGE_NAME = "recoupement";

  private CasSet plannedCasSet;
  private CasSet doneCasSet;

  private final State state;
  private final PossessionRecoupeeListPanel possessionRecoupeeListPanel;

  public RecoupementPage() {
    super(PAGE_NAME);
    this.state = new State(Map.of("filterStatus", PossessionRecoupeeFilterStatus.TOUT));
    this.possessionRecoupeeListPanel =
        new PossessionRecoupeeListPanel(
            () -> {
              updateCasSet();
              update();
            });

    state.subscribe(Set.of("filterStatus", "selectedFile"), this::update);
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
    statusFilter.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    statusFilter.setCursor(new Cursor(Cursor.HAND_CURSOR));
    statusFilter.addActionListener(
        e -> state.update("filterStatus", statusFilter.getSelectedItem()));

    var appBar =
        new AppBar(
            List.of(new NavigateButton("Retour", "patrilang-files"), statusFilter),
            List.of(builtInUserInfoPanel()));

    add(appBar, BorderLayout.NORTH);
  }

  private void addMainSplitPane() {
    var fileList =
        new JList<>(new FileListModel(FileSideBar.getPlannedPatrilangFilesWithoutCasSet()));
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
    var plannedPatrimoine = getPatrimoine(state.get("selectedFile"), plannedCasSet);
    var donePatrimoine = getPatrimoine(state.get("selectedFile"), doneCasSet);
    var recoupeurDePossession = RecoupeurDePossessions.of(plannedPatrimoine, donePatrimoine);
    var possessionsRecoupees = recoupeurDePossession.getPossessionsRecoupees();

    Set<PossessionRecoupeeStatus> statusToKeep = new HashSet<>();
    switch ((PossessionRecoupeeFilterStatus) state.get("filterStatus")) {
      case TOUT -> statusToKeep.addAll(Set.of(PossessionRecoupeeStatus.values()));
      case IMPREVU -> statusToKeep.add(PossessionRecoupeeStatus.IMPREVU);
      case NON_EXECUTE -> statusToKeep.add(PossessionRecoupeeStatus.NON_EXECUTE);
      case EXECUTE_AVEC_CORRECTION ->
          statusToKeep.add(PossessionRecoupeeStatus.EXECUTE_AVEC_CORRECTION);
      case EXECUTE_SANS_CORRECTION ->
          statusToKeep.add(PossessionRecoupeeStatus.EXECUTE_SANS_CORRECTION);
    }

    return possessionsRecoupees.stream()
        .filter(p -> statusToKeep.contains(p.status()))
        .sorted(Comparator.comparing((PossessionRecoupee p) -> p.possession().t()).reversed())
        .toList();
  }

  @Override
  protected void update() {
    if (state.get("selectedFile") == null) {
      return;
    }

    AsyncTask.<List<PossessionRecoupee>>builder()
        .task(this::getFilteredPossessionRecoupees)
        .onSuccess(possessionRecoupeeListPanel::update)
        .withDialogLoading(isActive())
        .build()
        .execute();
  }

  private static Patrimoine getPatrimoine(File file, CasSet casSet) {
    var fileName = file.getName();
    var baseName = fileName.contains(".") ? fileName.substring(0, fileName.indexOf('.')) : fileName;

    return casSet.set().stream()
        .map(Cas::patrimoine)
        .filter(p -> p.nom().equals(baseName))
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
