package school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin;

import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.PatriLangStagingFileManager.getStagedDoneFiles;
import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.PatriLangStagingFileManager.getStagedPlannedFiles;

import java.awt.*;
import java.io.File;
import java.util.List;
import javax.swing.*;
import lombok.Getter;

public class SyncConfirmDialog extends JDialog {
  @Getter private boolean confirmed = false;

  public SyncConfirmDialog() {
    super((Frame) null, "Confirmer la synchronisation", true);

    List<File> plannedFiles = getStagedPlannedFiles();
    List<File> doneFiles = getStagedDoneFiles();

    boolean hasFiles = !plannedFiles.isEmpty() || !doneFiles.isEmpty();

    if (hasFiles) {
      initConfirmComponents(plannedFiles, doneFiles);
    } else {
      initInfoComponents();
    }

    pack();
    setLocationRelativeTo(null);
  }

  private void initConfirmComponents(List<File> plannedFiles, List<File> doneFiles) {
    setLayout(new BorderLayout(10, 10));

    JPanel messagePanel = new JPanel(new BorderLayout(5, 5));
    messagePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

    String message = formatStagedFilesMessage(plannedFiles, doneFiles);
    JTextArea textArea = new JTextArea(message);
    textArea.setEditable(false);
    textArea.setBackground(messagePanel.getBackground());
    textArea.setFont(UIManager.getFont("Label.font"));

    messagePanel.add(textArea, BorderLayout.CENTER);

    JLabel questionLabel = new JLabel("Voulez-vous synchroniser ces fichiers avec Google Drive ?");
    questionLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
    messagePanel.add(questionLabel, BorderLayout.SOUTH);

    add(messagePanel, BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

    JButton cancelButton = new JButton("Annuler");
    cancelButton.addActionListener(
        e -> {
          confirmed = false;
          dispose();
        });

    JButton confirmButton = new JButton("Synchroniser");
    confirmButton.addActionListener(
        e -> {
          confirmed = true;
          dispose();
        });
    confirmButton.setPreferredSize(new Dimension(120, cancelButton.getPreferredSize().height));

    buttonPanel.add(cancelButton);
    buttonPanel.add(confirmButton);

    add(buttonPanel, BorderLayout.SOUTH);

    setMinimumSize(new Dimension(400, 200));
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
  }

  private void initInfoComponents() {
    setLayout(new BorderLayout(10, 10));

    JPanel messagePanel = new JPanel(new BorderLayout(5, 5));
    messagePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

    JLabel messageLabel = new JLabel("Aucun fichier en attente de synchronisation.");
    messageLabel.setFont(UIManager.getFont("Label.font"));
    messagePanel.add(messageLabel, BorderLayout.CENTER);

    add(messagePanel, BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

    JButton okButton = new JButton("OK");
    okButton.addActionListener(
        e -> {
          confirmed = false;
          dispose();
        });
    okButton.setPreferredSize(new Dimension(100, okButton.getPreferredSize().height));

    buttonPanel.add(okButton);

    add(buttonPanel, BorderLayout.SOUTH);

    setMinimumSize(new Dimension(350, 120));
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
  }

  private static String formatStagedFilesMessage(List<File> planned, List<File> done) {
    var sb = new StringBuilder("Fichiers en attente de synchronisation :\n\n");

    if (!planned.isEmpty()) {
      sb.append("Planifiés :\n");
      planned.forEach(f -> sb.append("  • ").append(f.getName()).append("\n"));
      sb.append("\n");
    }

    if (!done.isEmpty()) {
      sb.append("Réalisés :\n");
      done.forEach(f -> sb.append("  • ").append(f.getName()).append("\n"));
    }

    return sb.toString();
  }

  public static boolean showDialog() {
    SyncConfirmDialog dialog = new SyncConfirmDialog();
    dialog.setVisible(true);
    return dialog.isConfirmed();
  }
}
