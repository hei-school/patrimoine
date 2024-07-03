package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
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
  void patrimoine_de_zety() {
    Personne Zety = new Personne("Zety");
    LocalDate au03juillet2024 = LocalDate.of(2024, JULY, 3);

    Materiel ordinateur = new Materiel("ordinateur", au03juillet2024, 1_200_000, au03juillet2024, -0.10);

    Materiel vetements = new Materiel("vêtements", au03juillet2024, 1_500_000, au03juillet2024, -0.50);

    Argent argentEspeces = new Argent("argent en espèces", au03juillet2024, 800_000);

    int fraisScolariteTotal = 0;
    LocalDate debutScolarite = LocalDate.of(2024, JULY, 27);
    LocalDate finScolarite = LocalDate.of(2024, AUGUST, 27);
    LocalDate datePaiement = debutScolarite;
    while (!datePaiement.isAfter(finScolarite)) {
      fraisScolariteTotal += 200_000;
      datePaiement = datePaiement.plusMonths(1);
    }
    Argent fraisScolarite = new Argent("frais de scolarité", au03juillet2024, fraisScolariteTotal);

    int fraisTenueCompte = 0;
    LocalDate debutCompte = LocalDate.of(2024, JULY, 25);
    LocalDate datePonction = debutCompte;
    while (!datePonction.isAfter(LocalDate.of(2024, SEPTEMBER, 25))) {
      fraisTenueCompte += 20_000;
      datePonction = datePonction.plusMonths(1);
    }

    Argent compteBancaire = new Argent("compte bancaire", au03juillet2024, 100_000 - fraisTenueCompte);

    LocalDate au17septembre2024 = LocalDate.of(2024, SEPTEMBER, 17);

    Patrimoine patrimoine_de_zety = new Patrimoine(
            "patrimoineDeZety",
            Zety, au17septembre2024,
            Set.of(
                    new Materiel("ordinateur", au03juillet2024, 1_200_000, au03juillet2024, -0.10),
                    new Materiel("vêtements", au03juillet2024, 1_500_000, au03juillet2024, -0.50),
                    new Argent("frais de scolarité", au03juillet2024, fraisScolariteTotal),
                    new Argent("compte bancaire", au03juillet2024, 100_000 - fraisTenueCompte)
            ));

   assertEquals(1_175_013, ordinateur.valeurComptableFuture(au17septembre2024));
   assertEquals(1_343_835, vetements.valeurComptableFuture(au17septembre2024));
   assertEquals(400_000, fraisScolarite.valeurComptableFuture(au17septembre2024));
   assertEquals(40_000, compteBancaire.valeurComptableFuture(au17septembre2024));
   assertEquals(3_140_000, patrimoine_de_zety.getValeurComptable());
  }

}