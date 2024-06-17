package school.hei.patrimoine.visualisation.swing.ihm;

import lombok.Getter;
import lombok.SneakyThrows;
import school.hei.patrimoine.ResourceFileGetter;
import school.hei.patrimoine.modele.EvolutionPatrimoine;
import school.hei.patrimoine.visualisation.swing.modele.EvolutionPatrimoineObservable;

import javax.swing.*;
import java.awt.*;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.WEST;
import static java.awt.EventQueue.invokeLater;
import static java.awt.Toolkit.getDefaultToolkit;
import static java.time.LocalDate.now;

public class MainIHM extends JFrame {
  @Getter
  private final EvolutionPatrimoineObservable evolutionPatrimoineObservable;
  private final SelecteurPatrimoineIHM selecteurPatrimoineIHM;
  private final SpecifieurPatrimoineIHM specifieurPatrimoineIHM;
  private final GrapheurEvolutionPatrimoineIHM grapheurEvolutionPatrimoineIHM;

  public MainIHM() {
    this.selecteurPatrimoineIHM = new SelecteurPatrimoineIHM();
    this.evolutionPatrimoineObservable = new EvolutionPatrimoineObservable();
    this.evolutionPatrimoineObservable.setEvolutionPatrimoine(getEvolutionPatrimoine());

    this.specifieurPatrimoineIHM = new SpecifieurPatrimoineIHM(evolutionPatrimoineObservable);
    this.grapheurEvolutionPatrimoineIHM = new GrapheurEvolutionPatrimoineIHM(evolutionPatrimoineObservable);

    configureFrame();
    configureContentPane();
  }

  public static void main(String[] args) {
    invokeLater(MainIHM::new);
  }

  @SneakyThrows
  private void configureFrame() {
    setTitle("Patrimoine - " + evolutionPatrimoineObservable.getEvolutionPatrimoine().getPatrimoine().possesseur().nom());
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    pack();
    setSize(getDefaultToolkit().getScreenSize());
    var coinFile = new ResourceFileGetter().apply("coin-dollar-2686.png");
    setIconImage(new ImageIcon(coinFile.getAbsolutePath()).getImage());
    setResizable(true);
    setVisible(true);
    setLocationRelativeTo(null);
  }

  private void configureContentPane() {
    var contentPane = new JPanel();
    setContentPane(contentPane);
    contentPane.setLayout(new BorderLayout(0, 0));

    contentPane.add(specifieurPatrimoineIHM, WEST);
    contentPane.add(grapheurEvolutionPatrimoineIHM, CENTER);
  }

  private EvolutionPatrimoine getEvolutionPatrimoine() {
    var debut = now();
    return new EvolutionPatrimoine(
        "",
        selecteurPatrimoineIHM.get(),
        debut,
        debut.plusDays(30));
  }
}
