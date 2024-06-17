package school.hei.patrimoine.visualisation.xchart;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.ResourceFileGetter;
import school.hei.patrimoine.cas.PatrimoineEtudiantPireCas;
import school.hei.patrimoine.modele.EvolutionPatrimoine;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.visualisation.AreImagesEqual;

import java.time.LocalDate;

import static java.time.Month.MAY;
import static java.time.Month.NOVEMBER;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GrapheurEvolutionPatrimoinePatrimoineEtudiantTest {
  private final GrapheurEvolutionPatrimoine grapheurEvolutionPatrimoine = new GrapheurEvolutionPatrimoine();
  private final AreImagesEqual areImagesEqual = new AreImagesEqual();
  private final ResourceFileGetter resourceFileGetter = new ResourceFileGetter();

  private Patrimoine patrimoine() {
    return new PatrimoineEtudiantPireCas().get();
  }

  @Test
  void visualise_sur_quelques_jours() {
    var patrimoine = new EvolutionPatrimoine(
        "Dummy",
        patrimoine(),
        LocalDate.of(2024, MAY, 12),
        LocalDate.of(2024, MAY, 17));

    var imageGeneree = grapheurEvolutionPatrimoine.apply(patrimoine);

    assertTrue(areImagesEqual.apply(
        resourceFileGetter.apply("patrimoine-etudiant-sur-quelques-jours.png"),
        imageGeneree));
  }

  @Test
  void visualise_sur_quelques_mois() {
    var patrimoine = new EvolutionPatrimoine(
        "Dummy",
        patrimoine(),
        LocalDate.of(2024, MAY, 12),
        LocalDate.of(2024, NOVEMBER, 5));

    var imageGeneree = grapheurEvolutionPatrimoine.apply(patrimoine);

    assertTrue(areImagesEqual.apply(
        resourceFileGetter.apply("patrimoine-etudiant-sur-quelques-mois.png"),
        imageGeneree));
  }

  @Test
  void visualise_sur_quelques_annees() {
    var patrimoine = new EvolutionPatrimoine(
        "Dummy",
        patrimoine(),
        LocalDate.of(2024, MAY, 12),
        LocalDate.of(2026, NOVEMBER, 5));

    var imageGeneree = grapheurEvolutionPatrimoine.apply(patrimoine);

    assertTrue(areImagesEqual.apply(
        resourceFileGetter.apply("patrimoine-etudiant-sur-quelques-annees.png"),
        imageGeneree));
  }
}