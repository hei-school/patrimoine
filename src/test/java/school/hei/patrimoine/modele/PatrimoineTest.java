package school.hei.patrimoine.modele;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

class PatrimoineTest {

  @Test
  void patrimoine_vide_vaut_0() {
    var ilo = new Personne("Ilo");

    var patrimoineIloAu13mai24 =
        new Patrimoine("patrimoineIloAu13mai24", ilo, LocalDate.of(2024, MAY, 13), Set.of());

    assertEquals(0, patrimoineIloAu13mai24.getValeurComptable());
  }

  @Test
  void patrimoine_a_de_l_argent() {
    var ilo = new Personne("Ilo");

    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var patrimoineIloAu13mai24 =
        new Patrimoine(
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
  void patrimoine_possede_un_train_de_vie_finance_par_argent() {
    var ilo = new Personne("Ilo");
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var financeur = new Argent("Espèces", au13mai24, 600_000);
    var trainDeVie =
        new FluxArgent(
            "Vie courante",
            financeur,
            au13mai24.minusDays(100),
            au13mai24.plusDays(100),
            -100_000,
            15);

    var patrimoineIloAu13mai24 =
        new Patrimoine("patrimoineIloAu13mai24", ilo, au13mai24, Set.of(financeur, trainDeVie));

    assertEquals(
        500_000,
        patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(10)).getValeurComptable());
    assertEquals(
        200_000,
        patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(100)).getValeurComptable());
    assertEquals(
        200_000,
        patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(1_000)).getValeurComptable());
  }

  @Test
  void patrimoine_possede_groupe_de_train_de_vie_et_d_argent() {
    var ilo = new Personne("Ilo");
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var patrimoineIloAu13mai24 = getPatrimoine(au13mai24, ilo);

    assertEquals(
        500_000,
        patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(10)).getValeurComptable());
    assertEquals(
        200_000,
        patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(100)).getValeurComptable());
    assertEquals(
        200_000,
        patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(1_000)).getValeurComptable());
  }

  @Test
  void patrimoine_de_zety_le_17_sep_24() {
    var zety = new Personne("Zety");

    var ordinateur =
        new Materiel(
            "Ordinateur",
            LocalDate.of(2024, JULY, 3),
            1_200_000,
            LocalDate.of(2024, JULY, 3),
            -0.10);
    var vetements =
        new Materiel(
            "Vêtements",
            LocalDate.of(2024, JULY, 3),
            1_500_000,
            LocalDate.of(2024, JULY, 3),
            -0.50);
    var argentLiquide = new Argent("Argent en liquide", LocalDate.of(2024, JULY, 3), 800_000);
    var fraisScolarite =
        new FluxArgent(
            "Frais de scolarité",
            argentLiquide,
            LocalDate.of(2023, NOVEMBER, 27),
            LocalDate.of(2024, AUGUST, 27),
            200_000,
            27);
    var fraisCompte =
        new FluxArgent(
            "Frais de compte",
            argentLiquide,
            LocalDate.of(2024, JULY, 25),
            LocalDate.of(2024, SEPTEMBER, 17),
            -20_000,
            25);
    var compteBancaire =
        new Argent(
            "Compte bancaire",
            LocalDate.of(2024, JULY, 3),
            LocalDate.of(2024, SEPTEMBER, 17),
            100_000,
            Set.of(fraisCompte));

    Set<Possession> possessions = new HashSet<>();
    possessions.add(ordinateur);
    possessions.add(vetements);
    possessions.add(argentLiquide);
    possessions.add(fraisScolarite);
    possessions.add(compteBancaire);
    var patrimoine =
        new Patrimoine("Patrimoine de Zety", zety, LocalDate.of(2024, JULY, 3), possessions);

    var patrimoineFutur = patrimoine.projectionFuture(LocalDate.of(2024, SEPTEMBER, 17));

    var valeurAttendueCalculeManuellementParMaCalculatrice = 4_438_848;

    assertEquals(
        valeurAttendueCalculeManuellementParMaCalculatrice, patrimoineFutur.getValeurComptable());
  }

  private static Patrimoine getPatrimoine(LocalDate au13mai24, Personne ilo) {
    var financeur = new Argent("Espèces", au13mai24, 600_000);
    var trainDeVie =
        new FluxArgent(
            "Vie courante",
            financeur,
            au13mai24.minusDays(100),
            au13mai24.plusDays(100),
            -100_000,
            15);

    return new Patrimoine(
        "patrimoineIloAu13mai24",
        ilo,
        au13mai24,
        Set.of(new GroupePossession("Le groupe", au13mai24, Set.of(financeur, trainDeVie))));
  }
}
