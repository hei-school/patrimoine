package school.hei.patrimoine.visualisation.swing.modele;

import java.util.Observable;
import lombok.Getter;
import school.hei.patrimoine.visualisation.xchart.GrapheConf;

public class GrapheConfObservable extends Observable {
  @Getter private GrapheConf grapheConf = new GrapheConf(true, true, true, true);

  public GrapheConfObservable() {
    super();
  }

  public void setGrapheConf(GrapheConf grapheConf) {
    this.grapheConf = grapheConf;
    change();
  }

  private void change() {
    setChanged();
    notifyObservers();
  }
}
