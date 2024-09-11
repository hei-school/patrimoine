package school.hei.patrimoine.visualisation.swing.ihm.flux;

import static java.awt.Toolkit.getDefaultToolkit;
import static java.lang.Math.ceil;
import static java.lang.Math.sqrt;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.evolution.EvolutionPatrimoine;
import school.hei.patrimoine.visualisation.swing.ihm.EvolutionPatrimoineIHM;
import school.hei.patrimoine.visualisation.xchart.GrapheConf;

public class TousGrapheIHM extends JDialog {
  public TousGrapheIHM(
      List<Patrimoine> patrimoines, LocalDate debut, LocalDate fin, GrapheConf grapheConf) {
    super();

    pack();
    setSize(getDefaultToolkit().getScreenSize());
    setVisible(true);
    setResizable(false);
    setLocationRelativeTo(null);

    var contentPane = new JPanel();
    setContentPane(contentPane);
    var rowsCols = (int) ceil(sqrt(patrimoines.size()));
    contentPane.setLayout(new GridLayout(rowsCols, rowsCols));
    patrimoines.forEach(
        patrimoine ->
            contentPane.add(
                new EvolutionPatrimoineIHM(
                    () -> new EvolutionPatrimoine(patrimoine.nom(), patrimoine, debut, fin),
                    () -> grapheConf)));
  }
}
