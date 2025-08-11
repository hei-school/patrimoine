package school.hei.patrimoine.visualisation.swing.ihm.google;

import static java.util.Objects.requireNonNull;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;
import static school.hei.patrimoine.compiler.CompilerUtilities.DOWNLOADS_DIRECTORY_PATH;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.CAS_FILE_EXTENSION;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.TOUT_CAS_FILE_EXTENSION;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import school.hei.patrimoine.google.DriveApi;
import school.hei.patrimoine.google.exception.GoogleIntegrationException;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkList;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.NamedID;
import school.hei.patrimoine.visualisation.swing.ihm.google.utils.MarkdownToHtmlConverter;

public class PatriLangViewerScreen extends Screen {
  private static final int DEFAULT_FONT_SIZE = 14;

  private final DriveApi driveApi;
  private final GoogleLinkList<NamedID> ids;

  private final List<File> files;
  private final MarkdownToHtmlConverter markdownToHtmlConverter;

  private int fontSize;
  private File currentFile;
  private ViewMode currentMode;
  private JEditorPane htmlViewer;

  public PatriLangViewerScreen(GoogleLinkList<NamedID> ids, DriveApi driveApi) {
    super("PatriLang Viewer", 1_300, 800);

    this.ids = ids;
    this.driveApi = driveApi;

    this.files = getPatriLangFiles();
    this.markdownToHtmlConverter = new MarkdownToHtmlConverter();

    this.fontSize = DEFAULT_FONT_SIZE;
    this.currentMode = ViewMode.VIEW;

    setLayout(new BorderLayout());

    add(createTopPanel(), BorderLayout.NORTH);
    add(createMainSplitPane(), BorderLayout.CENTER);
  }

  private JPanel createTopPanel() {
    var modeSelect = new JComboBox<>(ViewMode.values());
    var saveButton = new Button("Save");
    var graphicButton = new Button("Graphics");
    var syncButton = new Button("Synchronize with Drive");

    modeSelect.setBackground(Theme.MAIN_COLOR);
    modeSelect.setForeground(Color.WHITE);
    modeSelect.setBorder(
        BorderFactory.createCompoundBorder(
            modeSelect.getBorder(), BorderFactory.createEmptyBorder(6, 8, 6, 8)));

    modeSelect.addActionListener(e -> toggleMode((ViewMode) modeSelect.getSelectedItem()));
    saveButton.addActionListener(e -> saveToFile());
    syncButton.addActionListener(e -> syncCurrentFile());
    saveButton.setEnabled(currentMode == ViewMode.EDIT);

    var topPanel = new JPanel(new BorderLayout());
    var leftControls = new JPanel(new FlowLayout(FlowLayout.LEFT));
    leftControls.add(modeSelect);
    leftControls.add(saveButton);
    leftControls.add(graphicButton);
    leftControls.add(syncButton);

    var rightControls = getRightJPanel();

    topPanel.add(leftControls, BorderLayout.WEST);
    topPanel.add(rightControls, BorderLayout.EAST);

    modeSelect.addActionListener(
        (ActionEvent e) -> {
          var selected = (ViewMode) modeSelect.getSelectedItem();
          toggleMode(selected);
          saveButton.setEnabled(selected == ViewMode.EDIT);
        });

    return topPanel;
  }

  private String getCurrentFileDriveId() {
    if (currentFile == null || !currentFile.exists()) {
      return "";
    }

    return ids.driveLinkList().stream()
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
        .map(NamedID::id)
        .orElse("");
  }

  private void syncCurrentFile() {
    var driveFileId = getCurrentFileDriveId();
    if (driveFileId.isEmpty()) {
      showMessageDialog(
          this, "Aucun fichier sélectionné ou fichier inexistant.", "Erreur", ERROR_MESSAGE);
      return;
    }

    var loadingDialog = new Dialog(this, "Synchronisation en cours...", 300, 100);

    SwingWorker<Void, Void> worker =
        new SwingWorker<>() {
          protected Void doInBackground() {
            try {
              driveApi.update(driveFileId, "text/plain", currentFile);
            } catch (GoogleIntegrationException e) {
              throw new RuntimeException(e);
            }
            return null;
          }

          @Override
          protected void done() {
            loadingDialog.dispose();
            try {
              get();
              showMessageDialog(PatriLangViewerScreen.this, "Synchronisation réussie !");
            } catch (Exception e) {
              showMessageDialog(
                  PatriLangViewerScreen.this, e.getCause().getMessage(), "Erreur", ERROR_MESSAGE);
            }
          }
        };

    worker.execute();
    loadingDialog.setVisible(true);
  }

  private JPanel getRightJPanel() {
    var increaseFontButton = new Button("+");
    var decreaseFontButton = new Button("-");
    var fontSizeField = new JTextField(String.valueOf(fontSize), 3);
    fontSizeField.setBorder(
        BorderFactory.createCompoundBorder(
            fontSizeField.getBorder(), BorderFactory.createEmptyBorder(8, 6, 8, 6)));

    increaseFontButton.addActionListener(e -> adjustFontSize(1, fontSizeField));
    decreaseFontButton.addActionListener(e -> adjustFontSize(-1, fontSizeField));
    fontSizeField.addActionListener(
        e -> {
          fontSize = Integer.parseInt(fontSizeField.getText());
          updateViewerText();
        });

    var rightControls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    rightControls.add(decreaseFontButton);
    rightControls.add(fontSizeField);
    rightControls.add(increaseFontButton);
    return rightControls;
  }

  private void adjustFontSize(int delta, JTextField fontSizeField) {
    fontSize += delta;
    if (fontSize < 1) fontSize = 1;
    fontSizeField.setText(String.valueOf(fontSize));
    updateViewerText();
  }

  private JSplitPane createMainSplitPane() {
    var fileList = new JList<>(new FileListModel(files));
    fileList.setCellRenderer(new FileListCellRenderer());
    fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    var leftScrollPane = new JScrollPane(fileList);
    leftScrollPane.setPreferredSize(new Dimension(200, 0));

    htmlViewer = new JEditorPane();
    htmlViewer.setContentType("text/html");
    htmlViewer.setEditable(false);
    htmlViewer.setText(
        "<html><body style='background-color:#fff8dc;'>Sélectionner un fichier pour"
            + " l'afficher.</body></html>");
    var centerScrollPane = new JScrollPane(htmlViewer);

    var rightPanel = new JPanel(new BorderLayout());
    rightPanel.add(new JLabel(""), BorderLayout.CENTER);
    fileList.addListSelectionListener(e -> onFileSelected(e, fileList));

    var horizontalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    horizontalSplit.setLeftComponent(leftScrollPane);
    horizontalSplit.setRightComponent(
        new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, centerScrollPane, rightPanel));
    horizontalSplit.setDividerLocation(200);
    ((JSplitPane) horizontalSplit.getRightComponent()).setDividerLocation(700);

    return horizontalSplit;
  }

  private void toggleMode(ViewMode selectedMode) {
    this.currentMode = selectedMode;
    updateViewerText();
  }

  private void onFileSelected(ListSelectionEvent e, JList<File> fileList) {
    if (e.getValueIsAdjusting()) {
      return;
    }

    currentFile = fileList.getSelectedValue();
    updateViewerText();
  }

  private void updateViewerText() {
    if (currentFile == null || !currentFile.exists()) {
      htmlViewer.setText(
          "<html><body style='background-color:#fff8dc;'>Sélectionner un fichier pour"
              + " l'afficher.</body></html>");
    }

    try {
      var content = Files.readString(currentFile.toPath());
      htmlViewer.setFont(new Font(Font.MONOSPACED, Font.PLAIN, fontSize));

      if (currentMode == ViewMode.EDIT) {
        htmlViewer.setContentType("text/plain");
        htmlViewer.setEditable(true);
        htmlViewer.setBackground(new Color(255, 248, 220));
        htmlViewer.setText(content);
      } else {
        var html = markdownToHtmlConverter.apply(content);
        html = html.replace("<body>", "<body style='font-size: " + fontSize + "px;'>");
        html = html.replaceAll("<code>", "<code style='font-size: " + fontSize + "px;'>");
        htmlViewer.setContentType("text/html");
        htmlViewer.setEditable(false);
        htmlViewer.setBackground(Color.WHITE);
        htmlViewer.setText(html);
      }
      htmlViewer.setCaretPosition(0);
    } catch (Exception ex) {
      htmlViewer.setText(
          "<html><body style='color:red;'>Erreur lors de la lecture du fichier.</body></html>");
    }
  }

  private void saveToFile() {
    if (currentMode != ViewMode.EDIT || currentFile == null) {
      return;
    }

    try (FileWriter writer = new FileWriter(currentFile)) {
      writer.write(htmlViewer.getText());
      showMessageDialog(this, "Fichier sauvegardé avec succès.");
    } catch (Exception e) {
      showMessageDialog(
          this,
          "Erreur lors de la sauvegarde du fichier: " + e.getMessage(),
          "Erreur",
          ERROR_MESSAGE);
    }
  }

  private static List<File> getPatriLangFiles() {
    return Arrays.stream(requireNonNull(new File(DOWNLOADS_DIRECTORY_PATH).listFiles()))
        .filter(
            file ->
                file.getName().endsWith(TOUT_CAS_FILE_EXTENSION)
                    || file.getName().endsWith(CAS_FILE_EXTENSION))
        .toList();
  }

  private enum ViewMode {
    VIEW,
    EDIT
  }
}
