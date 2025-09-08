package school.hei.patrimoine.visualisation.swing.ihm.google.component.files;

import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.transpileToutCas;

import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.function.Supplier;
import javax.swing.*;
import lombok.Builder;
import lombok.Getter;
import school.hei.patrimoine.cas.CasSetAnalyzer;
import school.hei.patrimoine.google.api.DriveApi;
import school.hei.patrimoine.google.model.User;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.ImageComponent;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.NavigateButton;
import school.hei.patrimoine.visualisation.swing.ihm.google.pages.RecoupementPage;

@Getter
public class AppBar extends JPanel {
  private ViewMode currentMode;
  private int controlledFontSize;
  private final Runnable stateHandler;
  private final Conf conf;
  private final Supplier<String> currentFileContentSupplier;
  private final Supplier<String> currentFileIdSupplier;
  private final Supplier<File> currentFileSupplier;

  public AppBar(
      Supplier<String> currentFileContentSupplier,
      Supplier<String> currentFileIdSupplier,
      Supplier<File> currentFileSupplier,
      Runnable stateHandler) {
    this(
        currentFileIdSupplier,
        currentFileContentSupplier,
        currentFileSupplier,
        stateHandler,
        new Conf(true, true, true, true, true, List.of()));
  }

  public AppBar(
      Supplier<String> currentFileIdSupplier,
      Supplier<String> currentFileContentSupplier,
      Supplier<File> currentFileSupplier,
      Runnable stateHandler,
      Conf conf) {
    super(new BorderLayout());

    this.conf = conf;
    this.controlledFontSize = 14;
    this.currentMode = ViewMode.VIEW;
    this.stateHandler = stateHandler;
    this.currentFileSupplier = currentFileSupplier;
    this.currentFileIdSupplier = currentFileIdSupplier;
    this.currentFileContentSupplier = currentFileContentSupplier;

    add(leftControls(), BorderLayout.WEST);
    add(rightControls(), BorderLayout.EAST);
  }

  private JPanel leftControls() {
    var leftControls = new JPanel(new FlowLayout(FlowLayout.LEFT));
    conf.customComponents().forEach(leftControls::add);

    if (conf.withModeSelect()) {
      var modeSelect = new JComboBox<>(ViewMode.values());
      modeSelect.setSelectedItem(currentMode);
      modeSelect.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
      modeSelect.setCursor(new Cursor(Cursor.HAND_CURSOR));

      modeSelect.addActionListener(
          e -> {
            currentMode = (ViewMode) modeSelect.getSelectedItem();
            stateHandler.run();
          });

      leftControls.add(modeSelect);
    }

    if (conf.withSaveButton()) {
      var saveButton = new Button("Enregistrement local");
      saveButton.addActionListener(e -> saveSelectedFile());

      leftControls.add(saveButton);
    }

    if (conf.withSyncButton()) {
      var syncButton = new Button("Synchroniser avec Drive");
      leftControls.add(syncButton);
      syncButton.addActionListener(e -> syncSelectedFileWithDrive());
    }

    if (conf.withGraphicButton()) {
      var graphicButton = new Button("Évolution graphique");
      graphicButton.addActionListener(
          e ->
              invokeLater(
                  () ->
                      new CasSetAnalyzer(DISPOSE_ON_CLOSE)
                          .accept(
                              transpileToutCas(
                                  FileSideBar.getPlannedCasSetFile().getAbsolutePath()))));

      leftControls.add(graphicButton);
    }

    if (conf.withRecoupementButton()) {
      var recoupementButton = new NavigateButton("Recoupement", RecoupementPage.PAGE_NAME);
      leftControls.add(recoupementButton);
    }

    return leftControls;
  }

  private void saveSelectedFile() {
    if (currentMode != ViewMode.EDIT) {
      showMessageDialog(
          this,
          "Vous devez être en mode édition pour sauvegarder.",
          "Erreur",
          JOptionPane.ERROR_MESSAGE);
      return;
    }

    var currentFile = currentFileSupplier.get();
    if (currentFile == null) {
      showMessageDialog(
          this,
          "Veuillez sélectionner un fichier avant de sauvegarder.",
          "Erreur",
          JOptionPane.ERROR_MESSAGE);
      return;
    }

    try {
      var content = currentFileContentSupplier.get();
      Files.writeString(currentFile.toPath(), content);

      showMessageDialog(
          this,
          "Le fichier a été enregistré avec succès.\n"
              + "Vous pouvez maintenant le synchroniser avec Google Drive.",
          "Succès",
          JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception e) {
      showMessageDialog(
          this,
          "Une erreur est survenue lors de l'enregistrement : " + e.getMessage(),
          "Erreur",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void syncSelectedFileWithDrive() {
    var currentFile = currentFileSupplier.get();
    if (currentFile == null) {
      JOptionPane.showMessageDialog(
          this,
          "Veuillez sélectionner un fichier avant de synchroniser avec Drive.",
          "Erreur",
          JOptionPane.ERROR_MESSAGE);
      return;
    }

    int confirm =
        JOptionPane.showConfirmDialog(
            this,
            "Vous devez d'abord sauvegarder votre fichier localement avant de le synchroniser avec"
                + " Drive.\n"
                + "Avez-vous sauvegardé les modifications ?",
            "Vérification sauvegarde",
            JOptionPane.YES_NO_OPTION);

    if (confirm != JOptionPane.YES_OPTION) {
      return;
    }

    var loadingDialog = new Dialog("Synchronisation avec Drive...", 350, 120);
    SwingWorker<Void, Void> worker =
        new SwingWorker<>() {
          @Override
          protected Void doInBackground() throws Exception {
            var fileId = currentFileIdSupplier.get();
            var mimeType = "application/octet-stream";
            DriveApi driveApi = AppContext.getDefault().getData("drive-api");

            driveApi.update(fileId, mimeType, currentFile);

            return null;
          }

          @Override
          protected void done() {
            loadingDialog.dispose();
            try {
              get();
              showMessageDialog(
                  AppBar.this,
                  "Le fichier a été synchronisé avec succès sur Google Drive.",
                  "Succès",
                  JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
              showMessageDialog(
                  AppBar.this,
                  "Échec de la synchronisation : " + e.getMessage(),
                  "Erreur",
                  JOptionPane.ERROR_MESSAGE);
            }
          }
        };

    worker.execute();
    loadingDialog.setVisible(true);
  }

  private JPanel rightControls() {
    var increaseFontButton = new Button("+");
    var decreaseFontButton = new Button("-");

    var fontSizeField = new JTextField(String.valueOf(controlledFontSize), 3);
    fontSizeField.setBorder(
        BorderFactory.createCompoundBorder(
            fontSizeField.getBorder(), BorderFactory.createEmptyBorder(8, 6, 8, 6)));

    increaseFontButton.addActionListener(e -> adjustControlledFontSize(1, fontSizeField));
    decreaseFontButton.addActionListener(e -> adjustControlledFontSize(-1, fontSizeField));
    fontSizeField.addActionListener(
        e -> {
          try {
            controlledFontSize = Integer.parseInt(fontSizeField.getText());
            controlledFontSize = Math.max(8, controlledFontSize);
          } catch (NumberFormatException ex) {
            controlledFontSize = 14;
          }

          fontSizeField.setText(String.valueOf(controlledFontSize));
          stateHandler.run();
        });

    var rightControls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    rightControls.add(decreaseFontButton);
    rightControls.add(fontSizeField);
    rightControls.add(increaseFontButton);
    rightControls.add(createUserInfoPanel());

    return rightControls;
  }

  private JPanel createUserInfoPanel() {
    User user = AppContext.getDefault().getData("connected-user");

    var panel = new JPanel(new BorderLayout());
    panel.setOpaque(false);
    panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

    var avatarLabel = new ImageComponent(user.avatarUrl(), user.displayName(), 40, true);
    var avatarPanel = new JPanel(new BorderLayout());
    avatarPanel.setOpaque(false);
    avatarPanel.add(avatarLabel, BorderLayout.CENTER);
    avatarPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); // espace à droite

    var nameLabel = new JLabel(user.displayName());
    nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 14f));

    var emailLabel = new JLabel(user.email());
    emailLabel.setFont(emailLabel.getFont().deriveFont(Font.PLAIN, 12f));
    emailLabel.setForeground(Color.GRAY);

    var textPanel = new JPanel();
    textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
    textPanel.setOpaque(false);
    textPanel.add(nameLabel);
    textPanel.add(Box.createRigidArea(new Dimension(0, 2)));
    textPanel.add(emailLabel);

    panel.add(avatarPanel, BorderLayout.WEST);
    panel.add(textPanel, BorderLayout.CENTER);

    return panel;
  }

  private void adjustControlledFontSize(int delta, JTextField fontSizeField) {
    controlledFontSize = Math.max(8, controlledFontSize + delta);

    fontSizeField.setText(String.valueOf(controlledFontSize));

    stateHandler.run();
  }

  @Getter
  public enum ViewMode {
    VIEW("Affichage"),
    EDIT("Édition");

    private final String label;

    ViewMode(String label) {
      this.label = label;
    }

    @Override
    public String toString() {
      return label;
    }
  }

  @Builder
  public record Conf(
      boolean withSyncButton,
      boolean withGraphicButton,
      boolean withSaveButton,
      boolean withRecoupementButton,
      boolean withModeSelect,
      List<Component> customComponents) {}
}
