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
  void patrimoine_possede_un_train_de_vie_financé_par_argent() {
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
        new Patrimoine(
            "patrimoineIloAu13mai24",
            ilo,
            au13mai24,
            Set.of(new GroupePossession("Le groupe", au13mai24, Set.of(financeur, trainDeVie))));

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
  void patrimoine_de_zety_possede_materiel_argent_et_flux_argent() {
    var zety = new Personne("Zety");
    var au03Juillet2024 = LocalDate.of(2024, JULY, 03);

    var ordinateur = new Materiel("Ordinateur", au03Juillet2024, 1_200_000, au03Juillet2024, -0.1);
    var vetements = new Materiel("Garde-Robe", au03Juillet2024, 1_500_000, au03Juillet2024, -0.5);

    var espece = new Argent("Espèces", au03Juillet2024, 800_000);
    var debutFraisScolarite = LocalDate.of(2023, NOVEMBER, 01);
    var finFraisDeScolarite = LocalDate.of(2024, AUGUST, 28);
    var fraisDeScolarite2023a2024 =
        new FluxArgent(
            "Frais de scolarité", espece, debutFraisScolarite, finFraisDeScolarite, -200_000, 27);

    var compteBancaire = new Argent("Compte Bancaire", au03Juillet2024, 100_000);
    var fraisDeTenueDeCompte =
        new FluxArgent(
            "Frais de tenue de compte",
            compteBancaire,
            au03Juillet2024,
            LocalDate.MAX,
            -20_000,
            25);

    var patrimoineZetyAu03Juillet2024 =
        new Patrimoine(
            "patrimoineZetyAu03Juillet2024",
            zety,
            au03Juillet2024,
            Set.of(ordinateur, vetements, espece, compteBancaire));
    var au17Septembre2024 = LocalDate.of(2024, SEPTEMBER, 17);

    int valeurComptableAttendue = 2978848;
    int valeurComptableActuelle =
        patrimoineZetyAu03Juillet2024.projectionFuture(au17Septembre2024).getValeurComptable();
    assertEquals(valeurComptableAttendue, valeurComptableActuelle);
  }

  @Test
  void zety_s_endette() {
    var zety = new Personne("Zety");
    var au03Juillet2024 = LocalDate.of(2024, JULY, 03);

    var ordinateur = new Materiel("Ordinateur", au03Juillet2024, 1_200_000, au03Juillet2024, -0.1);
    var vetements = new Materiel("Garde-Robe", au03Juillet2024, 1_500_000, au03Juillet2024, -0.5);

    var espece = new Argent("Espèces", au03Juillet2024, 800_000);
    var debutFraisScolarite = LocalDate.of(2023, NOVEMBER, 01);
    var finFraisDeScolarite = LocalDate.of(2024, AUGUST, 28);
    var fraisDeScolarite2023a2024 =
        new FluxArgent(
            "Frais de scolarité", espece, debutFraisScolarite, finFraisDeScolarite, -200_000, 27);

    var compteBancaire = new Argent("Compte Bancaire", au03Juillet2024, 100_000);

    var fraisDeTenueDeCompte =
        new FluxArgent(
            "Frais de tenue de compte",
            compteBancaire,
            au03Juillet2024,
            LocalDate.MAX,
            -20_000,
            25);

    var au18Septembre2024 = LocalDate.of(2024, SEPTEMBER, 18);
    var empruntFraisDeScolarite =
        new FluxArgent(
            "Emprunt frais de scolarité",
            compteBancaire,
            au18Septembre2024,
            au18Septembre2024,
            10_000_000,
            au18Septembre2024.getDayOfMonth());

    var dette = new Dette("Dette emprunt frais de scolarités", au18Septembre2024, -11_000_000);

    var patrimoineZetyAu03Juillet2024 =
        new Patrimoine(
            "patrimoineZetyAu03Juillet2024",
            zety,
            au03Juillet2024,
            Set.of(ordinateur, vetements, espece, compteBancaire, dette));

    var au17Septembre2024 = LocalDate.of(2024, SEPTEMBER, 17);
    var patrimoineZetyAu17Septembre =
        patrimoineZetyAu03Juillet2024.projectionFuture(au17Septembre2024);

    var patrimoineZetyAu18Septembre =
        patrimoineZetyAu03Juillet2024.projectionFuture(au18Septembre2024);

    var diminutionPatrimoineAttendue = 1002384;
    var diminutionPatrimoineActuelle =
        patrimoineZetyAu17Septembre.getValeurComptable()
            - patrimoineZetyAu18Septembre.getValeurComptable();

    assertEquals(diminutionPatrimoineAttendue, diminutionPatrimoineActuelle);
  }

  @Test
  void zety_etudie_en_2024_2025() {
    var zety = new Personne("Zety");
    var au03Juillet2024 = LocalDate.of(2024, JULY, 03);

    var ordinateur = new Materiel("Ordinateur", au03Juillet2024, 1_200_000, au03Juillet2024, -0.1);
    var vetements = new Materiel("Garde-Robe", au03Juillet2024, 1_500_000, au03Juillet2024, -0.5);

    var espece = new Argent("Espèces", au03Juillet2024, 800_000);
    var debutFraisScolarite = LocalDate.of(2023, NOVEMBER, 01);
    var finFraisDeScolarite = LocalDate.of(2024, AUGUST, 28);
    var fraisDeScolarite2023a2024 =
        new FluxArgent(
            "Frais de scolarité 2023-2024",
            espece,
            debutFraisScolarite,
            finFraisDeScolarite,
            -200_000,
            27);

    var compteBancaire = new Argent("Compte Bancaire", au03Juillet2024, 100_000);
    var au21Septembre2024 = LocalDate.of(2024, SEPTEMBER, 21);
    var fraisDeScolarite2024a2025 =
        new FluxArgent(
            "Frais de scolarité 2024-2025",
            compteBancaire,
            au21Septembre2024,
            au21Septembre2024,
            -2_500_000,
            au21Septembre2024.getDayOfMonth());

    var fraisDeTenueDeCompte =
        new FluxArgent(
            "Frais de tenue de compte",
            compteBancaire,
            au03Juillet2024,
            LocalDate.MAX,
            -20_000,
            25);

    var au18Septembre2024 = LocalDate.of(2024, SEPTEMBER, 18);
    var empruntFraisDeScolarite =
        new FluxArgent(
            "Emprunt frais de scolarité",
            compteBancaire,
            au18Septembre2024,
            au18Septembre2024,
            10_000_000,
            au18Septembre2024.getDayOfMonth());

    var dette = new Dette("Dette emprunt frais de scolarités", au18Septembre2024, -11_000_000);

    var debut2024 = LocalDate.of(2024, JANUARY, 01);
    var donDesParents =
        new FluxArgent("Don des parents", espece, debut2024, LocalDate.MAX, 100_000, 15);

    var debutTrainDeVie = LocalDate.of(2024, OCTOBER, 01);
    var finTrainDeVie = LocalDate.of(2025, FEBRUARY, 13);
    var trainDeVie =
        new FluxArgent(
            "Loyer, nourriture, transport, revy",
            espece,
            debutTrainDeVie,
            finTrainDeVie,
            -250_000,
            1);

    var patrimoineZetyAu03Juillet2024 =
        new Patrimoine(
            "patrimoineZetyAu03Juillet2024",
            zety,
            au03Juillet2024,
            Set.of(ordinateur, vetements, espece, compteBancaire, dette));

    var dateZeroEspecesAttendu = LocalDate.of(2025, JANUARY, 01);
    var dateDeProjectionActuel = au03Juillet2024;
    var especesProjetees = espece.projectionFuture(dateDeProjectionActuel);
    while (especesProjetees.getValeurComptable() != 0) {
      dateDeProjectionActuel = dateDeProjectionActuel.plusDays(1);
      especesProjetees = espece.projectionFuture(dateDeProjectionActuel);
    }
    assertEquals(dateZeroEspecesAttendu, dateDeProjectionActuel);
  }

  @Test
  void zety_evalue_son_patrimoine_au_14_fevrier_2025() {
    var zety = new Personne("Zety");
    var au03Juillet2024 = LocalDate.of(2024, JULY, 03);

    var ordinateur = new Materiel("Ordinateur", au03Juillet2024, 1_200_000, au03Juillet2024, -0.1);
    var vetements = new Materiel("Garde-Robe", au03Juillet2024, 1_500_000, au03Juillet2024, -0.5);

    var espece = new Argent("Espèces", au03Juillet2024, 800_000);
    var debutFraisScolarite = LocalDate.of(2023, NOVEMBER, 01);
    var finFraisDeScolarite = LocalDate.of(2024, AUGUST, 28);
    var fraisDeScolarite2023a2024 =
        new FluxArgent(
            "Frais de scolarité 2023-2024",
            espece,
            debutFraisScolarite,
            finFraisDeScolarite,
            -200_000,
            27);

    var compteBancaire = new Argent("Compte Bancaire", au03Juillet2024, 100_000);
    var au21Septembre2024 = LocalDate.of(2024, SEPTEMBER, 21);
    var fraisDeScolarite2024a2025 =
        new FluxArgent(
            "Frais de scolarité 2024-2025",
            compteBancaire,
            au21Septembre2024,
            au21Septembre2024,
            -2_500_000,
            au21Septembre2024.getDayOfMonth());

    var fraisDeTenueDeCompte =
        new FluxArgent(
            "Frais de tenue de compte",
            compteBancaire,
            au03Juillet2024,
            LocalDate.MAX,
            -20_000,
            25);

    var au18Septembre2024 = LocalDate.of(2024, SEPTEMBER, 18);
    var empruntFraisDeScolarite =
        new FluxArgent(
            "Emprunt frais de scolarité",
            compteBancaire,
            au18Septembre2024,
            au18Septembre2024,
            10_000_000,
            au18Septembre2024.getDayOfMonth());

    var dette = new Dette("Dette emprunt frais de scolarités", au18Septembre2024, -11_000_000);

    var debut2024 = LocalDate.of(2024, JANUARY, 01);
    var donDesParents =
        new FluxArgent("Don des parents", espece, debut2024, LocalDate.MAX, 100_000, 15);

    var debutTrainDeVie = LocalDate.of(2024, OCTOBER, 01);
    var finTrainDeVie = LocalDate.of(2025, FEBRUARY, 13);
    var trainDeVie =
        new FluxArgent(
            "Loyer, nourriture, transport, revy",
            espece,
            debutTrainDeVie,
            finTrainDeVie,
            -250_000,
            1);

    var patrimoineZetyAu03Juillet2024 =
        new Patrimoine(
            "patrimoineZetyAu03Juillet2024",
            zety,
            au03Juillet2024,
            Set.of(ordinateur, vetements, espece, compteBancaire, dette));

    var au14Fevrier2025 = LocalDate.of(2025, FEBRUARY, 14);
    assertEquals(
        -1528686,
        patrimoineZetyAu03Juillet2024.projectionFuture(au14Fevrier2025).getValeurComptable());
  }
}
