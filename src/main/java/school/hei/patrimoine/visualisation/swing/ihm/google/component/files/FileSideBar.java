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

  public FileSideBar(State state) {
    super(new BorderLayout());

    this.doneList =
        createList(getPatriLangDoneFiles(), state, () -> getPlannedList().clearSelection());
    this.plannedList =
        createList(getPatriLangPlannedFiles(), state, () -> getDoneList().clearSelection());

    var panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    addPanel(panel, "Journaux planifiés", plannedList);
    panel.add(Box.createVerticalStrut(20));
    addPanel(panel, "Journaux réalisés", doneList);

    add(panel, BorderLayout.CENTER);

    state.subscribe(
        "selectedFile",
        () -> {
          if (getSelectedFile(state).isEmpty()) {
            doneList.clearSelection();
            plannedList.clearSelection();
          }
        });
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
