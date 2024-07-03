package school.hei.patrimoine.visualisation.xchart;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.ResourceFileGetter;
import school.hei.patrimoine.cas.PatrimoineRicheMoyenCas;
import school.hei.patrimoine.cas.PatrimoineRichePireCas;
import school.hei.patrimoine.modele.EvolutionPatrimoine;
import school.hei.patrimoine.visualisation.AreImagesEqual;

import java.time.LocalDate;

import static java.time.Month.MARCH;
import static java.time.Month.MAY;
import static java.time.Month.NOVEMBER;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GrapheurEvolutionPatrimoinePatrimoineRicheTest {
  private final GrapheurEvolutionPatrimoine grapheurEvolutionPatrimoine = new GrapheurEvolutionPatrimoine();
  private final AreImagesEqual areImagesEqual = new AreImagesEqual();
  private final ResourceFileGetter resourceFileGetter = new ResourceFileGetter();

  @Test
  void visualise_riche_pire_sur_quelques_annees() {
    var patrimoine = new EvolutionPatrimoine(
        "Dummy",
        new PatrimoineRichePireCas().get(),
        LocalDate.of(2024, MAY, 12),
        LocalDate.of(2026, NOVEMBER, 5));

    var imageGeneree = grapheurEvolutionPatrimoine.apply(patrimoine);

    assertTrue(areImagesEqual.apply(
        resourceFileGetter.apply("patrimoine-riche-pire-sur-quelques-annees.png"),
        imageGeneree));
  }

  @Test
  void visualise_riche_moyen_sur_quelques_annees() {
    var patrimoine = new EvolutionPatrimoine(
        "Dummy",
        new PatrimoineRicheMoyenCas().get(),
        LocalDate.of(2024, MAY, 12),
        LocalDate.of(2026, MARCH, 5));

    var imageGeneree = grapheurEvolutionPatrimoine.apply(patrimoine);

    assertTrue(areImagesEqual.apply(
        resourceFileGetter.apply("patrimoine-riche-moyen-sur-quelques-annees.png"),
        imageGeneree));
  }
}