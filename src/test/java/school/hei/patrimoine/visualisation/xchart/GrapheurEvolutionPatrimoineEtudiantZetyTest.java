package school.hei.patrimoine.visualisation.xchart;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.ResourceFileGetter;
import school.hei.patrimoine.cas.PatrimoineEtudiantZetyCas;
import school.hei.patrimoine.modele.EvolutionPatrimoine;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.visualisation.AreImagesEqual;
import school.hei.patrimoine.visualisation.xchart.GrapheurEvolutionPatrimoine;

class GrapheurEvolutionPatrimoineEtudiantZetyTest {
  private final GrapheurEvolutionPatrimoine grapheurEvolutionPatrimoine = new GrapheurEvolutionPatrimoine();
  private final AreImagesEqual areImagesEqual = new AreImagesEqual();
  private final ResourceFileGetter resourceFileGetter = new ResourceFileGetter();
  private PatrimoineEtudiantZetyCas zetyCas;
  private Patrimoine patrimoine;

  @Test
  void testGraphVisualization() {
    var evolutionPatrimoine = new EvolutionPatrimoine(
        "Zety étudie en 2023-2024",
        patrimoine,
        LocalDate.of(2024, 7, 3),
        LocalDate.of(2024, 9, 17)
    );

    File imageGeneree = grapheurEvolutionPatrimoine.apply(evolutionPatrimoine);

    assertTrue(areImagesEqual.apply(
        resourceFileGetter.apply("patrimoine-etudiant-zety.png"),
        imageGeneree
    ));
  }
}