package school.hei.patrimoine.visualisation.swing.ihm.google.component.files;

import static java.util.Objects.requireNonNull;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.CAS_FILE_EXTENSION;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.TOUT_CAS_FILE_EXTENSION;
import static school.hei.patrimoine.visualisation.swing.ihm.google.downloader.DriveNamedIdDownloader.DOWNLOAD_DONE_FILE_DIRECTORY_PATH;
import static school.hei.patrimoine.visualisation.swing.ihm.google.downloader.DriveNamedIdDownloader.DOWNLOAD_PLANNED_FILE_DIRECTORY_PATH;

import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.swing.*;

import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkList;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.NamedID;

public class FileSideBar extends JPanel {
    private final JList<File> plannedList;
    private final JList<File> doneList;
    private State state;

    public FileSideBar(Runnable stateHandler) {
        super(new BorderLayout());

        this.state = new State(null, null);
        this.plannedList = new JList<>(new FileListModel(getPatriLangPlannedFiles()));
        this.doneList = new JList<>(new FileListModel(getPatriLangDoneFiles()));

        plannedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        doneList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        plannedList.setCellRenderer(new FileListCellRenderer());
        doneList.setCellRenderer(new FileListCellRenderer());

        plannedList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                var selected = plannedList.getSelectedValue();
                if (selected != null) {
                    state = new State(selected, true);
                    doneList.clearSelection();
                    stateHandler.run();
                }
            }
        });

        doneList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                var selected = doneList.getSelectedValue();
                if (selected != null) {
                    state = new State(selected, false);
                    plannedList.clearSelection();
                    stateHandler.run();
                }
            }
        });

        var panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        var plannedLabel = new JLabel("Planned files");
        plannedLabel.setFont(plannedLabel.getFont().deriveFont(Font.BOLD));
        panel.add(plannedLabel);
        panel.add(new JScrollPane(plannedList));

        panel.add(Box.createVerticalStrut(20));

        var doneLabel = new JLabel("Done files");
        doneLabel.setFont(doneLabel.getFont().deriveFont(Font.BOLD));
        panel.add(doneLabel);
        panel.add(new JScrollPane(doneList));

        add(panel, BorderLayout.CENTER);
    }

    public Optional<File> getSelectedFile() {
        return Optional.ofNullable(state.currentFile());
    }

    public Optional<String> getSelectedFileDriveId() {
        GoogleLinkList<NamedID> ids = AppContext.getDefault().getData("named-ids");

        var currentFile = state.currentFile();
        if(currentFile == null){
            return Optional.empty();
        }

        var namedIds  = state.isPlanned() ? ids.planned() : ids.done();
        return namedIds.stream()
                .filter(driveNamedId -> {
                    var filename = currentFile
                            .getName()
                            .replace(TOUT_CAS_FILE_EXTENSION, "")
                            .replace(CAS_FILE_EXTENSION, "");
                    return driveNamedId.name().equals(filename);
                })
                .findFirst()
                .map(NamedID::id);
    }

    private static List<File> getPatriLangPlannedFiles() {
        return Arrays.stream(requireNonNull(new File(DOWNLOAD_PLANNED_FILE_DIRECTORY_PATH).listFiles()))
                .filter(file -> file.getName().endsWith(TOUT_CAS_FILE_EXTENSION) || file.getName().endsWith(CAS_FILE_EXTENSION))
                .toList();
    }

    private static List<File> getPatriLangDoneFiles() {
        return Arrays.stream(requireNonNull(new File(DOWNLOAD_DONE_FILE_DIRECTORY_PATH).listFiles()))
                .filter(file -> file.getName().endsWith(TOUT_CAS_FILE_EXTENSION) || file.getName().endsWith(CAS_FILE_EXTENSION))
                .toList();
    }

    public static File getCasSetFile() {
        return Arrays.stream(requireNonNull(new File(DOWNLOAD_PLANNED_FILE_DIRECTORY_PATH).listFiles())).filter(file -> file.getName().endsWith(TOUT_CAS_FILE_EXTENSION)).findFirst().orElseThrow();
    }

    private record State(File currentFile, Boolean isPlanned){}
}