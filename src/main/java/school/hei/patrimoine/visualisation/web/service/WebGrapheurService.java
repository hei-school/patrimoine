package school.hei.patrimoine.visualisation.web.service;

import org.springframework.stereotype.Service;
import school.hei.patrimoine.modele.evolution.EvolutionPatrimoine;
import school.hei.patrimoine.visualisation.xchart.GrapheConf;
import school.hei.patrimoine.visualisation.xchart.GrapheurEvolutionPatrimoine;

import java.io.File;
import java.util.function.Supplier;

@Service
public class WebGrapheurService {
  private final GrapheurEvolutionPatrimoine grapheurEvolutionPatrimoine;

  public WebGrapheurService() {
    this.grapheurEvolutionPatrimoine = new GrapheurEvolutionPatrimoine();
  }

  public File generateGraphe(EvolutionPatrimoine evolutionPatrimoine, GrapheConf grapheConf) {
    return grapheurEvolutionPatrimoine.apply(
        evolutionPatrimoine,
        grapheConf
    );
  }
}
