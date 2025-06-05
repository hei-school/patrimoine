package school.hei.patrimoine.visualisation.web.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;
import school.hei.patrimoine.modele.evolution.EvolutionPatrimoine;
import school.hei.patrimoine.visualisation.web.service.WebGrapheurService;
import school.hei.patrimoine.visualisation.web.states.PatrimoinesState;
import school.hei.patrimoine.visualisation.xchart.GrapheConf;

import java.util.Observable;
import java.util.Observer;

public class GrapheWrapper extends Div implements Observer {
  private final Image imageComponent;
  private final WebGrapheurService grapheurService;
  private final PatrimoinesState patrimoinesState;

  public GrapheWrapper(WebGrapheurService webGrapheurService, PatrimoinesState patrimoinesState) {
    this.grapheurService = webGrapheurService;
    this.patrimoinesState = patrimoinesState;
    this.patrimoinesState.addObserver(this);
    this.imageComponent = new Image();
    this.imageComponent.setHeight("100%");
    this.imageComponent.setWidth("100%");
    imageComponent.setAlt("GrapheImage");
    setClassName("graphe-wrapper");
    add(this.imageComponent);
  }

  private void loadImage(EvolutionPatrimoine evolutionPatrimoine) {
      StreamResource ressource = new StreamResource(
        "graphe-image.png",
          () -> grapheurService.generateGraphe(evolutionPatrimoine, new GrapheConf(true, true, true, true, true))
      );
      this.imageComponent.setSrc(ressource);
      this.imageComponent.setAlt("Image should be loaded");
  }

  @Override
  public void update(Observable observable, Object o) {
    this.loadImage(this.patrimoinesState.getEvolutionPatrimoine());
  }
}
