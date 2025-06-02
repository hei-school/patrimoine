package school.hei.patrimoine.visualisation.web.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class GrapheContainer extends VerticalLayout {
  public GrapheContainer() {
    setWidthFull();
    setHeightFull();
    var label = new H3("Graphe de l'évolution patrimoine");
    var grapheWrapper = new Div();
    grapheWrapper.setClassName("graphe-wrapper");
    add(label, grapheWrapper);
  }
}
