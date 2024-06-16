package school.hei.patrimoine.visualisation.xchart;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.TestFileGetter;
import school.hei.patrimoine.modele.EvolutionPatrimoine;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.visualisation.AreImagesEqual;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.MAY;
import static java.time.Month.NOVEMBER;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VisualiseurPatrimoineEtudiantTest {
  private final Visualiseur visualiseur = new Visualiseur();
  private final AreImagesEqual areImagesEqual = new AreImagesEqual();
  private final TestFileGetter testFileGetter = new TestFileGetter();

  private Patrimoine patrimoine() {
    var ilo = new Personne("Ilo");
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var financeur = new Argent("Espèces", au13mai24, 400_000);
    var trainDeVie = new FluxArgent(
        "Vie courante",
        financeur,
        au13mai24.minusDays(100),
        au13mai24.plusDays(100),
        -100_000,
        15);

    var mac = new Materiel(
        "MacBook Pro",
        au13mai24,
        500_000,
        -0.9);

    return new Patrimoine(
        ilo,
        au13mai24,
        Set.of(financeur, trainDeVie, mac));

  }

  @Test
  void visualise_sur_quelques_jours() {
    var patrimoine = new EvolutionPatrimoine(
        "Dummy",
        patrimoine(),
        LocalDate.of(2024, MAY, 12),
        LocalDate.of(2024, MAY, 17));

    var imageGeneree = visualiseur.apply(patrimoine);

    assertTrue(areImagesEqual.apply(
        testFileGetter.apply("patrimoine-etudiant-sur-quelques-jours.png"),
        imageGeneree));
  }

  @Test
  void visualise_sur_quelques_mois() {
    var patrimoine = new EvolutionPatrimoine(
        "Dummy",
        patrimoine(),
        LocalDate.of(2024, MAY, 12),
        LocalDate.of(2024, NOVEMBER, 5));

    var imageGeneree = visualiseur.apply(patrimoine);

    assertTrue(areImagesEqual.apply(
        testFileGetter.apply("patrimoine-etudiant-sur-quelques-mois.png"),
        imageGeneree));
  }

  @Test
  void visualise_sur_quelques_annees() {
    var patrimoine = new EvolutionPatrimoine(
        "Dummy",
        patrimoine(),
        LocalDate.of(2024, MAY, 12),
        LocalDate.of(2026, NOVEMBER, 5));

    var imageGeneree = visualiseur.apply(patrimoine);

    assertTrue(areImagesEqual.apply(
        testFileGetter.apply("patrimoine-etudiant-sur-quelques-annees.png"),
        imageGeneree));
  }
}