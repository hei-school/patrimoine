package school.hei.patrimoine.visualisation.web.states;

import lombok.Getter;
import school.hei.patrimoine.visualisation.xchart.GrapheConf;

import java.util.Set;

@Getter
public final class GrapheConfState extends State {
  private GrapheConf grapheConf = new GrapheConf(false, true, true, true, true);

  public GrapheConfState() {
    super();
  }

  public void setGrapheConf(Set<String> conf) {
    var confBuilder = GrapheConf.builder();
    if(conf.contains("Agrégat")) {
      confBuilder.avecAgregat(true);
    }
    if(conf.contains("Trésorerie")) {
      confBuilder.avecTresorerie(true);
    }
    if(conf.contains("Immobilisations")) {
      confBuilder.avecImmobilisations(true);
    }
    if(conf.contains("Obligations")) {
      confBuilder.avecObligations(true);
    }

    this.grapheConf = confBuilder.build();
    change();
  }
}
