package school.hei.patrimoine.visualisation.web.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;
import java.util.Observable;
import java.util.Observer;
import school.hei.patrimoine.modele.evolution.EvolutionPatrimoine;
import school.hei.patrimoine.visualisation.web.service.WebGrapheurService;
import school.hei.patrimoine.visualisation.xchart.GrapheConf;

public class GrapheWrapper extends Div implements Observer {
  private final Image imageComponent;
  private final WebGrapheurService grapheurService;
  private EvolutionPatrimoine evolutionPatrimoine;
  private final GrapheConf grapheConf;

  public GrapheWrapper(WebGrapheurService webGrapheurService, GrapheConf grapheConf) {
    this.grapheurService = webGrapheurService;
    this.grapheConf = grapheConf;
    this.evolutionPatrimoine = null;
    this.imageComponent = new Image();
    this.imageComponent.setClassName("graphe");
    this.imageComponent.setHeight("100%");
    this.imageComponent.setWidth("100%");
    this.imageComponent.setAlt("Selectionez un patrimoine pour visualiser");
    setClassName("graphe-wrapper");
    add(this.imageComponent);
  }

  public GrapheWrapper(
      WebGrapheurService webGrapheurService,
      GrapheConf grapheConf,
      EvolutionPatrimoine evolutionPatrimoine) {
    this.grapheurService = webGrapheurService;
    this.grapheConf = grapheConf;
    this.evolutionPatrimoine = evolutionPatrimoine;
    this.imageComponent = new Image();
    this.imageComponent.setClassName("graphe");
    this.imageComponent.setHeight("100%");
    this.imageComponent.setWidth("100%");
    this.imageComponent.setAlt("Selectionez un patrimoine pour visualiser");
    setClassName("graphe-wrapper");
    loadImage(evolutionPatrimoine);
    add(this.imageComponent);
  }

  public void loadImage(EvolutionPatrimoine evolutionPatrimoine) {
    StreamResource ressource =
        new StreamResource(
            "graphe-image.png",
            () -> grapheurService.generateGraphe(evolutionPatrimoine, grapheConf));
    this.imageComponent.setSrc(ressource);
    this.evolutionPatrimoine = evolutionPatrimoine;
  }

  @Override
  public void update(Observable observable, Object o) {
    this.loadImage(evolutionPatrimoine);
  }
}
