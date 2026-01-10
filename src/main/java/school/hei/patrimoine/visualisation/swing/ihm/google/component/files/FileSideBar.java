package school.hei.patrimoine.visualisation.swing.ihm.google.component.files;

import static java.util.Objects.requireNonNull;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.*;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.FileCategory.*;

import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.swing.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkList.NamedID;

public class FileSideBar extends JPanel {
  private final State state;
  private final JList<File> plannedList;
  private final JList<File> doneList;
  private final JList<File> justificativeList;

  public FileSideBar(State state) {
    super(new BorderLayout());

    this.state = state;
    this.plannedList = new JList<>(new FileListModel(getPatriLangPlannedFiles()));
    this.doneList = new JList<>(new FileListModel(getPatriLangDoneFiles()));
    this.justificativeList = new JList<>(new FileListModel(getPatriLangJustificativeFiles()));

    plannedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    doneList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    justificativeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    plannedList.setCellRenderer(new FileListCellRenderer());
    doneList.setCellRenderer(new FileListCellRenderer());
    justificativeList.setCellRenderer(new FileListCellRenderer());

    var plannedDebouncer =
        new Debouncer(
            () -> {
              var selectedFile = plannedList.getSelectedValue();
              if (selectedFile == null) return;
              this.state.update(
                  Map.of(
                      "selectedFile",
                      selectedFile,
                      "selectedCasSetFile",
                      getPlannedCasSetFile(),
                      "selectedFileId",
                      getSelectedFileDriveId(selectedFile, PLANNED).orElse(""),
                      "isPlannedSelectedFile",
                      true));
              doneList.clearSelection();
              justificativeList.clearSelection();
            });

    var doneDebouncer =
        new Debouncer(
            () -> {
              var selectedFile = doneList.getSelectedValue();
              if (selectedFile == null) return;
              this.state.update(
                  Map.of(
                      "selectedFile",
                      selectedFile,
                      "selectedCasSetFile",
                      getDoneCasSetFile(),
                      "selectedFileId",
                      getSelectedFileDriveId(selectedFile, DONE).orElse(""),
                      "isPlannedSelectedFile",
                      false));
              plannedList.clearSelection();
              justificativeList.clearSelection();
            });

    var justificativeDebouncer =
        new Debouncer(
            () -> {
              var selectedFile = justificativeList.getSelectedValue();
              if (selectedFile == null) return;
              this.state.update(
                  Map.of(
                      "selectedFile",
                      selectedFile,
                      "selectedFileId",
                      getSelectedFileDriveId(selectedFile, JUSTIFICATIVE).orElse(""),
                      "isPlannedSelectedFile",
                      false));
              plannedList.clearSelection();
              doneList.clearSelection();
            });

    plannedList.addListSelectionListener(
        e -> {
          if (!e.getValueIsAdjusting()) {
            plannedDebouncer.restart();
          }
        });

    doneList.addListSelectionListener(
        e -> {
          if (!e.getValueIsAdjusting()) {
            doneDebouncer.restart();
          }
        });

    justificativeList.addListSelectionListener(
        e -> {
          if (!e.getValueIsAdjusting()) {
            justificativeDebouncer.restart();
          }
        });

    var panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    var plannedLabel = new JLabel("Journaux planifiés");
    plannedLabel.setFont(plannedLabel.getFont().deriveFont(Font.BOLD));
    panel.add(plannedLabel);
    panel.add(new JScrollPane(plannedList));

    panel.add(Box.createVerticalStrut(20));

    var doneLabel = new JLabel("Journaux réalisés");
    doneLabel.setFont(doneLabel.getFont().deriveFont(Font.BOLD));
    panel.add(doneLabel);
    panel.add(new JScrollPane(doneList));

    panel.add(Box.createVerticalStrut(20));

    var justificativeLabel = new JLabel("Pièces justificatives");
    justificativeLabel.setFont(justificativeLabel.getFont().deriveFont(Font.BOLD));
    panel.add(justificativeLabel);
    panel.add(new JScrollPane(justificativeList));

    add(panel, BorderLayout.CENTER);
  }

  private Optional<String> getSelectedFileDriveId(File currentFile, FileCategory category) {
    GoogleLinkList<NamedID> ids = AppContext.getDefault().getData("named-ids");

    if (currentFile == null) {
      return Optional.empty();
    }

    var namedIds =
        switch (category) {
          case JUSTIFICATIVE -> ids.justificative();
          case PLANNED -> ids.planned();
          case DONE -> ids.done();
        };

    return namedIds.stream()
        .filter(
            driveNamedId -> {
              var filename =
                  currentFile
                      .getName()
                      .replace(TOUT_CAS_FILE_EXTENSION, "")
                      .replace(CAS_FILE_EXTENSION, "")
                      .replace(PJ_FILE_EXTENSION, "");
              return driveNamedId.name().equals(filename);
            })
        .findFirst()
        .map(NamedID::id);
  }

  public static List<File> getPatriLangPlannedFiles() {
    return Arrays.stream(
            requireNonNull(
                new File(GoogleLinkListDownloader.getPlannedDirectoryPath()).listFiles()))
        .filter(
            file ->
                file.getName().endsWith(TOUT_CAS_FILE_EXTENSION)
                    || file.getName().endsWith(CAS_FILE_EXTENSION))
        .toList();
  }

  public static List<File> getPatriLangDoneFiles() {
    return Arrays.stream(
            requireNonNull(new File(GoogleLinkListDownloader.getDoneDirectoryPath()).listFiles()))
        .filter(
            file ->
                file.getName().endsWith(TOUT_CAS_FILE_EXTENSION)
                    || file.getName().endsWith(CAS_FILE_EXTENSION))
        .toList();
  }

  public static List<File> getPatriLangJustificativeFiles() {
    return Arrays.stream(
            requireNonNull(
                new File(GoogleLinkListDownloader.getJustificativeDirectoryPath()).listFiles()))
        .filter(file -> file.getName().endsWith(PJ_FILE_EXTENSION))
        .toList();
  }

  public static File getPlannedCasSetFile() {
    return Arrays.stream(
            requireNonNull(
                new File(GoogleLinkListDownloader.getPlannedDirectoryPath()).listFiles()))
        .filter(file -> file.getName().endsWith(TOUT_CAS_FILE_EXTENSION))
        .findFirst()
        .orElseThrow();
  }

  public static File getDoneCasSetFile() {
    return Arrays.stream(
            requireNonNull(new File(GoogleLinkListDownloader.getDoneDirectoryPath()).listFiles()))
        .filter(file -> file.getName().endsWith(TOUT_CAS_FILE_EXTENSION))
        .findFirst()
        .orElseThrow();
  }

  public static List<File> getDonePatrilangFilesWithoutCasSet() {
    return getPatriLangDoneFiles().stream()
        .filter(file -> !file.getName().endsWith(TOUT_CAS_FILE_EXTENSION))
        .toList();
  }
}
