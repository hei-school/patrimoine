package school.hei.patrimoine.visualisation.swing.ihm.selecteur;

import static java.awt.FlowLayout.LEFT;

import java.awt.*;
import javax.swing.*;
import school.hei.patrimoine.visualisation.swing.ihm.FixedSizer;
import school.hei.patrimoine.visualisation.swing.modele.PatrimoinesVisualisables;

public class SelecteurPatrimoineIHM extends JPanel {

  public SelecteurPatrimoineIHM(PatrimoinesVisualisables patrimoinesVisualisables) {
    super(new FlowLayout(LEFT));

    new FixedSizer().accept(this, new Dimension(500, 30));

    var description = new JLabel("Patrimoine");
    this.add(description);

    String[] nomsPatrimoines = patrimoinesVisualisables.noms().toArray(new String[0]);
    var selecteur = new JComboBox(nomsPatrimoines);
    selecteur.setSelectedIndex(0);
    selecteur.addActionListener(
        e ->
            patrimoinesVisualisables.selectionne(
                ((JComboBox) e.getSource()).getSelectedItem().toString()));
    this.add(selecteur);
  }
}
