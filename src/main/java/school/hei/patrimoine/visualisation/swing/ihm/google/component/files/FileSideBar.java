package school.hei.patrimoine.visualisation.swing.ihm.google.component.files;

import static java.util.Objects.requireNonNull;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.CAS_FILE_EXTENSION;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.TOUT_CAS_FILE_EXTENSION;

import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.swing.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.Debouncer;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkList;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkList.NamedID;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkListDownloader;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;

public class FileSideBar extends JPanel {
  private final State state;
  private final JList<File> plannedList;
  private final JList<File> doneList;

  public FileSideBar(State state) {
    super(new BorderLayout());

    this.state = state;
    this.plannedList = new JList<>(new FileListModel(getPatriLangPlannedFiles()));
    this.doneList = new JList<>(new FileListModel(getPatriLangDoneFiles()));

    plannedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    doneList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    plannedList.setCellRenderer(new FileListCellRenderer());
    doneList.setCellRenderer(new FileListCellRenderer());

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
                      getSelectedFileDriveId(selectedFile, true).orElse(""),
                      "isPlannedSelectedFile",
                      true));
              doneList.clearSelection();
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
                      getSelectedFileDriveId(selectedFile, false).orElse(""),
                      "isPlannedSelectedFile",
                      false));
              plannedList.clearSelection();
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

    add(panel, BorderLayout.CENTER);
  }

  private Optional<String> getSelectedFileDriveId(File currentFile, boolean isPlannedSelectedFile) {
    GoogleLinkList<NamedID> ids = AppContext.getDefault().getData("named-ids");

    if (currentFile == null) {
      return Optional.empty();
    }

    var namedIds = isPlannedSelectedFile ? ids.planned() : ids.done();
    return namedIds.stream()
        .filter(
            driveNamedId -> {
              var filename =
                  currentFile
                      .getName()
                      .replace(TOUT_CAS_FILE_EXTENSION, "")
                      .replace(CAS_FILE_EXTENSION, "");
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
