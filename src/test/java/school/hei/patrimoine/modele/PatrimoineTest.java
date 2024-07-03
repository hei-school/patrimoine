package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.GroupePossession;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static java.time.Month.MAY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PatrimoineTest {

  @Test
  void patrimoine_vide_vaut_0() {
    var ilo = new Personne("Ilo");

    var patrimoineIloAu13mai24 = new Patrimoine(
        "patrimoineIloAu13mai24",
        ilo,
        LocalDate.of(2024, MAY, 13),
        Set.of());

    assertEquals(0, patrimoineIloAu13mai24.getValeurComptable());
  }

  @Test
  void patrimoine_a_de_l_argent() {
    var ilo = new Personne("Ilo");

    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var patrimoineIloAu13mai24 = new Patrimoine(
        "patrimoineIloAu13mai24",
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
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var financeur = new Argent("Espèces", au13mai24, 600_000);
    var trainDeVie = new FluxArgent(
        "Vie courante",
        financeur, au13mai24.minusDays(100), au13mai24.plusDays(100), -100_000,
        15);

    var patrimoineIloAu13mai24 = new Patrimoine(
        "patrimoineIloAu13mai24",
        ilo,
        au13mai24,
        Set.of(financeur, trainDeVie));

    assertEquals(500_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(10)).getValeurComptable());
    assertEquals(200_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(100)).getValeurComptable());
    assertEquals(200_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(1_000)).getValeurComptable());
  }

  @Test
  void patrimoine_possede_groupe_de_train_de_vie_et_d_argent() {
    var ilo = new Personne("Ilo");
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var financeur = new Argent("Espèces", au13mai24, 600_000);
    var trainDeVie = new FluxArgent(
        "Vie courante",
        financeur, au13mai24.minusDays(100), au13mai24.plusDays(100), -100_000,
        15);

    var patrimoineIloAu13mai24 = new Patrimoine(
        "patrimoineIloAu13mai24",
        ilo,
        au13mai24,
        Set.of(new GroupePossession("Le groupe", au13mai24, Set.of(financeur, trainDeVie))));

    assertEquals(500_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(10)).getValeurComptable());
    assertEquals(200_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(100)).getValeurComptable());
    assertEquals(200_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(1_000)).getValeurComptable());
  }

  @Test
  public void testValeurPatrimoine14Fevrier2025() {
    Patrimoine patrimoine = new Patrimoine("Zety", new Personne("Zety"), LocalDate.now(), Set.of(
            new Dette("Dette", LocalDate.of(2025, 2, 15), -7000)
    ));
    double valeur = patrimoine.getValeurComptableEnDevise(LocalDate.of(2025, 2, 14), "EUR", 1, 0);
    assertEquals(-7000, valeur);
  }

  @Test
  public void testValeurPatrimoine26Octobre2025() {
    Patrimoine patrimoine = new Patrimoine("Zety", new Personne("Zety"), LocalDate.now(), Set.of(
            new Dette("Dette", LocalDate.of(2025, 2, 15), -7000)
    ));
    double valeur = patrimoine.getValeurComptableEnDevise(LocalDate.of(2025, 10, 26), "EUR", 4821, -0.10);
    assertEquals(-7000 / 4821.0, valeur, 0.01);
  }

  @Test
  public void testValeurPatrimoineEnDollar() {
    Patrimoine patrimoine = new Patrimoine("Zety", new Personne("Zety"), LocalDate.now(), Set.of(
            new Dette("Dette", LocalDate.of(2025, 2, 15), -7000)
    ));
    double tauxChangeEuroDollar = 1.1;
    double tauxAppreciationAnnuelle = 0.02;
    double valeur = patrimoine.getValeurComptableEnDevise(LocalDate.of(2025, 10, 26), "USD", tauxChangeEuroDollar, tauxAppreciationAnnuelle);
    assertTrue(valeur < -6198.45036664612 && valeur > -7000);
  }
}