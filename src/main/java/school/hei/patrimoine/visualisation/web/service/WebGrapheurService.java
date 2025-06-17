package school.hei.patrimoine.visualisation.web.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.springframework.stereotype.Service;
import school.hei.patrimoine.modele.evolution.EvolutionPatrimoine;
import school.hei.patrimoine.visualisation.xchart.GrapheConf;
import school.hei.patrimoine.visualisation.xchart.GrapheurEvolutionPatrimoine;

@Service
public class WebGrapheurService {
  private final GrapheurEvolutionPatrimoine grapheurEvolutionPatrimoine;

  public WebGrapheurService() {
    this.grapheurEvolutionPatrimoine = new GrapheurEvolutionPatrimoine();
  }

  public InputStream generateGraphe(
      EvolutionPatrimoine evolutionPatrimoine, GrapheConf grapheConf) {
    InputStream inputStream = null;
    try {
      inputStream =
          new FileInputStream(grapheurEvolutionPatrimoine.apply(evolutionPatrimoine, grapheConf));
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
    return inputStream;
  }
}
