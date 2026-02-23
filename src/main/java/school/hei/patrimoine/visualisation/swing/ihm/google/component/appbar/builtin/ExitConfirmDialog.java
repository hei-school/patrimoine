package school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin;

import static school.hei.patrimoine.visualisation.swing.ihm.google.component.appbar.builtin.SyncConfirmDialog.addContentPanel;

import java.awt.*;
import javax.swing.*;
import lombok.Getter;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;

public class ExitConfirmDialog extends Dialog {
  @Getter private boolean confirmed;

  public ExitConfirmDialog() {
    super("Modifications non synchronisées", 800, 500, false);

    setLayout(new BorderLayout());
    setResizable(false);

    addContentPanel(
        this, "Il reste des modifications non synchronisées. Voulez-vous vraiment quitter ?");

    addButtons();
    setVisible(true);
  }

  private void addButtons() {
    var buttonPanel = new JPanel(new BorderLayout());

    var cancelButton =
        new school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button(
            "Annuler",
            e -> {
              confirmed = false;
              dispose();
            });

    var confirmButton =
        new Button(
            "Quitter",
            e -> {
              confirmed = true;
              dispose();
            });

    var rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
    rightPanel.add(cancelButton);
    rightPanel.add(Box.createRigidArea(new Dimension(10, 0)));
    rightPanel.add(confirmButton);
    buttonPanel.add(rightPanel, BorderLayout.EAST);

    add(buttonPanel, BorderLayout.SOUTH);
  }
}
