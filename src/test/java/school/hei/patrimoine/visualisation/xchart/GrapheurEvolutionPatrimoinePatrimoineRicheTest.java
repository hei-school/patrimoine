package school.hei.patrimoine.visualisation.xchart;

import static java.time.Month.MARCH;
import static java.time.Month.MAY;
import static java.time.Month.NOVEMBER;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.ResourceFileGetter;
import school.hei.patrimoine.cas.example.PatrimoineCresusSupplier;
import school.hei.patrimoine.cas.example.PatrimoineRicheSupplier;
import school.hei.patrimoine.modele.evolution.EvolutionPatrimoine;
import school.hei.patrimoine.visualisation.AreImagesEqual;

class GrapheurEvolutionPatrimoinePatrimoineRicheTest {
  private final GrapheurEvolutionPatrimoine grapheurEvolutionPatrimoine =
      new GrapheurEvolutionPatrimoine();
  private final AreImagesEqual areImagesEqual = new AreImagesEqual();
  private final ResourceFileGetter resourceFileGetter = new ResourceFileGetter();

  @Test
  void visualise_riche_pire_sur_quelques_annees() {
    var patrimoine =
        new EvolutionPatrimoine(
            "Dummy",
            new PatrimoineRicheSupplier().get(),
            LocalDate.of(2024, MAY, 12),
            LocalDate.of(2026, NOVEMBER, 5));

    var imageGeneree = grapheurEvolutionPatrimoine.apply(patrimoine);

    assertTrue(
        areImagesEqual.apply(
            resourceFileGetter.apply("patrimoine-riche-sur-quelques-annees.png"), imageGeneree));
  }

  @Test
  void visualise_riche_moyen_sur_quelques_annees_avec_tous_les_agregats() {
    var patrimoine =
        new EvolutionPatrimoine(
            "Dummy",
            new PatrimoineCresusSupplier().get(),
            LocalDate.of(2024, MAY, 12),
            LocalDate.of(2025, MARCH, 5));

    var imageGeneree =
        grapheurEvolutionPatrimoine.apply(
            patrimoine, new GrapheConf(false, true, true, true, true));

    assertTrue(
        areImagesEqual.apply(
            resourceFileGetter.apply("patrimoine-cresus-sur-quelques-annees.png"), imageGeneree));
  }

  @Test
  void visualise_riche_moyen_sur_quelques_annees_avec_tresorie_et_agregat_seulement() {
    var patrimoine =
        new EvolutionPatrimoine(
            "Dummy",
            new PatrimoineCresusSupplier().get(),
            LocalDate.of(2024, MAY, 12),
            LocalDate.of(2025, MARCH, 5));

    var imageGeneree =
        grapheurEvolutionPatrimoine.apply(
            patrimoine, new GrapheConf(false, true, true, false, false));

    assertTrue(
        areImagesEqual.apply(
            resourceFileGetter.apply("patrimoine-cresus-sur-quelques-annees_treso.png"),
            imageGeneree));
  }

  @Test
  void visualise_riche_moyen_sur_quelques_annees_avec_immo_et_agregat_seulement() {
    var patrimoine =
        new EvolutionPatrimoine(
            "Dummy",
            new PatrimoineCresusSupplier().get(),
            LocalDate.of(2024, MAY, 12),
            LocalDate.of(2025, MARCH, 5));

    var imageGeneree =
        grapheurEvolutionPatrimoine.apply(
            patrimoine, new GrapheConf(false, true, false, true, false));

    assertTrue(
        areImagesEqual.apply(
            resourceFileGetter.apply("patrimoine-cresus-sur-quelques-annees_immo.png"),
            imageGeneree));
  }

  @Test
  void visualise_riche_moyen_sur_quelques_annees_avec_obli_et_agregat_seulement() {
    var patrimoine =
        new EvolutionPatrimoine(
            "Dummy",
            new PatrimoineCresusSupplier().get(),
            LocalDate.of(2024, MAY, 12),
            LocalDate.of(2025, MARCH, 5));

    var imageGeneree =
        grapheurEvolutionPatrimoine.apply(
            patrimoine, new GrapheConf(true, true, false, false, true));

    assertTrue(
        areImagesEqual.apply(
            resourceFileGetter.apply("patrimoine-cresus-sur-quelques-annees_obli.png"),
            imageGeneree));
  }
}
