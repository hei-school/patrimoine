package school.hei.patrimoine.visualisation.swing.ihm.google.component;

import static java.util.Objects.requireNonNull;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;
import static school.hei.patrimoine.compiler.CompilerUtilities.DOWNLOADS_DIRECTORY_PATH;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.CAS_FILE_EXTENSION;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.TOUT_CAS_FILE_EXTENSION;

import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.swing.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkList;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.NamedID;

public class FileSideBar extends JList<File> {
  private final GoogleLinkList<NamedID> fileIds;

  public FileSideBar(GoogleLinkList<NamedID> fileIds, Runnable updateUICallback) {
    super(new FileListModel(getPatriLangFiles()));

    this.fileIds = fileIds;

    setSelectionMode(SINGLE_SELECTION);
    setCellRenderer(new FileListCellRenderer());

    addListSelectionListener(
        (e) -> {
          if (e.getValueIsAdjusting()) {
            return;
          }

          updateUICallback.run();
        });
  }

  public JScrollPane toScrollPane() {
    var scrollPane = new JScrollPane(this);
    scrollPane.setPreferredSize(new Dimension(200, 0));

    return scrollPane;
  }

  public Optional<File> getSelectedFile() {
    return Optional.ofNullable(getSelectedValue());
  }

  public Optional<String> getSelectedFileDriveId() {
    var currentFile = getSelectedFile().orElse(null);

    if (currentFile == null) {
      return Optional.empty();
    }

    return fileIds.driveLinkList().stream()
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

  private static List<File> getPatriLangFiles() {
    return Arrays.stream(requireNonNull(new File(DOWNLOADS_DIRECTORY_PATH).listFiles()))
        .filter(
            file ->
                file.getName().endsWith(TOUT_CAS_FILE_EXTENSION)
                    || file.getName().endsWith(CAS_FILE_EXTENSION))
        .toList();
  }

  public static File getCasSetFile() {
    return Arrays.stream(requireNonNull(new File(DOWNLOADS_DIRECTORY_PATH).listFiles()))
        .filter(file -> file.getName().endsWith(TOUT_CAS_FILE_EXTENSION))
        .findFirst()
        .orElseThrow();
  }
}
