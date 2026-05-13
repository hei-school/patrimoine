package school.hei.patrimoine.visualisation.swing.ihm.google.component.files;

import static javax.swing.ListSelectionModel.SINGLE_SELECTION;
import static school.hei.patrimoine.visualisation.swing.ihm.google.providers.FilesProvider.*;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import javax.swing.*;
import lombok.AccessLevel;
import lombok.Getter;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContext;

public class FileSideBar extends JPanel {
  @Getter(AccessLevel.PRIVATE)
  private final JList<PatriLangFileContext> doneList;

  @Getter(AccessLevel.PRIVATE)
  private final JList<PatriLangFileContext> plannedList;

  @Getter(AccessLevel.PRIVATE)
  private final JList<PatriLangFileContext> pieceJustificativeList;

  public FileSideBar(State state) {
    super(new BorderLayout());

    this.doneList =
        createList(
            getPatriLangDoneFiles(),
            state,
            () -> {
              getPlannedList().clearSelection();
              getPieceJustificativeList().clearSelection();
            });
    this.plannedList =
        createList(
            getPatriLangPlannedFiles(),
            state,
            () -> {
              getDoneList().clearSelection();
              getPieceJustificativeList().clearSelection();
            });

    this.pieceJustificativeList =
        createList(
            getPatriLangJustificativeFiles(),
            state,
            () -> {
              getDoneList().clearSelection();
              getPlannedList().clearSelection();
            });

    var panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    addPanel(panel, "Journaux planifiés", plannedList);
    panel.add(Box.createVerticalStrut(20));
    addPanel(panel, "Journaux réalisés", doneList);
    panel.add(Box.createVerticalStrut(20));
    addPanel(panel, "Pièces justificatives", pieceJustificativeList);

    add(panel, BorderLayout.CENTER);

    state.subscribe(
        "selectedFile",
        () -> {
          if (getSelectedFile(state).isEmpty()) {
            doneList.clearSelection();
            plannedList.clearSelection();
            pieceJustificativeList.clearSelection();
          }
        });
  }

  public void refresh() {
    ((FileListModel) doneList.getModel()).refresh(getPatriLangDoneFiles());
    ((FileListModel) plannedList.getModel()).refresh(getPatriLangPlannedFiles());
    ((FileListModel) pieceJustificativeList.getModel()).refresh(getPatriLangJustificativeFiles());
  }

  public static Optional<PatriLangFileContext> getSelectedFile(State state) {
    return Optional.ofNullable(state.get("selectedFile"));
  }

  public interface SelectedFileSupplier extends Supplier<Optional<PatriLangFileContext>> {}

  public static JList<PatriLangFileContext> createList(
      List<PatriLangFileContext> files, State state, Runnable onSuccess) {
    var list = new JList<>(new FileListModel(files));
    list.setCellRenderer(new FileListCellRenderer());
    list.setSelectionMode(SINGLE_SELECTION);

    var debouncer =
        new Debouncer(
            () -> {
              var selectedFile = list.getSelectedValue();
              if (selectedFile == null) {
                return;
              }
              state.update("selectedFile", selectedFile);
              onSuccess.run();
            });

    list.addListSelectionListener(
        e -> {
          if (!e.getValueIsAdjusting()) {
            debouncer.restart();
          }
        });

    return list;
  }

  private static void addPanel(JPanel base, String labelValue, JList<PatriLangFileContext> list) {
    var label = new JLabel(labelValue);
    label.setFont(label.getFont().deriveFont(Font.BOLD));

    base.add(label);
    base.add(new JScrollPane(list));
  }
}
