package school.hei.patrimoine.visualisation.swing.ihm;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.WEST;
import static java.awt.Toolkit.getDefaultToolkit;
import static java.util.stream.Collectors.joining;
import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.BoxLayout.Y_AXIS;

import java.awt.*;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import lombok.Getter;
import lombok.SneakyThrows;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.visualisation.swing.ihm.flux.FluxImpossiblesIHM;
import school.hei.patrimoine.visualisation.swing.ihm.flux.FluxJournaliersIHM;
import school.hei.patrimoine.visualisation.swing.ihm.selecteur.SelecteurGrapheConfIHM;
import school.hei.patrimoine.visualisation.swing.ihm.selecteur.SelecteurPatrimoineIHM;
import school.hei.patrimoine.visualisation.swing.ihm.selecteur.SelecteurPeriodeIHM;
import school.hei.patrimoine.visualisation.swing.modele.GrapheConfObservable;
import school.hei.patrimoine.visualisation.swing.modele.PatrimoinesVisualisables;

public class MainIHM extends JFrame implements Observer {
  @Getter private final PatrimoinesVisualisables patrimoinesVisualisables;
  private final GrapheConfObservable grapheConfObservable = new GrapheConfObservable();

  private final SelecteurPatrimoineIHM selecteurPatrimoineIHM;
  private final SelecteurPeriodeIHM selecteurPeriodeIHM;

  private final FluxImpossiblesIHM fluxImpossiblesIHM;
  private final FluxJournaliersIHM fluxJournaliersIHM;

  private final EvolutionPatrimoineSelectionnéIHM evolutionPatrimoineSelectionnéIHM;
  private final SelecteurGrapheConfIHM selecteurGrapheConfIHM;

  public MainIHM(List<Patrimoine> patrimoines) {
    this.patrimoinesVisualisables = new PatrimoinesVisualisables(patrimoines);
    this.patrimoinesVisualisables.addObserver(this);

    this.selecteurPatrimoineIHM =
        new SelecteurPatrimoineIHM(patrimoinesVisualisables, grapheConfObservable);

    this.fluxImpossiblesIHM = new FluxImpossiblesIHM(patrimoinesVisualisables);
    this.fluxJournaliersIHM = new FluxJournaliersIHM(patrimoinesVisualisables);

    this.selecteurPeriodeIHM = new SelecteurPeriodeIHM(patrimoinesVisualisables);
    this.evolutionPatrimoineSelectionnéIHM =
        new EvolutionPatrimoineSelectionnéIHM(patrimoinesVisualisables, grapheConfObservable);
    this.selecteurGrapheConfIHM = new SelecteurGrapheConfIHM(grapheConfObservable);

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
    westPanel.add(selecteurGrapheConfIHM);
    westPanel.add(selecteurPeriodeIHM);
    westPanel.add(fluxImpossiblesIHM);

    var fluxJournaliersBox = new BoxLayout(fluxJournaliersIHM, BoxLayout.X_AXIS);
    fluxJournaliersIHM.setLayout(fluxJournaliersBox);
    fluxJournaliersIHM.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 5));

    Dimension fixedWidth =
        new Dimension(
            fluxImpossiblesIHM.getPreferredSize().width,
            fluxJournaliersIHM.getPreferredSize().height);
    fluxJournaliersIHM.setPreferredSize(fixedWidth);
    fluxJournaliersIHM.setMaximumSize(fixedWidth);
    fluxJournaliersIHM.setMinimumSize(fixedWidth);

    westPanel.add(fluxJournaliersIHM);
    contentPane.add(westPanel, WEST);
    contentPane.add(evolutionPatrimoineSelectionnéIHM, CENTER);
  }

  @Override
  public void update(Observable o, Object arg) {
    setTitle();
  }

  private void setTitle() {
    var p = patrimoinesVisualisables.getEvolutionPatrimoine().getPatrimoine();
    setTitle(
        String.format(
            "Patrimoine : possesseur=%s, t=%s",
            p.possesseurs().stream().map(Personne::nom).collect(joining(",")), p.t()));
  }
}
