package school.hei.patrimoine.visualisation.swing.ihm.google.modele.files;

import java.awt.*;
import javax.swing.*;
import lombok.Getter;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;

public class RestoreConfirmDialog extends Dialog {
  @Getter private boolean confirmed = false;

  public RestoreConfirmDialog() {
    super("Confirmer la restauration", 400, 150, false);

    setLayout(new BorderLayout());
    setResizable(false);

    addContentPanel();
    addButtons();

    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private void addContentPanel() {
    var label =
        new JLabel(
            "Voulez-vous revenir au dernier état sauvegardé ? Toutes les modifications non"
                + " sauvegardées seront perdues.");
    label.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
    add(label, BorderLayout.CENTER);
  }

  private void addButtons() {
    var buttonPanel = new JPanel(new BorderLayout());

    var cancelButton =
        new Button(
            "Annuler",
            e -> {
              confirmed = false;
              dispose();
            });

    var confirmButton =
        new Button(
            "Restaurer",
            e -> {
              confirmed = true;
              dispose();
            });

    var rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
    rightPanel.add(cancelButton);
    rightPanel.add(confirmButton);
    buttonPanel.add(rightPanel, BorderLayout.EAST);

    add(buttonPanel, BorderLayout.SOUTH);
  }
}
