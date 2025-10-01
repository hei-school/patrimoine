package school.hei.patrimoine.visualisation.swing.ihm.google.component;

import static school.hei.patrimoine.patrilang.files.PatriLangFileWritter.FileWritterInput;
import static school.hei.patrimoine.visualisation.swing.ihm.google.utils.MessageDialog.*;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.function.Supplier;
import javax.swing.*;
import lombok.Getter;
import school.hei.patrimoine.google.api.DriveApi;
import school.hei.patrimoine.google.model.User;
import school.hei.patrimoine.patrilang.files.PatriLangFileWritter;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;
import school.hei.patrimoine.visualisation.swing.ihm.google.utils.AsyncTask;

@Getter
public class AppBar extends JPanel {
  private final List<Component> leftComponents;
  private final List<Component> rightComponents;

  public AppBar(List<Component> leftComponents, List<Component> rightComponents) {
    super(new BorderLayout());

    this.leftComponents = leftComponents;
    this.rightComponents = rightComponents;

    add(leftControls(), BorderLayout.WEST);
    add(rightControls(), BorderLayout.EAST);
  }

  private JPanel leftControls() {
    var leftControls = new JPanel(new FlowLayout(FlowLayout.LEFT));
    leftComponents.forEach(leftControls::add);

    return leftControls;
  }

  private JPanel rightControls() {
    var rightControls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    rightComponents.forEach(rightControls::add);

    return rightControls;
  }

  private static void saveSelectedFile(State state, String content) {
    ViewMode currentMode = state.get("viewMode");
    if (!ViewMode.EDIT.equals(currentMode)) {
      showError("Erreur", "Vous devez être en mode édition pour sauvegarder.");
      return;
    }

    File currentFile = state.get("selectedFile");
    if (currentFile == null) {
      showError("Erreur", "Veuillez sélectionner un fichier avant de sauvegarder.");
      return;
    }

    AsyncTask.<Void>builder()
        .loadingMessage("Validation et sauvegarde du fichier...")
        .task(
            () -> {
              new PatriLangFileWritter()
                  .write(
                      FileWritterInput.builder()
                          .casSet(state.get("selectedCasSetFile"))
                          .file(currentFile)
                          .content(content)
                          .build());
              return null;
            })
        .onSuccess(
            result -> {
              AppContext.getDefault().globalState().update("newUpdate", true);
              showInfo("Succès", "Vous pouvez maintenant le synchroniser avec Google Drive.");
            })
        .onError(
            error -> {
              if (showExceptionMessageIfRecognizedException(error)) {
                return;
              }
              showError("Erreur", "Une erreur est survenue lors de l'enregistrement");
            })
        .build()
        .execute();
  }

  private static void syncSelectedFileWithDrive(State state) {
    File currentFile = state.get("selectedFile");
    if (currentFile == null) {
      showError("Erreur", "Veuillez sélectionner un fichier avant de synchroniser avec Drive.");
      return;
    }

    int confirm =
        JOptionPane.showConfirmDialog(
            AppContext.getDefault().app(),
            "Vous devez d'abord sauvegarder votre fichier localement avant de le synchroniser avec"
                + " Drive.\n"
                + "Avez-vous sauvegardé les modifications ?",
            "Vérification sauvegarde",
            JOptionPane.YES_NO_OPTION);

    if (confirm != JOptionPane.YES_OPTION) {
      return;
    }

    AsyncTask.<Void>builder()
        .task(
            () -> {
              String mimeType = "application/octet-stream";
              String fileId = state.get("selectedFileId");
              DriveApi driveApi = AppContext.getDefault().getData("drive-api");

              driveApi.update(fileId, mimeType, currentFile);
              return null;
            })
        .loadingMessage("Synchronisation avec Drive...")
        .onSuccess(
            result ->
                showInfo("Succès", "Le fichier a été synchronisé avec succès sur Google Drive."))
        .onError(error -> showError("Erreur", "Échec de la synchronisation"))
        .build()
        .execute();
  }

  private static void adjustControlledFontSize(State state, int delta, JTextField fontSizeField) {
    state.update("fontSize", Math.max(8, (int) state.get("fontSize") + delta));
    fontSizeField.setText(String.valueOf((int) state.get("fontSize")));
  }

  public static JComboBox<ViewMode> builtInViewModeSelect(State state) {
    var modeSelect = new JComboBox<>(ViewMode.values());
    modeSelect.setSelectedItem(state.get("viewMode"));
    modeSelect.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    modeSelect.setCursor(new Cursor(Cursor.HAND_CURSOR));
    modeSelect.addActionListener(e -> state.update("viewMode", modeSelect.getSelectedItem()));

    return modeSelect;
  }

  public static Button builtInSaveButton(
      State state, Supplier<String> currentFileNewContentSupplier) {
    var saveButton = new Button("Enregistrement local");
    saveButton.addActionListener(e -> saveSelectedFile(state, currentFileNewContentSupplier.get()));

    return saveButton;
  }

  public static Button builtInSyncButton(State state) {
    var saveButton = new Button("Synchroniser avec Drive");
    saveButton.addActionListener(e -> syncSelectedFileWithDrive(state));

    return saveButton;
  }

  public static JPanel builtInFontSizeControllerButton(State state) {
    var increaseFontButton = new Button("+");
    var decreaseFontButton = new Button("-");

    var fontSizeField = new JTextField(String.valueOf((int) state.get("fontSize")), 3);
    fontSizeField.setBorder(
        BorderFactory.createCompoundBorder(
            fontSizeField.getBorder(), BorderFactory.createEmptyBorder(8, 6, 8, 6)));

    increaseFontButton.addActionListener(e -> adjustControlledFontSize(state, 1, fontSizeField));
    decreaseFontButton.addActionListener(e -> adjustControlledFontSize(state, -1, fontSizeField));
    fontSizeField.addActionListener(
        e -> {
          try {
            var newFontSize = Integer.parseInt(fontSizeField.getText());
            state.update("fontSize", Math.max(8, newFontSize));
          } catch (NumberFormatException ex) {
            state.update("fontSize", 14);
          }

          fontSizeField.setText(String.valueOf((int) state.get("fontSize")));
        });

    var fontSizeControllerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    fontSizeControllerPanel.add(decreaseFontButton);
    fontSizeControllerPanel.add(fontSizeField);
    fontSizeControllerPanel.add(increaseFontButton);

    return fontSizeControllerPanel;
  }

  public static JPanel builtInUserInfoPanel() {
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
}
