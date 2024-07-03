package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.time.Month;
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
  void valeurPatrimoineAu17Septembre2024() {
    var zety = new Personne("Zety");
    var au3juillet2024 = LocalDate.of(2024, Month.JULY, 3);

    var ordinateur = new Materiel(
            "Ordinateur",
            au3juillet2024,
            1_200_000,
            au3juillet2024.minusDays(2),
            -0.10);

    var vetements = new Materiel(
            "Vêtements",
            au3juillet2024,
            1_500_000,
            au3juillet2024.minusDays(2),
            -0.50);

    var argentEspeces = new Argent("Espèces", au3juillet2024, 800_000);

    var fraisScolarite = new FluxArgent(
            "Frais de scolarité",
            argentEspeces,
            LocalDate.of(2023, Month.NOVEMBER, 27),
            LocalDate.of(2024, Month.AUGUST, 27),
            -200_000,
            30);

    var compteBancaire = new Argent("Compte bancaire", au3juillet2024, 100_000);

    var fraisTenueCompte = new FluxArgent(
            "Frais de tenue de compte",
            compteBancaire,
            au3juillet2024.minusMonths(1),
            au3juillet2024.plusYears(1),
            -20_000,
            30);
    var patrimoineZety = new Patrimoine(
            "Patrimoine de Zety",
            zety,
            au3juillet2024,
            Set.of(ordinateur, vetements, argentEspeces, fraisScolarite, compteBancaire, fraisTenueCompte));
    var valeurPatrimoineAu17Septembre2024 = patrimoineZety.projectionFuture(LocalDate.of(2024, Month.SEPTEMBER, 17)).getValeurComptable();
    var valeurOrdinateurAu17Septembre2024 = ordinateur.valeurComptableFuture(LocalDate.of(2024, Month.SEPTEMBER, 17));
    var valeurVetementsAu17Septembre2024 = vetements.valeurComptableFuture(LocalDate.of(2024, Month.SEPTEMBER, 17));
    var valeurEspecesAu17Septembre2024 = argentEspeces.valeurComptableFuture(LocalDate.of(2024, Month.SEPTEMBER, 17));
    var valeurCompteBancaireAu17Septembre2024 = compteBancaire.valeurComptableFuture(LocalDate.of(2024, Month.SEPTEMBER, 17));
    var valeurTotaleAttendue = valeurOrdinateurAu17Septembre2024 + valeurVetementsAu17Septembre2024 + valeurEspecesAu17Septembre2024 + valeurCompteBancaireAu17Septembre2024;
    assertEquals(valeurTotaleAttendue, valeurPatrimoineAu17Septembre2024);
  }
  @Test
  void diminutionPatrimoineEntre17Et18Septembre2024() {
    var zety = new Personne("Zety");
    var au3juillet2024 = LocalDate.of(2024, Month.JULY, 3);

    var ordinateur = new Materiel(
            "Ordinateur",
            au3juillet2024,
            1_200_000,
            au3juillet2024,
            -0.10);

    var vetements = new Materiel(
            "Vêtements",
            au3juillet2024,
            1_500_000,
            au3juillet2024,
            -0.50);

    var espece = new Argent("espèce", au3juillet2024, 800_000);

    var debutScolarite = LocalDate.of(2023, Month.NOVEMBER, 1);
    var finScolarite = LocalDate.of(2024, Month.AUGUST, 30);
    var fraisDeScolarite = new FluxArgent(
            "Frais de Scolarité",
            espece,
            debutScolarite,
            finScolarite,
            -200_000,
            27);

    var compteBancaire = new Argent("CompteBancaire", au3juillet2024, 100_000);
    var fraisDuCompte = new FluxArgent(
            "Frais de compte",
            compteBancaire,
            au3juillet2024,
            LocalDate.MAX,
            -20_000,
            25);

    var dateEmprunt = LocalDate.of(2024, Month.SEPTEMBER, 18);
    var dateRemb = dateEmprunt.plusYears(1);
    var dette = new Dette("Dette Scolarité", au3juillet2024, 0);

    var pret = new FluxArgent("Frais De Scolarité Prêt", compteBancaire, dateEmprunt, dateEmprunt, 10_000_000, dateEmprunt.getDayOfMonth());
    var detteAjout = new FluxArgent("Frais De Scolarité Dette", dette, dateEmprunt, dateEmprunt, -11_000_000, dateEmprunt.getDayOfMonth());
    var remboursement = new FluxArgent("Frais De Scolarité Rem", compteBancaire, dateRemb, dateRemb, -11_000_000, dateRemb.getDayOfMonth());
    var detteAnnulation = new FluxArgent("Frais De Scolarité annulation", dette, dateRemb, dateRemb, 11_000_000, dateRemb.getDayOfMonth());

    var detteCompteBancaire = new GroupePossession(
            "Compte Bancaire",
            au3juillet2024,
            Set.of(pret, detteAjout, remboursement, detteAnnulation)
    );

    var patrimoineZetyAu3juillet24 = new Patrimoine(
            "patrimoineZetyAu3juillet24",
            zety,
            au3juillet2024,
            Set.of(ordinateur,
                    vetements,
                    espece,
                    fraisDeScolarite,
                    compteBancaire,
                    fraisDuCompte,
                    dette,
                    detteCompteBancaire
            )
    );

    var au17septembre24 = LocalDate.of(2024, Month.SEPTEMBER, 17);
    var au18septembre24 = LocalDate.of(2024, Month.SEPTEMBER, 18);
    assertTrue(patrimoineZetyAu3juillet24.projectionFuture(au17septembre24).getValeurComptable() > patrimoineZetyAu3juillet24.projectionFuture(au18septembre24).getValeurComptable());
    assertEquals(1_002_384, patrimoineZetyAu3juillet24.projectionFuture(au17septembre24).getValeurComptable() - patrimoineZetyAu3juillet24.projectionFuture(au18septembre24).getValeurComptable());
  }

  @Test
  void zety_n_a_plus_especes_apres_18_septembre_2024() {
    var zety = new Personne("Zety");
    var au3juillet2024 = LocalDate.of(2024, Month.JULY, 3);
    var argentEspeces = new Argent("Espèces", au3juillet2024, 800_000);
    var ordinateur = new Materiel(
            "Ordinateur",
            au3juillet2024,
            1_200_000,
            au3juillet2024.minusDays(2),
            -0.10);

    var vetements = new Materiel(
            "Vêtements",
            au3juillet2024,
            1_500_000,
            au3juillet2024.minusDays(2),
            -0.50);

    var fraisScolarite = new FluxArgent(
            "Frais de scolarité",
            argentEspeces,
            LocalDate.of(2023, Month.NOVEMBER, 27),
            LocalDate.of(2024, Month.AUGUST, 27),
            -200_000,
            30);

    var compteBancaire = new Argent("Compte bancaire", au3juillet2024, 100_000);

    var fraisTenueCompte = new FluxArgent(
            "Frais de tenue de compte",
            compteBancaire,
            au3juillet2024.minusMonths(1),
            au3juillet2024.plusYears(1),
            -20_000,
            30);

    var patrimoineZety = new Patrimoine(
            "Patrimoine de Zety",
            zety,
            au3juillet2024,
            Set.of(argentEspeces, ordinateur, vetements, fraisScolarite, compteBancaire, fraisTenueCompte));

    LocalDate dateProjection = au3juillet2024;
    while (true) {
      if (dateProjection.equals(LocalDate.of(2024, Month.SEPTEMBER, 21))) {
        argentEspeces = new Argent("Espèces", dateProjection, argentEspeces.getValeurComptable() - 2_500_000);
      }
      if (dateProjection.getDayOfMonth() == 15 && dateProjection.getMonthValue() >= 1 && dateProjection.getYear() == 2024) {
        argentEspeces = new Argent("Espèces", dateProjection, argentEspeces.getValeurComptable() + 100_000);
      }
      if (dateProjection.getMonthValue() >= 10 && dateProjection.getDayOfMonth() == 1 &&
              dateProjection.isBefore(LocalDate.of(2025, Month.FEBRUARY, 14))) {
        argentEspeces = new Argent("Espèces", dateProjection, argentEspeces.getValeurComptable() - 250_000);
      }
      if (argentEspeces.getValeurComptable() <= 0) {
        break;
      }
      dateProjection = dateProjection.plusDays(1);
    }
    assertEquals(LocalDate.of(2024, Month.SEPTEMBER, 21), dateProjection);
  }
  @Test
  void valeurPatrimoineAu14Fevrier2025() {
    var zety = new Personne("Zety");
    var au3juillet2024 = LocalDate.of(2024, Month.JULY, 3);
    var argentEspeces = new Argent("Espèces", au3juillet2024, 800_000);
    var ordinateur = new Materiel(
            "Ordinateur",
            au3juillet2024,
            1_200_000,
            au3juillet2024.minusDays(2),
            -0.10);

    var vetements = new Materiel(
            "Vêtements",
            au3juillet2024,
            1_500_000,
            au3juillet2024.minusDays(2),
            -0.50);

    var fraisScolarite = new FluxArgent(
            "Frais de scolarité",
            argentEspeces,
            LocalDate.of(2023, Month.NOVEMBER, 27),
            LocalDate.of(2024, Month.AUGUST, 27),
            -200_000,
            30);

    var compteBancaire = new Argent("Compte bancaire", au3juillet2024, 100_000);

    var fraisTenueCompte = new FluxArgent(
            "Frais de tenue de compte",
            compteBancaire,
            au3juillet2024.minusMonths(1),
            au3juillet2024.plusYears(1),
            -20_000,
            30);
    var patrimoineZety = new Patrimoine(
            "Patrimoine de Zety",
            zety,
            au3juillet2024,
            Set.of(argentEspeces, ordinateur, vetements, fraisScolarite, compteBancaire, fraisTenueCompte));
    var valeurPatrimoineAu14Fevrier2025 = patrimoineZety.projectionFuture(LocalDate.of(2025, Month.FEBRUARY, 14)).getValeurComptable();
    assertEquals(2721314, valeurPatrimoineAu14Fevrier2025);
  }
  @Test
  void testValeurPatrimoineZetyEnEuroAu26Octobre2025() {
    LocalDate dateCreation = LocalDate.of(2024, Month.JULY, 3);
    Personne zety = new Personne("Zety");
    Set<Possession> possessionsZety = Set.of(
            new Materiel("Ordinateur", dateCreation, 1_200_000, dateCreation.minusDays(2), -0.10),
            new Materiel("Vêtements", dateCreation, 1_500_000, dateCreation.minusDays(2), -0.50),
            new Argent("Espèces", dateCreation, 800_000),
            new FluxArgent("Frais de scolarité", new Argent("Espèces", dateCreation, 800_000), LocalDate.of(2023, Month.NOVEMBER, 27), LocalDate.of(2024, Month.AUGUST, 27), -200_000, 30),
            new Argent("Compte bancaire", dateCreation, 100_000),
            new FluxArgent("Frais de tenue de compte", new Argent("Compte bancaire", dateCreation, 100_000), dateCreation.minusMonths(1), dateCreation.plusYears(1), -20_000, 30)
    );
    Patrimoine patrimoineZety = new Patrimoine("Patrimoine de Zety", zety, dateCreation, possessionsZety);

    double tauxChangeEURtoAr = 4821.0;
    double tauxAppreciationAnnuel = -10.0;
    LocalDate dateEvaluation = LocalDate.of(2025, Month.OCTOBER, 26);
    double valeurPatrimoineEnEuro = calculerValeurPatrimoineEnDevise(patrimoineZety, dateEvaluation, "EUR", tauxChangeEURtoAr, tauxAppreciationAnnuel);
    double expectedValueInEuros = 0.0;
    assertEquals(expectedValueInEuros, valeurPatrimoineEnEuro, 0.01);
  }

  private double calculerValeurPatrimoineEnDevise(Patrimoine patrimoine, LocalDate date, String devise, double tauxChange, double tauxAppreciationAnnuel) {
    double valeurTotale = 0.0;
    for (Possession possession : patrimoine.possessions()) {
      double valeurPossession = possession.getValeurComptable();
      valeurTotale += valeurPossession * tauxChange * Math.pow(1 + tauxAppreciationAnnuel / 100.0, ChronoUnit.DAYS.between(patrimoine.t(), date));
    }
    return valeurTotale;
  }

}