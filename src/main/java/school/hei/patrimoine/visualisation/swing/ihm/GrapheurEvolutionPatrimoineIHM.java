package school.hei.patrimoine.visualisation.swing.ihm;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import javax.swing.*;
import lombok.SneakyThrows;
import school.hei.patrimoine.visualisation.swing.modele.GrapheConfObservable;
import school.hei.patrimoine.visualisation.swing.modele.PatrimoinesVisualisables;
import school.hei.patrimoine.visualisation.xchart.GrapheurEvolutionPatrimoine;

public class GrapheurEvolutionPatrimoineIHM extends JPanel implements Observer {
  private final PatrimoinesVisualisables patrimoinesVisualisables;
  private final GrapheConfObservable grapheConfObservable;

  private final GrapheurEvolutionPatrimoine grapheurEvolutionPatrimoine;

  public GrapheurEvolutionPatrimoineIHM(
      PatrimoinesVisualisables patrimoinesVisualisables,
      GrapheConfObservable grapheConfObservable) {
    super();
    this.grapheurEvolutionPatrimoine = new GrapheurEvolutionPatrimoine();

    this.patrimoinesVisualisables = patrimoinesVisualisables;
    this.patrimoinesVisualisables.addObserver(this);

    this.grapheConfObservable = grapheConfObservable;
    this.grapheConfObservable.addObserver(this);
  }

  @Override
  public void update(Observable o, Object arg) {
    this.repaint();
  }

  @SneakyThrows
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    var grapheFile =
        grapheurEvolutionPatrimoine.apply(
            patrimoinesVisualisables.getEvolutionPatrimoine(),
            grapheConfObservable.getGrapheConf());
    var grapheImage = ImageIO.read(grapheFile);
    g.drawImage(grapheImage, 0, 0, this.getWidth(), this.getHeight(), this);
  }
}
