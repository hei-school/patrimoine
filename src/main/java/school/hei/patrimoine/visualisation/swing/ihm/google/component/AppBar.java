package school.hei.patrimoine.visualisation.swing.ihm.google.component;

import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.transpileToutCas;
import static school.hei.patrimoine.visualisation.swing.ihm.google.PatriLangViewerScreen.ViewMode;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;

import lombok.Getter;
import school.hei.patrimoine.cas.CasSetAnalyzer;
import school.hei.patrimoine.google.api.DriveApi;
import school.hei.patrimoine.google.model.User;

@Getter
public class AppBar extends JPanel {
  private ViewMode currentMode;
  private int controlledFontSize;
  private final Runnable updateUICallback;
  private final FileSideBar fileSideBar;
  private final HtmlViewer htmlViewer;
  private final DriveApi driveApi;

  public AppBar(JFrame owner, User currentUser, DriveApi driveApi, FileSideBar fileSideBar, HtmlViewer htmlViewer, Runnable updateUICallback) {
    super(new BorderLayout());

    this.driveApi = driveApi;
    this.controlledFontSize = 14;
    this.htmlViewer = htmlViewer;
    this.fileSideBar = fileSideBar;
    this.currentMode = ViewMode.VIEW;
    this.updateUICallback = updateUICallback;

    add(leftControls(owner), BorderLayout.WEST);
    add(rightControls(), BorderLayout.CENTER);
    add(createUserInfoPanel(currentUser), BorderLayout.EAST);
  }

  private JPanel leftControls(JFrame owner) {
    var modeSelect = new JComboBox<>(ViewMode.values());
    var saveButton = new Button("Save");
    var graphicButton = new Button("Graphics");
    var syncButton = new Button("Synchronize with Drive");

    var leftControls = new JPanel(new FlowLayout(FlowLayout.LEFT));
    leftControls.add(modeSelect);
    leftControls.add(saveButton);
    leftControls.add(graphicButton);
    leftControls.add(syncButton);

    graphicButton.addActionListener(e -> {
        invokeLater(() -> new CasSetAnalyzer(DISPOSE_ON_CLOSE).accept(transpileToutCas(FileSideBar.getCasSetFile().getAbsolutePath())));
    });

    modeSelect.addActionListener(
        e -> {
          currentMode = (ViewMode) modeSelect.getSelectedItem();
          updateUICallback.run();
        });
    modeSelect.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    modeSelect.setCursor(new Cursor(Cursor.HAND_CURSOR));

      saveButton.addActionListener(e -> saveSelectedFile(owner));
  syncButton.addActionListener(e -> syncSelectedFileWithDrive(owner));
    return leftControls;
  }

    private void saveSelectedFile(JFrame owner) {
        if (currentMode != ViewMode.EDIT) {
            showMessageDialog(this, "Vous devez être en mode édition pour sauvegarder.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        var optFile = fileSideBar.getSelectedFile();
        if (optFile.isEmpty()) {
            showMessageDialog(this, "Aucun fichier sélectionné.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        var dialog = new Dialog(owner, "Traitement...", 300, 100);

        SwingWorker<Void, Void> worker =
                new SwingWorker<>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        var currentFile = optFile.get();
                        Files.writeString(currentFile.toPath(), htmlViewer.getText());
                        return null;
                    }

                    @Override
                    protected void done() {
                        dialog.dispose();
                        try {
                            get();
                            showMessageDialog(AppBar.this, "Sauvegarde locale réussie !");
                            updateUICallback.run();
                        } catch (Exception e) {
                            showMessageDialog(AppBar.this, "Erreur lors de la sauvegarde : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                };
        worker.execute();
        dialog.setVisible(true);
    }

    private void syncSelectedFileWithDrive(JFrame owner) {
        var optFile = fileSideBar.getSelectedFile();
        var optDriveId = fileSideBar.getSelectedFileDriveId();

        if (optFile.isEmpty()) {
            showMessageDialog(this, "Aucun fichier local sélectionné.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (optDriveId.isEmpty()) {
            showMessageDialog(this, "Ce fichier n'est pas lié à un fichier Drive.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        var dialog = new Dialog(owner, "Traitement...", 300, 100);

        SwingWorker<Void, Void> worker =
                new SwingWorker<>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        driveApi.update(optDriveId.get(), "text/plain", optFile.get());
                        return null;
                    }

                    @Override
                    protected void done() {
                        dialog.dispose();
                        try {
                            get();
                            showMessageDialog(AppBar.this, "Synchronisation réussie avec Google Drive !");
                            updateUICallback.run();
                        } catch (Exception e) {
                            showMessageDialog(AppBar.this, "Erreur lors de la synchronisation : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                };

        worker.execute();
        dialog.setVisible(true);
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
            if (controlledFontSize < 1) {
              controlledFontSize = 1;
            }
          } catch (NumberFormatException ex) {
            controlledFontSize = 14;
          }

          fontSizeField.setText(String.valueOf(controlledFontSize));
          updateUICallback.run();
        });

    var rightControls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    rightControls.add(decreaseFontButton);
    rightControls.add(fontSizeField);
    rightControls.add(increaseFontButton);

    return rightControls;
  }

    private JPanel createUserInfoPanel(User user) {
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

        panel.add(avatarPanel, BorderLayout.WEST); // on ajoute le panel avec padding
        panel.add(textPanel, BorderLayout.CENTER);

        return panel;
    }

    private void adjustControlledFontSize(int delta, JTextField fontSizeField) {
    controlledFontSize += delta;

    if (controlledFontSize < 1) {
      controlledFontSize = 1;
    }

    fontSizeField.setText(String.valueOf(controlledFontSize));
    updateUICallback.run();
  }
}
