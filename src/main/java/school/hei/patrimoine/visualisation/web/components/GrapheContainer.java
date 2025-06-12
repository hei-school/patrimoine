package school.hei.patrimoine.visualisation.web.components;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import school.hei.patrimoine.visualisation.web.service.WebGrapheurService;
import school.hei.patrimoine.visualisation.web.states.GrapheConfState;
import school.hei.patrimoine.visualisation.web.states.PatrimoinesState;

public class GrapheContainer extends VerticalLayout {
  private final H3 label = new H3("Graphe de l'évolution patrimoine");

  public GrapheContainer(
      WebGrapheurService grapheurService,
      PatrimoinesState patrimoinesState,
      GrapheConfState grapheConfState
  ) {
    setWidthFull();
    setHeightFull();
    add(label, new GrapheWrapper(grapheurService, patrimoinesState, grapheConfState));
  }
}
