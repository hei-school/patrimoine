package school.hei.patrimoine.visualisation.web.components;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.util.Observable;
import java.util.Observer;
import school.hei.patrimoine.visualisation.web.service.WebGrapheurService;
import school.hei.patrimoine.visualisation.web.states.GrapheConfState;
import school.hei.patrimoine.visualisation.web.states.PatrimoinesState;

public class GrapheContainer extends VerticalLayout implements Observer {
  private final H3 label = new H3("Graphe de l'évolution patrimoine");
  private GrapheWrapper grapheWrapper;
  private final PatrimoinesState patrimoinesState;

  public GrapheContainer(
      WebGrapheurService grapheurService,
      PatrimoinesState patrimoinesState,
      GrapheConfState grapheConfState) {
    setWidthFull();
    setHeightFull();
    patrimoinesState.addObserver(this);
    grapheConfState.addObserver(this);
    this.patrimoinesState = patrimoinesState;
    this.grapheWrapper = new GrapheWrapper(grapheurService, grapheConfState.getGrapheConf());

    add(label, grapheWrapper);
  }

  @Override
  public void update(Observable observable, Object o) {
    grapheWrapper.loadImage(patrimoinesState.getEvolutionPatrimoine());
  }
}
