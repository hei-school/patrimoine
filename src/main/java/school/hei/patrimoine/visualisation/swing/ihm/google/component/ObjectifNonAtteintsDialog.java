package school.hei.patrimoine.visualisation.swing.ihm.google.component;

import java.awt.*;
import java.util.Set;
import javax.swing.*;
import school.hei.patrimoine.modele.objectif.ObjectifNonAtteint;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;

public class ObjectifNonAtteintsDialog extends JDialog {
  public ObjectifNonAtteintsDialog(Set<ObjectifNonAtteint> objectifs) {
    setTitle("Objectifs Non Atteints");

    var listPanel = createListPanel();
    fillObjetifs(listPanel, objectifs);

    setupLayout(listPanel);
    finalizeDialog();
  }

  private JPanel createListPanel() {
    var panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setOpaque(false);
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    return panel;
  }

  private void fillObjetifs(JPanel panel, Set<ObjectifNonAtteint> objectifs) {
    objectifs.forEach(obj -> panel.add(new ObjectifItemPanel(obj)));
  }

  private void setupLayout(JPanel listPanel) {
    var scrollPane = new JScrollPane(listPanel);
    scrollPane.setBorder(
        CustomBorder.builder()
            .borderColor(new Color(166, 166, 166))
            .thickness(1)
            .radius(0)
            .padding(0, 0)
            .build());

    var closeBtn = new Button("Fermer");
    closeBtn.addActionListener(e -> dispose());

    var bottom = new JPanel();
    bottom.add(closeBtn);

    getContentPane().add(scrollPane, BorderLayout.CENTER);
    getContentPane().add(bottom, BorderLayout.SOUTH);
  }

  private void finalizeDialog() {
    setSize(700, 500);
    setLocationRelativeTo(null);
    setVisible(true);
  }
}
