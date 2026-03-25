package school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin;

import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.files.PatriLangFileContentManager.getAllModifiedFiles;

import java.awt.*;
import javax.swing.*;
import lombok.Getter;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.CustomBorder;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;

@Getter
public class UnsavedChangesConfirmDialog extends Dialog {

  static final String FILE_ICON_PATH = "/icons/file.png";

  private boolean confirmed = false;

  public UnsavedChangesConfirmDialog(String actionLabel) {
    super("Modifications non sauvegardées", 600, 400, false);

    setLayout(new BorderLayout());
    setResizable(false);

    addContentPanel(this, actionLabel);
    addButtons();

    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private static void addContentPanel(Dialog parent, String actionLabel) {
    var messagePanel = new JPanel(new BorderLayout(15, 0));
    messagePanel.setBorder(CustomBorder.builder().thickness(0).padding(10, 15).build());

    var contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

    var sectionTitle = new JLabel("Fichiers modifiés non sauvegardés");
    sectionTitle.setFont(new Font("Arial", Font.BOLD, 15));
    sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
    sectionTitle.setBorder(CustomBorder.builder().thickness(0).padding(0, 0, 3, 0).build());
    contentPanel.add(sectionTitle);

    var icon = getFileIcon();
    for (var fileInput : getAllModifiedFiles()) {
      var filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
      filePanel.setBorder(
          CustomBorder.builder()
              .radius(20)
              .borderColor(new Color(180, 180, 180))
              .padding(10, 0)
              .thickness(1)
              .build());
      filePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
      filePanel.add(new JLabel(icon));
      filePanel.add(new JLabel(fileInput.file().getBaseFileName()));
      contentPanel.add(filePanel);
      contentPanel.add(Box.createVerticalStrut(5));
    }

    var scrollPane = new JScrollPane(contentPanel);
    scrollPane.setBorder(CustomBorder.builder().thickness(0).padding(0, 0).build());
    scrollPane.setPreferredSize(new Dimension(500, 250));
    messagePanel.add(scrollPane, BorderLayout.CENTER);

    var questionLabel =
        new JLabel(
            "<html>Si vous continuez vers <b>\""
                + actionLabel
                + "\"</b>, ces modifications ne seront pas prises en compte.</html>");
    questionLabel.setBorder(CustomBorder.builder().thickness(0).padding(10, 0, 0, 0).build());
    messagePanel.add(questionLabel, BorderLayout.SOUTH);

    parent.add(messagePanel, BorderLayout.CENTER);
  }

  private void addButtons() {
    var buttonPanel = new JPanel(new BorderLayout());
    buttonPanel.setBorder(CustomBorder.builder().thickness(0).padding(5, 0, 10, 15).build());
    var resterButton =
        new Button(
            "Rester",
            e -> {
              confirmed = false;
              dispose();
            });

    var continuerButton =
        new Button(
            "Continuer quand même",
            e -> {
              confirmed = true;
              dispose();
            });

    var rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
    rightPanel.add(resterButton);
    rightPanel.add(Box.createRigidArea(new Dimension(10, 0)));
    rightPanel.add(continuerButton);
    buttonPanel.add(rightPanel, BorderLayout.EAST);

    add(buttonPanel, BorderLayout.SOUTH);
  }

  private static ImageIcon getFileIcon() {
    var url = UnsavedChangesConfirmDialog.class.getResource(FILE_ICON_PATH);
    assert url != null;
    var icon = new ImageIcon(url);
    var scaled = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
    return new ImageIcon(scaled);
  }
}
