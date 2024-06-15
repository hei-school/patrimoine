package school.hei.patrimoine;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.possession.Argent;
import school.hei.patrimoine.possession.FluxArgent;
import school.hei.patrimoine.possession.GroupePossession;

import java.time.Instant;
import java.util.Set;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PatrimoineTest {

  @Test
  void patrimoine_vide_vaut_0() {
    var ilo = new Personne("Ilo");

    var patrimoineIloAu13mai24 = new Patrimoine(
        ilo,
        Instant.parse("2024-05-13T00:00:00.00Z"),
        Set.of());

    assertEquals(0, patrimoineIloAu13mai24.getValeurComptable());
  }

  @Test
  void patrimoine_a_de_l_argent() {
    var ilo = new Personne("Ilo");

    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var patrimoineIloAu13mai24 = new Patrimoine(
        ilo,
        au13mai24,
        Set.of(
            new Argent("Espèces", au13mai24, 400_000),
            new Argent("Compte epargne", au13mai24, 200_000),
            new Argent("Compte courant", au13mai24, 600_000)));

    assertEquals(1_200_000, patrimoineIloAu13mai24.getValeurComptable());
  }

  @Test
  void patrimoine_possede_un_train_de_vie_financé_par_argent() {
    var ilo = new Personne("Ilo");
    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var financeur = new Argent("Espèces", au13mai24, 600_000);
    var trainDeVie = new FluxArgent(
        "Vie courante",
        -100_000,
        au13mai24.minus(100, DAYS),
        au13mai24.plus(100, DAYS),
        financeur, 15);

    var patrimoineIloAu13mai24 = new Patrimoine(
        ilo,
        au13mai24,
        Set.of(financeur, trainDeVie));

    assertEquals(500_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plus(10, DAYS)).getValeurComptable());
    assertEquals(200_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plus(100, DAYS)).getValeurComptable());
    assertEquals(200_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plus(1_000, DAYS)).getValeurComptable());
  }

  @Test
  void patrimoine_possede__groupe_de_train_de_vie_et_d_argent() {
    var ilo = new Personne("Ilo");
    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var financeur = new Argent("Espèces", au13mai24, 600_000);
    var trainDeVie = new FluxArgent(
        "Vie courante",
        -100_000,
        au13mai24.minus(100, DAYS),
        au13mai24.plus(100, DAYS),
        financeur, 15);

    var patrimoineIloAu13mai24 = new Patrimoine(
        ilo,
        au13mai24,
        Set.of(new GroupePossession("Le groupe", au13mai24, Set.of(financeur, trainDeVie))));

    assertEquals(500_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plus(10, DAYS)).getValeurComptable());
    assertEquals(200_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plus(100, DAYS)).getValeurComptable());
    assertEquals(200_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plus(1_000, DAYS)).getValeurComptable());
  }
}