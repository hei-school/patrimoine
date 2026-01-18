package school.hei.patrimoine.visualisation.swing.ihm.google.pages;

import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.transpilePieceJustificative;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.AppBar.builtInUserInfoPanel;
import static school.hei.patrimoine.visualisation.swing.ihm.google.pages.PatriLangFilesPage.addImprevuButton;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.*;
import java.util.List;
import javax.swing.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;
import school.hei.patrimoine.modele.recouppement.RecoupementStatus;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Footer;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.PlaceholderTextField;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.LazyPage;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.AppBar;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.NavigateButton;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileListCellRenderer;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileListModel;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileSideBar;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement.PossessionRecoupeeListPanel;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.AsyncTask;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.CasSetSetter;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.Debouncer;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;
import school.hei.patrimoine.visualisation.swing.ihm.google.providers.PossessionRecoupeeProvider;
import school.hei.patrimoine.visualisation.swing.ihm.google.providers.model.Pagination;

@Slf4j
public class RecoupementPage extends LazyPage {
  public static final String PAGE_NAME = "recoupement";
  public static final int RECOUPEMENT_ITEM_PER_PAGE = 50;

  private final CasSetSetter casSetSetter;
  private final State state;

  private final PossessionRecoupeeListPanel possessionRecoupeeListPanel;

  public RecoupementPage() {
    super(PAGE_NAME);
    this.casSetSetter = CasSetSetter.getInstance();

    this.state =
        new State(
            Map.of(
                "totalPages",
                1,
                "filterStatus",
                PossessionRecoupeeFilterStatus.TOUT,
                "filterPJ",
                PieceJustificativeFilter.TOUT,
                "pagination",
                new Pagination(1, RECOUPEMENT_ITEM_PER_PAGE)));

    this.possessionRecoupeeListPanel = new PossessionRecoupeeListPanel(state);

    casSetSetter.addObserver(this::update);
    state.subscribe(
        Set.of("filterStatus", "filterPJ", "selectedFile", "pagination", "filterName"),
        this::update);

    setLayout(new BorderLayout());
  }

  @Override
  protected void init() {

    addAppBar();
    addMainSplitPane();
    addFooter();
  }

  private void addAppBar() {
    var statusFilter = new JComboBox<>(PossessionRecoupeeFilterStatus.values());
    statusFilter.setSelectedItem(PossessionRecoupeeFilterStatus.TOUT);
    statusFilter.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
    statusFilter.setCursor(new Cursor(Cursor.HAND_CURSOR));
    statusFilter.addActionListener(
        e -> state.update("filterStatus", statusFilter.getSelectedItem()));

    var pjFilter = new JComboBox<>(PieceJustificativeFilter.values());
    pjFilter.setSelectedItem(PieceJustificativeFilter.TOUT);
    pjFilter.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
    pjFilter.setCursor(new Cursor(Cursor.HAND_CURSOR));
    pjFilter.addActionListener(e -> state.update("filterPJ", pjFilter.getSelectedItem()));

    var addImprevuButton = addImprevuButton(state);
    var nameFilter = getPlaceholderTextField();

    var appBar =
        new AppBar(
            List.of(
                new NavigateButton("Retour", "patrilang-files"),
                statusFilter,
                pjFilter,
                addImprevuButton,
                nameFilter),
            List.of(builtInUserInfoPanel()));

    add(appBar, BorderLayout.NORTH);
  }

  // TODO: refactor to a component utilities
  private @NotNull PlaceholderTextField getPlaceholderTextField() {
    var nameFilter = new PlaceholderTextField("Rechercher");
    nameFilter.setPreferredSize(new Dimension(180, 35));
    nameFilter.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

    var fileSearchDebouncer =
        new Debouncer(
            () ->
                state.update(
                    Map.of(
                        "filterName",
                        nameFilter.getText().trim(),
                        "pagination",
                        new Pagination(1, RECOUPEMENT_ITEM_PER_PAGE))));

    nameFilter.addKeyListener(
        new KeyAdapter() {
          @Override
          public void keyReleased(KeyEvent evt) {
            fileSearchDebouncer.restart();
          }
        });
    return nameFilter;
  }

  private void addFooter() {
    add(new Footer(state), BorderLayout.SOUTH);
  }

  private void addMainSplitPane() {
    var fileList = new JList<>(new FileListModel(FileSideBar.getDonePatrilangFilesWithoutCasSet()));
    fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    fileList.setCellRenderer(new FileListCellRenderer());

    var fileSelectDebouncer =
        new Debouncer(
            () -> {
              var selectedFile = fileList.getSelectedValue();
              if (selectedFile != null) {
                state.update("selectedFile", selectedFile);
              }
            });

    fileList.addListSelectionListener(
        e -> {
          if (!e.getValueIsAdjusting()) {
            fileSelectDebouncer.restart();
          }
        });

    var horizontalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    horizontalSplit.setLeftComponent(new JScrollPane(fileList));
    horizontalSplit.setRightComponent(possessionRecoupeeListPanel.toScrollPane());
    horizontalSplit.setDividerLocation(200);

    add(horizontalSplit, BorderLayout.CENTER);
  }

  private List<PossessionRecoupee> getFilteredPossessionRecoupees() {
    var plannedCas = casSetSetter.getCas(state.get("selectedFile"), casSetSetter.plannedCasSet());
    var doneCas = casSetSetter.getCas(state.get("selectedFile"), casSetSetter.doneCasSet());
    Set<Compte> casSetComptes = globalState().get("casSetComptes");
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

    var provider = new PossessionRecoupeeProvider(casSetComptes);
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

    var selectedFile = (File) state.get("selectedFile");
    log.info("RecoupementPage.update called, selectedFile=" + selectedFile.getName());

    Set<PieceJustificative> pieces = loadPiecesForCas(selectedFile);
    log.info("PJs chargées=" + pieces.size());

    AsyncTask.<List<PossessionRecoupee>>builder()
        .task(this::getFilteredPossessionRecoupees)
        .onSuccess(
            list -> {
              var pjFilter = (PieceJustificativeFilter) state.get("filterPJ");

              Set<PossessionRecoupee> filteredPossessions =
                  list.stream()
                      .filter(
                          pr -> {
                            boolean hasPJ =
                                pieces.stream()
                                    .anyMatch(
                                        pj ->
                                            possessionRecoupeeListPanel.hasMatchingPiece(
                                                pj, pr.possession().nom()));
                            return switch (pjFilter) {
                              case TOUT -> true;
                              case AVEC_PJ -> hasPJ;
                              case SANS_PJ -> !hasPJ;
                            };
                          })
                      .collect(toSet());

              log.info("Possessions recoupées chargées=" + list.size());
              possessionRecoupeeListPanel.update(filteredPossessions, pieces);
              log.info("PossessionRecoupeeListPanel.update appelé");
            })
        .withDialogLoading(false)
        .build()
        .execute();
  }

  private Set<PieceJustificative> loadPiecesForCas(File casFile) {
    if (casFile == null) return Set.of();

    var casName = casFile.getName();
    if (!casName.endsWith(".cas.md")) return Set.of();

    var baseName = casName.substring(0, casName.length() - ".cas.md".length());

    var pjFile =
        FileSideBar.getPatriLangJustificativeFiles().stream()
            .filter(f -> f.getName().equals(baseName + ".pj.md"))
            .findFirst()
            .orElse(null);

    if (pjFile == null) return Set.of();

    try {
      return new HashSet<>(transpilePieceJustificative(pjFile.getAbsolutePath()));
    } catch (Exception e) {
      e.printStackTrace();
      return Set.of();
    }
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

  public enum PieceJustificativeFilter {
    TOUT(""),
    AVEC_PJ("Avec PJ"),
    SANS_PJ("Sans PJ");

    public final String label;

    PieceJustificativeFilter(String label) {
      this.label = label;
    }

    @Override
    public String toString() {
      return label;
    }
  }
}
