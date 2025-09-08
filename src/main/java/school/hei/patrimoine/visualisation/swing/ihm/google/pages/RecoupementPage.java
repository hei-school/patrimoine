package school.hei.patrimoine.visualisation.swing.ihm.google.pages;

import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.transpileToutCas;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.PossessionRecoupée.Status;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import javax.swing.*;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.cas.CasSet;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.recouppement.RecoupeurDePossessions;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.Page;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.NavigateButton;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.AppBar;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileListCellRenderer;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileListModel;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileSideBar;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement.PossessionRecoupéeCellRenderer;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.PossessionRecoupée;

public class RecoupementPage extends Page {
  public static final String PAGE_NAME = "recoupement";
  private AppBar appBar;
  private JList<File> fileList;
  private State state;

  private CasSet plannedCasSet;
  private CasSet doneCasSet;

  private final DefaultListModel<PossessionRecoupée> possessionsModel;

  public RecoupementPage() {
    super(PAGE_NAME);
    this.state = new State(null, Status.TOUT);
    this.possessionsModel = new DefaultListModel<>();

    setLayout(new BorderLayout());

    subscribe("named-ids");
  }

  @Override
  public void update(AppContext appContext) {
    init();
    super.update(appContext);
  }

  private void init() {
    this.plannedCasSet = transpileToutCas(FileSideBar.getPlannedCasSetFile().getAbsolutePath());
    this.doneCasSet = transpileToutCas(FileSideBar.getDoneCasSetFile().getAbsolutePath());

    var possessionsFilterSelect = new JComboBox<>(Status.values());
    possessionsFilterSelect.setSelectedItem(Status.TOUT);
    possessionsFilterSelect.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    possessionsFilterSelect.setCursor(new Cursor(Cursor.HAND_CURSOR));
    possessionsFilterSelect.addActionListener(
        e -> {
          state =
              new State(
                  fileList.getSelectedValue(), (Status) possessionsFilterSelect.getSelectedItem());
          onStateChange();
        });

    this.appBar =
        new AppBar(
            () -> null,
            () -> null,
            () -> null,
            this::onStateChange,
            AppBar.Conf.builder()
                .withModeSelect(false)
                .withSyncButton(false)
                .withRecoupementButton(false)
                .withGraphicButton(false)
                .withSaveButton(false)
                .customComponents(
                    List.of(
                        new NavigateButton("Retour", "patrilang-files"), possessionsFilterSelect))
                .build());

    this.fileList =
        new JList<>(new FileListModel(FileSideBar.getPlannedPatrilangFilesWithoutCasSet()));
    fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    fileList.setCellRenderer(new FileListCellRenderer());
    fileList.addListSelectionListener(
        e -> {
          state = new State(fileList.getSelectedValue(), state.currentFilterStatus);
          onStateChange();
        });

    add(appBar, BorderLayout.NORTH);
    addMainSplitPane();
  }

  private void onStateChange() {
    if (state.currentFile() == null) {
      return;
    }

    possessionsModel.clear();
    getFilteredPossessionRecoupées().forEach(possessionsModel::addElement);
  }

  private void addMainSplitPane() {
    var horizontalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    var possessionsList = new JList<>(possessionsModel);

    possessionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    possessionsList.setCellRenderer(new PossessionRecoupéeCellRenderer());

    horizontalSplit.setLeftComponent(new JScrollPane(fileList));
    horizontalSplit.setRightComponent(new JScrollPane(possessionsList));
    horizontalSplit.setDividerLocation(200);

    add(horizontalSplit, BorderLayout.CENTER);
  }

  private List<PossessionRecoupée> getFilteredPossessionRecoupées() {
    var plannedPatrimoine = getPatrimoine(state.currentFile(), plannedCasSet);
    var donePatrimoine = getPatrimoine(state.currentFile(), doneCasSet);
    var recoupeurDePossession = RecoupeurDePossessions.of(plannedPatrimoine, donePatrimoine);
    Set<PossessionRecoupée> possessionRecouppées = new HashSet<>();

    switch (state.currentFilterStatus) {
      case TOUT -> {
        possessionRecouppées.addAll(
            toPossessionRecoupées(
                recoupeurDePossession.getPossessionsNonPrévus(), Status.NON_PRÉVU));
        possessionRecouppées.addAll(
            toPossessionRecoupées(
                recoupeurDePossession.getPossessionsNonExecutés(), Status.NON_ÉXECUTÉ));
        possessionRecouppées.addAll(
            toPossessionRecoupées(
                recoupeurDePossession.getPossessionsÉxecutésAvecCorrections(),
                Status.ÉXECUTÉ_AVEC_CORRECTION));
        possessionRecouppées.addAll(
            toPossessionRecoupées(
                recoupeurDePossession.getPossessionsÉxecutésSansCorrections(),
                Status.ÉXECUTÉ_SANS_CORRECTION));
      }

      case NON_PRÉVU ->
          possessionRecouppées.addAll(
              toPossessionRecoupées(
                  recoupeurDePossession.getPossessionsNonPrévus(), Status.NON_PRÉVU));
      case NON_ÉXECUTÉ ->
          possessionRecouppées.addAll(
              toPossessionRecoupées(
                  recoupeurDePossession.getPossessionsNonExecutés(), Status.NON_ÉXECUTÉ));
      case ÉXECUTÉ_AVEC_CORRECTION ->
          possessionRecouppées.addAll(
              toPossessionRecoupées(
                  recoupeurDePossession.getPossessionsÉxecutésAvecCorrections(),
                  Status.ÉXECUTÉ_AVEC_CORRECTION));
      case ÉXECUTÉ_SANS_CORRECTION ->
          possessionRecouppées.addAll(
              toPossessionRecoupées(
                  recoupeurDePossession.getPossessionsÉxecutésSansCorrections(),
                  Status.ÉXECUTÉ_SANS_CORRECTION));
    }

    return possessionRecouppées.stream()
        .sorted(Comparator.comparing((PossessionRecoupée p) -> p.possession().t()).reversed())
        .toList();
  }

  public record State(File currentFile, Status currentFilterStatus) {}

  private static Patrimoine getPatrimoine(File file, CasSet casSet) {
    var fileName = file.getName();
    var baseName = fileName.contains(".") ? fileName.substring(0, fileName.indexOf('.')) : fileName;

    return casSet.set().stream()
        .map(Cas::patrimoine)
        .filter(p -> p.nom().equals(baseName))
        .findFirst()
        .orElseThrow();
  }

  private static Set<PossessionRecoupée> toPossessionRecoupées(
      Set<Possession> possessions, Status status) {
    return possessions.stream().map(p -> new PossessionRecoupée(p, status)).collect(toSet());
  }
}
