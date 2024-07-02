package school.hei.patrimoine.visualisation.swing.ihm;

import school.hei.patrimoine.visualisation.swing.modele.PatrimoinesVisualisables;

import javax.swing.*;
import java.awt.*;

import static java.awt.FlowLayout.LEFT;

public class SelecteurPatrimoineIHM extends JPanel {
  private final PatrimoinesVisualisables patrimoinesVisualisables;

  public SelecteurPatrimoineIHM(PatrimoinesVisualisables patrimoinesVisualisables) {
    super(new FlowLayout(LEFT));
    this.patrimoinesVisualisables = patrimoinesVisualisables;

    configureStyle();

    var description = new JLabel("Patrimoine");
    this.add(description);

    String[] nomsPatrimoines = patrimoinesVisualisables.noms().toArray(new String[0]);
    var selecteur = new JComboBox(nomsPatrimoines);
    selecteur.setSelectedIndex(0);
    selecteur.addActionListener(e -> patrimoinesVisualisables
        .selectionne(((JComboBox) e.getSource()).getSelectedItem().toString()));
    this.add(selecteur);
  }

  private void configureStyle() {
    var size = new Dimension(500, 30);
    this.setSize(size);
    this.setMinimumSize(size);
    this.setMaximumSize(size);
    this.setPreferredSize(size);
  }
}
