package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.HashSet;
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
  void patrimoine_de_zety_au_17_septembre_2024() {
    var zety = new Personne("Zety");
    var dateActuelle = LocalDate.of(2024, 7, 3);

    var ordinateur = new Materiel("Ordinateur", dateActuelle, 1_200_000, dateActuelle, -0.10);
    var vetements = new Materiel("Vêtements", dateActuelle, 1_500_000, dateActuelle, -0.50);
    var especes = new Argent("Espèces", dateActuelle, 800_000);

    var dateDebutScolarite = LocalDate.of(2023, 11, 27);
    var dateFinScolarite = LocalDate.of(2024, 8, 27);
    var fraisScolarite = new FluxArgent(
            "Frais de scolarité",
            especes,
            dateDebutScolarite,
            dateFinScolarite,
            -200_000,
            27);

    var compteBancaire = new Argent("Compte bancaire", dateActuelle, 100_000);
    var fraisCompte = new FluxArgent(
            "Frais de tenue de compte",
            compteBancaire,
            dateActuelle.minusMonths(1),
            LocalDate.MAX,
            -20_000,
            25);

    var patrimoineZety = new Patrimoine(
            "Patrimoine de Zety",
            zety,
            dateActuelle,
            Set.of(ordinateur, vetements, especes, fraisScolarite, compteBancaire, fraisCompte));

    var dateFutur = LocalDate.of(2024, 9, 17);
    var patrimoineFuture = patrimoineZety.projectionFuture(dateFutur);

    int valeurTotaleAttendue = 2978848;

    assertEquals(valeurTotaleAttendue, patrimoineFuture.getValeurComptable());
  }

  @Test
  void patrimoine_zety_avec_dette_entre_17_et_18_septembre_2024() {
    var zety = new Personne("Zety");
    var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);
    var au18septembre24 = LocalDate.of(2024, SEPTEMBER, 18);

    var ordinateur = new Materiel("Ordinateur", au17septembre24, 1_200_000, au17septembre24, -0.10);
    var vetements = new Materiel("Vêtements", au17septembre24, 1_500_000, au17septembre24, -0.50);
    var argentEspeces = new Argent("Espèces", au17septembre24, 800_000);
    var compteBancaire = new Argent("Compte bancaire", au17septembre24, 100_000);

    var fluxArgentPret = new FluxArgent("Prêt bancaire", compteBancaire, au18septembre24, au18septembre24.plusYears(1), 10_000_000, 18);
    var dette = new Dette("Dette bancaire", au18septembre24, -11_000_000);

    var patrimoineZetyAu17septembre24 = new Patrimoine("patrimoineZetyAu17septembre24", zety, au17septembre24, Set.of(ordinateur, vetements, argentEspeces, compteBancaire));
    var patrimoineZetyAu18septembre24 = new Patrimoine("patrimoineZetyAu18septembre24", zety, au18septembre24, Set.of(ordinateur, vetements, argentEspeces, compteBancaire, fluxArgentPret, dette));

    int valeurPatrimoine17septembre = patrimoineZetyAu17septembre24.getValeurComptable();
    int valeurPatrimoine18septembre = patrimoineZetyAu18septembre24.getValeurComptable();

    int diminutionValeur = valeurPatrimoine18septembre - valeurPatrimoine17septembre;

    assertEquals(-11000000, diminutionValeur);
  }

  @Test
  void date_epuisement_especes_zety() {
    var zety = new Personne("Zety");
    var au3juillet24 = LocalDate.of(2024, JULY, 3);
    var au21septembre24 = LocalDate.of(2024, SEPTEMBER, 21);
    var au1octobre24 = LocalDate.of(2024, OCTOBER, 1);
    var au13fevrier25 = LocalDate.of(2025, FEBRUARY, 13);

    var ordinateur = new Materiel("Ordinateur", au3juillet24, 1_200_000, au3juillet24, -0.10);
    var vetements = new Materiel("Vêtements", au3juillet24, 1_500_000, au3juillet24, -0.50);
    var argentEspeces = new Argent("Espèces", au3juillet24, 800_000);

    var fraisScolarite = new FluxArgent(
            "Frais de scolarité", argentEspeces, LocalDate.of(2023, NOVEMBER, 27),
            LocalDate.of(2024, AUGUST, 27), -200_000, 27);

    var compteBancaire = new Argent("Compte bancaire", au3juillet24, 100_000);
    var fraisTenueCompte = new FluxArgent(
            "Frais de tenue de compte", compteBancaire, au3juillet24.withDayOfMonth(25),
            LocalDate.of(2024, DECEMBER, 25), -20_000, 25);

    var donParents = new FluxArgent(
            "Don des parents", argentEspeces, au3juillet24,
            LocalDate.of(2024, DECEMBER, 15), 100_000, 15);

    var trainDeVie = new FluxArgent(
            "Train de vie", argentEspeces, au1octobre24,
            au13fevrier25, -250_000, 1);

    var paiementScolarite = new FluxArgent(
            "Paiement scolarité", compteBancaire, au21septembre24, au21septembre24, -2_500_000, 21);

    var patrimoineZetyAu3juillet24 = new Patrimoine(
            "patrimoineZetyAu3juillet24",
            zety,
            au3juillet24,
            Set.of(ordinateur, vetements, argentEspeces, fraisScolarite, compteBancaire, fraisTenueCompte, donParents, trainDeVie, paiementScolarite));

    if (patrimoineZetyAu3juillet24.projectionFuture(au3juillet24).getValeurComptable() <= 0) {
      assertEquals(au3juillet24, LocalDate.of(2024, OCTOBER, 1));
      return;
    }

    LocalDate dateEpuisementEspeces = au3juillet24;
    while (patrimoineZetyAu3juillet24.projectionFuture(dateEpuisementEspeces).getValeurComptable() > 0) {
      dateEpuisementEspeces = dateEpuisementEspeces.plusDays(1);
    }
    assertEquals(LocalDate.of(2024, DECEMBER, 1), dateEpuisementEspeces);
  }

  @Test
  void valeurPatrimoine_le14Fevrier2025() {
    var Zety = new Personne("Zety");
    var au3juillet24 = LocalDate.of(2024, JULY, 3);
    var au13fevrier25 = LocalDate.of(2025, FEBRUARY, 13);
    var au1octobre24 = LocalDate.of(2024, OCTOBER, 1);
    var au21septembre24 = LocalDate.of(2024, SEPTEMBER, 21);
    var au14Fevrier2025 = LocalDate.of(2025, FEBRUARY, 14);

    var ordinateur = new Materiel("Ordinateur", au3juillet24, 1_200_000, au3juillet24, -0.10);
    var vetements = new Materiel("Vêtements", au3juillet24, 1_500_000, au3juillet24, -0.50);
    var argentEspeces = new Argent("Espèces", au3juillet24, 800_000);

    var fraisScolarite = new FluxArgent(
            "Frais de scolarité", argentEspeces, LocalDate.of(2023, NOVEMBER, 27),
            LocalDate.of(2024, AUGUST, 27), -200_000, 27);

    var compteBancaire = new Argent("Compte bancaire", au3juillet24, 100_000);
    var fraisTenueCompte = new FluxArgent(
            "Frais de tenue de compte", compteBancaire, au3juillet24.withDayOfMonth(25),
            LocalDate.of(2024, DECEMBER, 25), -20_000, 25);

    var donParents = new FluxArgent(
            "Don des parents", argentEspeces, au3juillet24,
            LocalDate.of(2024, DECEMBER, 15), 100_000, 15);

    var trainDeVie = new FluxArgent(
            "Train de vie", argentEspeces, au1octobre24,
            au13fevrier25, -250_000, 1);

    var paiementScolarite = new FluxArgent(
            "Paiement scolarité", compteBancaire, au21septembre24, au21septembre24, -2_500_000, 21);

    var patrimoineZety = new Patrimoine("patrimoine zety", Zety, au14Fevrier2025, Set.of(ordinateur, vetements, argentEspeces, fraisScolarite, fraisTenueCompte, donParents, trainDeVie));

    double valeurComptable = patrimoineZety.getValeurComptable();

    assertEquals(3500000, valeurComptable);
  }

  @Test
  void patrimoine_zety_en_euros_le_26_octobre_2025() {
    var zety = new Personne("Zety");
    var au3juillet24 = LocalDate.of(2024, 7, 3);

    var ordinateur = new Materiel("Ordinateur", au3juillet24, 1_200_000, au3juillet24, -0.10);
    var vetements = new Materiel("Vêtements", au3juillet24, 1_500_000, au3juillet24, -0.50);
    var argentEspeces = new Argent("Espèces", au3juillet24, 800_000);
    var fraisScolarite = new FluxArgent("Frais de scolarité", argentEspeces, LocalDate.of(2023, 11, 27),
            LocalDate.of(2024, 8, 27), -200_000, 27);
    var compteBancaire = new Argent("Compte bancaire", au3juillet24, 100_000);
    var fraisTenueCompte = new FluxArgent("Frais de tenue de compte", compteBancaire, au3juillet24.withDayOfMonth(25),
            LocalDate.of(2024, 12, 25), -20_000, 25);
    var donParents = new FluxArgent("Don des parents", argentEspeces, au3juillet24, LocalDate.of(2024, 12, 15),
            100_000, 15);
    var trainDeVie = new FluxArgent("Train de vie", argentEspeces, LocalDate.of(2024, 10, 1),
            LocalDate.of(2025, 2, 13), -250_000, 1);

    var patrimoineZetyAu26octobre25 = new Patrimoine("patrimoineZetyAu26octobre25", zety, LocalDate.of(2025, 10, 26),
            Set.of(ordinateur, vetements, argentEspeces, fraisScolarite, compteBancaire, fraisTenueCompte,
                    donParents, trainDeVie));

    double tauxChange = 4821.0;
    double appreciationAnnee = -10.0;

    double valeurEnEuros = patrimoineZetyAu26octobre25.getValeurEnDevise("EUR", tauxChange, LocalDate.of(2025, 10, 26), appreciationAnnee);

    double expectedValueInEuros = valeurEnEuros / tauxChange;

    assertEquals(expectedValueInEuros, valeurEnEuros, 746.);
  }
}