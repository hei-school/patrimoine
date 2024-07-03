package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.MAY;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
  void patrimoine_zety_le_17_septembre_2024() {
    var au17Septembre = LocalDate.of(2024, 9, 17);
    var ordinateur = new Materiel("Ordinateur", LocalDate.of(2024, 7, 3), 1_200_000, LocalDate.of(2024, 7, 3), -0.10);
    var vetements = new Materiel("Vêtements", LocalDate.of(2024, 7, 3), 1_500_000, LocalDate.of(2024, 7, 3), -0.50);
    var argentEnEspece = new Argent("Argent en espèces", LocalDate.of(2024, 7, 3), 800_000);
    var compteBancaire = new Argent("Compte bancaire", LocalDate.of(2024, 7, 3), 100_000);
    var fraisTenueCompte = new FluxArgent("Frais de tenue de compte", compteBancaire, LocalDate.of(2024, 7, 3), LocalDate.of(2024, 7, 3).plusMonths(12), -20_000, 25);
    var fraisScolarite = new Argent("Frais de scolarité", LocalDate.of(2024, 7, 3), 0);
    var fluxFraisScolarite = new FluxArgent("Frais de scolarité", fraisScolarite, LocalDate.of(2023, 11, 27), LocalDate.of(2024, 8, 27), -200_000, 27);

    var patrimoineZety = new Patrimoine("Patrimoine Zety", new Personne("Zety"), LocalDate.of(2024, 7, 3), Set.of(ordinateur, vetements, argentEnEspece, compteBancaire, fraisScolarite, fluxFraisScolarite, fraisTenueCompte));

    var projectionFuture = patrimoineZety.projectionFuture(au17Septembre);
    assertEquals(2978848, projectionFuture.getValeurComptable());
  }

  @Test
  void zety_s_endette() {
    var au17Septembre = LocalDate.of(2024, 9, 17);
    var ordinateur = new Materiel("Ordinateur", LocalDate.of(2024, 7, 3), 1_200_000, LocalDate.of(2024, 7, 3), -0.10);
    var vetements = new Materiel("Vêtements", LocalDate.of(2024, 7, 3), 1_500_000, LocalDate.of(2024, 7, 3), -0.50);
    var argentEnEspece = new Argent("Argent en espèces", LocalDate.of(2024, 7, 3), 800_000);
    var compteBancaire = new Argent("Compte bancaire", LocalDate.of(2024, 7, 3), 100_000);
    var fraisTenueCompte = new FluxArgent("Frais de tenue de compte", compteBancaire, LocalDate.of(2024, 7, 3), LocalDate.of(2024, 7, 3).plusMonths(12), -20_000, 25);
    var fraisScolarite = new Argent("Frais de scolarité", LocalDate.of(2024, 7, 3), 0);
    var fluxFraisScolarite = new FluxArgent("Frais de scolarité", fraisScolarite, LocalDate.of(2023, 11, 27), LocalDate.of(2024, 8, 27), -200_000, 27);
    var patrimoineZety = new Patrimoine("Patrimoine Zety", new Personne("Zety"), LocalDate.of(2024, 7, 3), Set.of(ordinateur, vetements, argentEnEspece, compteBancaire, fraisScolarite, fluxFraisScolarite, fraisTenueCompte));
    var projectionAu17Septembre = patrimoineZety.projectionFuture(au17Septembre);
    assertEquals(2978848, projectionAu17Septembre.getValeurComptable());
    var au18Septembre = LocalDate.of(2024, 9, 18);
    var dette = new Dette("Dette bancaire", au18Septembre, -11_000_000);
    var fluxDette = new FluxArgent("Emprunt bancaire", compteBancaire, au18Septembre, LocalDate.of(2025, 9, 18), 10_000_000, 18);
    var patrimoineZetyEndette = new Patrimoine("Patrimoine Zety endetté", new Personne("Zety"), LocalDate.of(2024, 7, 3), Set.of(ordinateur, vetements, argentEnEspece, compteBancaire, fraisScolarite, fluxFraisScolarite, fraisTenueCompte, dette, fluxDette));
    var projectionAu18Septembre = patrimoineZetyEndette.projectionFuture(au18Septembre);
    var diminutionPatrimoine = projectionAu17Septembre.getValeurComptable() - projectionAu18Septembre.getValeurComptable();
    assertEquals(1002384, diminutionPatrimoine);
  }
}