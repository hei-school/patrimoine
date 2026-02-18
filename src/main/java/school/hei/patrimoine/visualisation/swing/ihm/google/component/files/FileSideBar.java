package school.hei.patrimoine.visualisation.swing.ihm.google.component.files;

import static school.hei.patrimoine.patrilang.PatriLangTranspiler.*;
import static school.hei.patrimoine.visualisation.swing.ihm.google.config.EnvironmentConfig.isOfflineMode;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.FileCategory.*;
import static school.hei.patrimoine.visualisation.swing.ihm.google.providers.FilesProvider.*;

import java.awt.*;
import java.io.File;
import java.util.Map;
import java.util.Optional;
import javax.swing.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkList.NamedID;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.FileCategory;

public class FileSideBar extends JPanel {
  private final State state;
  private final JList<File> plannedList;
  private final JList<File> doneList;

  public FileSideBar(State state) {
    super(new BorderLayout());

    this.state = state;
    this.doneList = new JList<>(new FileListModel(getPatriLangDoneFiles()));
    this.plannedList = new JList<>(new FileListModel(getPatriLangPlannedFiles()));

    doneList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    plannedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    doneList.setCellRenderer(new FileListCellRenderer());
    plannedList.setCellRenderer(new FileListCellRenderer());

    var plannedDebouncer =
        new Debouncer(
            () -> {
              var selectedFile = plannedList.getSelectedValue();
              if (selectedFile == null) return;
              if (isOfflineMode()) {
                this.state.update(
                    Map.of(
                        "selectedFile",
                        selectedFile,
                        "selectedFileCasSet",
                        getPlannedCasSetFile(),
                        "isPlannedSelectedFile",
                        true));
              } else {
                this.state.update(
                    Map.of(
                        "selectedFile",
                        selectedFile,
                        "selectedFileCasSet",
                        getPlannedCasSetFile(),
                        "selectedFileId",
                        getSelectedFileDriveId(selectedFile, PLANNED).orElse(""),
                        "isPlannedSelectedFile",
                        true));
              }
              doneList.clearSelection();
            });

    var doneDebouncer =
        new Debouncer(
            () -> {
              var selectedFile = doneList.getSelectedValue();
              if (selectedFile == null) return;
              if (isOfflineMode()) {
                this.state.update(
                    Map.of(
                        "selectedFile",
                        selectedFile,
                        "selectedFileCasSet",
                        getDoneCasSetFile(),
                        "isPlannedSelectedFile",
                        false));
              } else {
                this.state.update(
                    Map.of(
                        "selectedFile",
                        selectedFile,
                        "selectedFileCasSet",
                        getDoneCasSetFile(),
                        "selectedFileId",
                        getSelectedFileDriveId(selectedFile, DONE).orElse(""),
                        "isPlannedSelectedFile",
                        false));
              }
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

  private Optional<String> getSelectedFileDriveId(File currentFile, FileCategory category) {
    GoogleLinkList<NamedID> ids = AppContext.getDefault().getData("named-ids");

    if (currentFile == null) {
      return Optional.empty();
    }

    var namedIds =
        switch (category) {
          case DONE -> ids.done();
          case PLANNED -> ids.planned();
          case JUSTIFICATIVE -> ids.justificative();
        };

    return namedIds.stream()
        .filter(
            driveNamedId -> {
              var filename =
                  currentFile
                      .getName()
                      .replace(PJ_FILE_EXTENSION, "")
                      .replace(CAS_FILE_EXTENSION, "")
                      .replace(TOUT_CAS_FILE_EXTENSION, "");
              return driveNamedId.name().equals(filename);
            })
        .findFirst()
        .map(NamedID::id);
  }
}
