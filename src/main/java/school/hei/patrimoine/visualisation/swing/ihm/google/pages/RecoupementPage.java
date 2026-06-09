package school.hei.patrimoine.visualisation.swing.ihm.google.pages;

import static school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileSideBar.getSelectedFile;
import static school.hei.patrimoine.visualisation.swing.ihm.google.config.EnvironmentConfig.isOnlineMode;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFilesWatcher.*;
import static school.hei.patrimoine.visualisation.swing.ihm.google.providers.FilesProvider.*;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import javax.swing.*;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;
import school.hei.patrimoine.modele.recouppement.model.PossessionRecoupee;
import school.hei.patrimoine.modele.recouppement.model.RecoupementStatus;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.PlaceholderTextField;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.LazyPage;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.AppBar;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin.AddImprevuButton;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin.UserInfoPanel;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.NavigateButton;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileListCellRenderer;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.files.FileListModel;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement.PossessionRecoupeeListPanel;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.recoupement.RecoupementFooter;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFilesWatcher;
import school.hei.patrimoine.visualisation.swing.ihm.google.pages.filters.PossessionRecoupeeFilterPj;
import school.hei.patrimoine.visualisation.swing.ihm.google.pages.filters.PossessionRecoupeeFilterStatus;
import school.hei.patrimoine.visualisation.swing.ihm.google.providers.PJProvider;
import school.hei.patrimoine.visualisation.swing.ihm.google.providers.PossessionRecoupeeProvider;
import school.hei.patrimoine.visualisation.swing.ihm.google.providers.PossessionRecoupeeProvider.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.providers.model.Pagination;

@Slf4j
public class RecoupementPage extends LazyPage {
  public static final String PAGE_NAME = "recoupement";
  public static final int RECOUPEMENT_ITEM_PER_PAGE = 50;

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
                PossessionRecoupeeFilterStatus.TOUS,
                "filterPj",
                PossessionRecoupeeFilterPj.TOUS,
                "pagination",
                new Pagination(1, RECOUPEMENT_ITEM_PER_PAGE)));

    this.possessionRecoupeeListPanel = new PossessionRecoupeeListPanel(state);

    PatriLangFilesWatcher.addObserver(this::update);

    state.subscribe(
        Set.of("filterStatus", "filterPj", "selectedFile", "pagination", "filterName"),
        this::update);

    setLayout(new BorderLayout());
  }

  @Override
  protected void init() {
    addAppBar();
    addMainSplitPane();
    addFooter();
  }

  private static JComboBox<PossessionRecoupeeFilterStatus> getStatusFilter(State state) {
    var statusFilter = new JComboBox<>(PossessionRecoupeeFilterStatus.values());
    statusFilter.setSelectedItem(PossessionRecoupeeFilterStatus.TOUS);
    statusFilter.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
    statusFilter.setCursor(new Cursor(Cursor.HAND_CURSOR));
    statusFilter.setToolTipText("Filtrer par status");
    statusFilter.addActionListener(
        e -> state.update("filterStatus", statusFilter.getSelectedItem()));

    return statusFilter;
  }

  private static List<Component> leftAppBarControls(State state) {
    return List.of(
        new NavigateButton("Retour", "patrilang-files"),
        getStatusFilter(state),
        getPjFilter(state),
        new AddImprevuButton(state),
        getPlaceholderTextField(state));
  }

  private static JComboBox<PossessionRecoupeeFilterPj> getPjFilter(State state) {
    var pjFilter = new JComboBox<>(PossessionRecoupeeFilterPj.values());
    pjFilter.setSelectedItem(PossessionRecoupeeFilterPj.TOUS);
    pjFilter.setPreferredSize(new Dimension(120, 40));
    pjFilter.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
    pjFilter.setCursor(new Cursor(Cursor.HAND_CURSOR));
    pjFilter.setToolTipText("Filtrer par pièce justificative");
    pjFilter.addActionListener(e -> state.update("filterPj", pjFilter.getSelectedItem()));
    return pjFilter;
  }

  private static List<Component> rightAppBarControls() {
    if (isOnlineMode()) {
      return List.of(new UserInfoPanel());
    }
    return List.of();
  }

  private void addAppBar() {
    var appBar = new AppBar(leftAppBarControls(state), rightAppBarControls());
    add(appBar, BorderLayout.NORTH);
  }

  private static PlaceholderTextField getPlaceholderTextField(State state) {
    var nameFilter = new PlaceholderTextField("Rechercher");
    nameFilter.setPreferredSize(new Dimension(180, 40));
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
    add(new RecoupementFooter(state), BorderLayout.SOUTH);
  }

  private void addMainSplitPane() {
    var fileList = new JList<>(new FileListModel(getDonePatrilangFilesWithoutCasSet()));
    fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    fileList.setCellRenderer(new FileListCellRenderer());

    var fileSelectDebouncer =
        new Debouncer(
            () -> {
              var selectedFile = fileList.getSelectedValue();
              if (selectedFile != null) {
                state.update(
                    Map.of(
                        "selectedFile",
                        selectedFile,
                        "pagination",
                        new Pagination(1, RECOUPEMENT_ITEM_PER_PAGE)));
              }
            });

    fileList.addListSelectionListener(
        e -> {
          if (!e.getValueIsAdjusting()) {
            fileSelectDebouncer.restart();
          }
        });

    var horizontalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    horizontalSplit.setDividerLocation(200);
    horizontalSplit.setLeftComponent(new JScrollPane(fileList));
    horizontalSplit.setRightComponent(possessionRecoupeeListPanel.toScrollPane());

    add(horizontalSplit, BorderLayout.CENTER);
  }

  private PossessionRecoupeeFilterStatus getStatusFilter() {
    return state.get("filterStatus");
  }

  private String getFilterName() {
    return state.get("filterName");
  }

  private Pagination getPagination() {
    Pagination p = state.get("pagination");
    return p != null ? p : new Pagination(1, RECOUPEMENT_ITEM_PER_PAGE);
  }

  private PossessionRecoupeeFilterPj getPjFilter() {
    PossessionRecoupeeFilterPj filter = state.get("filterPj");
    return filter != null ? filter : PossessionRecoupeeFilterPj.TOUS;
  }

  private Map<String, PieceJustificative> buildPjMap(PatriLangFileContext selectedFile) {
    try {
      return new PJProvider().apply(selectedFile);
    } catch (Exception e) {
      log.warn(
          "Impossible de charger les PJ pour {}: {}",
          selectedFile.getBaseFileName(),
          e.getMessage());
      return Map.of();
    }
  }

  private List<PossessionRecoupee<Possession>> getFilteredPossessionRecoupees() {
    var optionalSelectedFile = getSelectedFile(state);
    if (optionalSelectedFile.isEmpty()) {
      return List.of();
    }

    var selectedFile = optionalSelectedFile.get();

    var casSetComptes = getDoneCasSetComptes();
    var doneCas = getCas(getDoneFile(selectedFile));
    var plannedCas = getCas(getPlannedFile(selectedFile));

    if (doneCas == null || plannedCas == null) return null;
    state.update(Map.of("plannedCas", plannedCas, "doneCas", doneCas));

    Set<RecoupementStatus> statusToKeep = new HashSet<>();
    switch (getStatusFilter()) {
      case IMPREVU -> statusToKeep.add(RecoupementStatus.IMPREVU);
      case NON_EXECUTE -> statusToKeep.add(RecoupementStatus.NON_EXECUTE);
      case TOUS -> statusToKeep.addAll(Set.of(RecoupementStatus.values()));
      case EXECUTE_AVEC_CORRECTION -> statusToKeep.add(RecoupementStatus.EXECUTE_AVEC_CORRECTION);
      case EXECUTE_SANS_CORRECTION -> statusToKeep.add(RecoupementStatus.EXECUTE_SANS_CORRECTION);
    }

    var pjMap = buildPjMap(selectedFile);
    state.update("currentPjMap", pjMap);

    var provider = new PossessionRecoupeeProvider(casSetComptes);
    var meta = new Meta(plannedCas, doneCas);
    var filter =
        Filter.builder()
            .statuses(statusToKeep)
            // TODO: add correct filter for performance
            .fin(LocalDate.MAX)
            .debut(LocalDate.MIN)
            .nom(getFilterName())
            .pjFilter(getPjFilter())
            .pjMap(pjMap)
            .pagination(getPagination())
            .build();

    var result = provider.getList(meta, filter);
    if (result.totalPage() != (int) state.get("totalPages")) {
      state.update("totalPages", result.totalPage());
    }

    return result.data();
  }

  @Override
  protected void update() {
    if (getSelectedFile(state).isEmpty()) {
      return;
    }

    AsyncTask.<List<PossessionRecoupee<Possession>>>builder()
        .withDialogLoading(false)
        .onError(MessageDialog::showError)
        .task(this::getFilteredPossessionRecoupees)
        .onSuccess(
            list -> {
              Map<String, PieceJustificative> pjMap = state.get("currentPjMap");
              possessionRecoupeeListPanel.update(list, pjMap != null ? pjMap : Map.of());
            })
        .build()
        .execute();
  }
}
