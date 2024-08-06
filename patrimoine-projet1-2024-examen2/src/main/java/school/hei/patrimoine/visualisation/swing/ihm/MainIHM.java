package school.hei.patrimoine.visualisation.swing.ihm;

import lombok.Getter;
import lombok.SneakyThrows;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.visualisation.swing.modele.PatrimoinesVisualisables;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.WEST;
import static java.awt.Toolkit.getDefaultToolkit;
import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.BoxLayout.Y_AXIS;

public class MainIHM extends JFrame implements Observer {
  @Getter
  private final PatrimoinesVisualisables patrimoinesVisualisables;
  private final SelecteurPatrimoineIHM selecteurPatrimoineIHM;
  private final SelecteurPeriodeIHM selecteurPeriodeIHM;
  private final GrapheurEvolutionPatrimoineIHM grapheurEvolutionPatrimoineIHM;

  public MainIHM(List<Patrimoine> patrimoines) {
    this.patrimoinesVisualisables = new PatrimoinesVisualisables(patrimoines);
    patrimoinesVisualisables.addObserver(this);

    this.selecteurPatrimoineIHM = new SelecteurPatrimoineIHM(patrimoinesVisualisables);

    this.selecteurPeriodeIHM = new SelecteurPeriodeIHM(patrimoinesVisualisables);
    this.grapheurEvolutionPatrimoineIHM = new GrapheurEvolutionPatrimoineIHM(patrimoinesVisualisables);

    configureFrame();
    configureContentPane();
  }

  @SneakyThrows
  private void configureFrame() {
    setTitle();
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    pack();
    setSize(getDefaultToolkit().getScreenSize());
    setResizable(true);
    setVisible(true);
    setLocationRelativeTo(null);
  }

  private void configureContentPane() {
    var contentPane = new JPanel();
    setContentPane(contentPane);
    contentPane.setLayout(new BorderLayout(0, 0));

    var westPanel = new JPanel();
    westPanel.setLayout(new BoxLayout(westPanel, Y_AXIS));
    westPanel.setAlignmentY(TOP_ALIGNMENT);
    var westMargin = 5;
    westPanel.setBorder(createEmptyBorder(westMargin, westMargin, westMargin, westMargin));
    westPanel.add(selecteurPatrimoineIHM);
    westPanel.add(selecteurPeriodeIHM);
    contentPane.add(westPanel, WEST);

    contentPane.add(grapheurEvolutionPatrimoineIHM, CENTER);
  }

  @Override
  public void update(Observable o, Object arg) {
    setTitle();
  }

  private void setTitle() {
    setTitle("Patrimoine - " + patrimoinesVisualisables.getEvolutionPatrimoine().getPatrimoine().possesseur().nom());
  }
}
