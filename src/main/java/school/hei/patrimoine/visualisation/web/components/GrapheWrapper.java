package school.hei.patrimoine.visualisation.web.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;
import java.util.Observable;
import java.util.Observer;
import school.hei.patrimoine.modele.evolution.EvolutionPatrimoine;
import school.hei.patrimoine.visualisation.web.service.WebGrapheurService;
import school.hei.patrimoine.visualisation.web.states.GrapheConfState;
import school.hei.patrimoine.visualisation.web.states.PatrimoinesState;

public class GrapheWrapper extends Div implements Observer {
  private final Image imageComponent;
  private final WebGrapheurService grapheurService;
  private final PatrimoinesState patrimoinesState;
  private final GrapheConfState grapheConfState;

  public GrapheWrapper(
      WebGrapheurService webGrapheurService,
      PatrimoinesState patrimoinesState,
      GrapheConfState grapheConfState) {
    this.grapheurService = webGrapheurService;
    this.patrimoinesState = patrimoinesState;
    this.grapheConfState = grapheConfState;
    this.grapheConfState.addObserver(this);
    this.patrimoinesState.addObserver(this);
    this.imageComponent = new Image();
    this.imageComponent.setClassName("graphe");
    this.imageComponent.setHeight("100%");
    this.imageComponent.setWidth("100%");
    this.imageComponent.setAlt("Selectionez un patrimoine pour visualiser");
    setClassName("graphe-wrapper");
    add(this.imageComponent);
  }

  private void loadImage(EvolutionPatrimoine evolutionPatrimoine) {
    StreamResource ressource =
        new StreamResource(
            "graphe-image.png",
            () ->
                grapheurService.generateGraphe(
                    evolutionPatrimoine, grapheConfState.getGrapheConf()));
    this.imageComponent.setSrc(ressource);
  }

  @Override
  public void update(Observable observable, Object o) {
    this.loadImage(this.patrimoinesState.getEvolutionPatrimoine());
  }
}
