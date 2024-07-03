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
  void patrimoine_zety_le_17_septembre_2024() {
    var zety = new Personne("Zety");
    var au3juillet24 = LocalDate.of(2024, JULY, 3);
    var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);

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

    var patrimoineZetyAu3juillet24 = new Patrimoine(
            "patrimoineZetyAu3juillet24",
            zety,
            au3juillet24,
            Set.of(ordinateur, vetements, argentEspeces, fraisScolarite, compteBancaire, fraisTenueCompte));

    assertEquals(2978848, patrimoineZetyAu3juillet24.projectionFuture(au17septembre24).getValeurComptable());
  }

  @Test
  void diminution_entre_17_et_18_septembre_2024() {
    var zety = new Personne("Zety");
    var au3juillet24 = LocalDate.of(2024, JULY, 3);
    var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);
    var au18septembre24 = LocalDate.of(2024, SEPTEMBER, 18);

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

    var patrimoineZetyAu17septembre24 = new Patrimoine(
            "patrimoineZetyAu17septembre24",
            zety,
            au3juillet24,
            Set.of(ordinateur, vetements, argentEspeces, fraisScolarite, compteBancaire, fraisTenueCompte));

    var fluxArgentPret = new FluxArgent(
            "Prêt bancaire", compteBancaire, au18septembre24, au18septembre24.plusYears(1), 10_000_000, 18);
    var dette = new Dette("Dette bancaire", au18septembre24, -11_000_000);

    var patrimoineZetyAu18septembre24 = new Patrimoine(
            "patrimoineZetyAu18septembre24",
            zety,
            au3juillet24,
            Set.of(ordinateur, vetements, argentEspeces, fraisScolarite, compteBancaire, fraisTenueCompte, fluxArgentPret, dette));

    int valeurPatrimoine17septembre = patrimoineZetyAu17septembre24.projectionFuture(au17septembre24).getValeurComptable();
    int valeurPatrimoine18septembre = patrimoineZetyAu18septembre24.projectionFuture(au18septembre24).getValeurComptable();
    int diminution = valeurPatrimoine18septembre - valeurPatrimoine17septembre;

    assertEquals(-1002384, diminution);
  }

  @Test
  void epuisement_especes_zety() {
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
            "Don des parents", argentEspeces, LocalDate.of(2024, JANUARY, 15),
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

    LocalDate EpuisementEspeces = au3juillet24;
    while (patrimoineZetyAu3juillet24.projectionFuture(EpuisementEspeces).getValeurComptable() > 0) {
      EpuisementEspeces = EpuisementEspeces.plusDays(1);
    }

    assertEquals(LocalDate.of(2024, DECEMBER, 1), EpuisementEspeces);
  }

  @Test
  void patrimoine_zety_le_14_fevrier_2025() {
    var zety = new Personne("Zety");
    var au3juillet24 = LocalDate.of(2024, JULY, 3);
    var au14fevrier25 = LocalDate.of(2025, FEBRUARY, 14);

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
            "Don des parents", argentEspeces, LocalDate.of(2024, JANUARY, 15),
            LocalDate.of(2024, DECEMBER, 15), 100_000, 15);

    var trainDeVie = new FluxArgent(
            "Train de vie", argentEspeces, LocalDate.of(2024, OCTOBER, 1),
            LocalDate.of(2025, FEBRUARY, 13), -250_000, 1);

    var paiementScolarite = new FluxArgent(
            "Paiement scolarité", compteBancaire, LocalDate.of(2024, SEPTEMBER, 21), LocalDate.of(2024, SEPTEMBER, 21), -2_500_000, 21);

    var patrimoineZetyAu3juillet24 = new Patrimoine(
            "patrimoineZetyAu3juillet24",
            zety,
            au3juillet24,
            Set.of(ordinateur, vetements, argentEspeces, fraisScolarite, compteBancaire, fraisTenueCompte, donParents, trainDeVie, paiementScolarite));

    assertEquals(-608686, patrimoineZetyAu3juillet24.projectionFuture(au14fevrier25).getValeurComptable());
  }
}