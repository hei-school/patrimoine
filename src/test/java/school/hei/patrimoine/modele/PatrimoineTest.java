package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.GroupePossession;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static java.time.Month.DECEMBER;
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
  void patrimoine_de_Zety_le_17_septembre_2024() {
    var zety = new Personne("Zety");
    var au3juillet24 = LocalDate.of(2024, JULY, 3);
    var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);

    var ordinateur = new Materiel("Ordinateur", au3juillet24, 1_200_000, au3juillet24, -0.10);
    var vetements = new Materiel("Vêtements", au3juillet24, 1_500_000, au3juillet24, -0.50);
    var argent = new Argent("Espèce", au3juillet24, 800_000);
    var fraisScolarite = new FluxArgent("Frais de scolarité", argent, LocalDate.of(2023, NOVEMBER, 27), LocalDate.of(2024, AUGUST, 27), -200_000, 27);
    var compteBancaire = new Argent("Compte bancaire", au3juillet24, 100_000);
    var fraisTenueCompte = new FluxArgent("Frais de tenue de compte", compteBancaire, au3juillet24.withDayOfMonth(25), LocalDate.of(2024, DECEMBER, 25), -20_000, 25);

    var patrimoineZetyAu3juillet24 = new Patrimoine(
            "patrimoineZetyAu3juillet24",
            zety,
            au3juillet24,
            Set.of(ordinateur, vetements, argent, fraisScolarite, compteBancaire, fraisTenueCompte));

    var patrimoineZetyAu17septembre24 = patrimoineZetyAu3juillet24.projectionFuture(au17septembre24);

    assertEquals(2978848, patrimoineZetyAu17septembre24.getValeurComptable());
  }

  @Test
  void patrimoine_de_Zety_entre_17_et_18_septembre_2024() {
    var zety = new Personne("Zety");
    var au3juillet24 = LocalDate.of(2024, JULY, 3);
    var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);
    var au18septembre24 = LocalDate.of(2024, SEPTEMBER, 18);

    var ordinateur = new Materiel("Ordinateur", au3juillet24, 1_200_000, au3juillet24, -0.10);
    var vetements = new Materiel("Vêtements", au3juillet24, 1_500_000, au3juillet24, -0.50);
    var argent = new Argent("Espèce", au3juillet24, 800_000);
    var fraisScolarite = new FluxArgent("Frais de scolarité", argent, LocalDate.of(2023, NOVEMBER, 27), LocalDate.of(2024, AUGUST, 27), -200_000, 27);
    var compteBancaire = new Argent("Compte bancaire", au3juillet24, 100_000);
    var fraisTenueCompte = new FluxArgent("Frais de tenue de compte", compteBancaire, au3juillet24.withDayOfMonth(25), LocalDate.of(2024, JULY, 25), -20_000, 25);

    var patrimoineZetyAu3juillet24 = new Patrimoine(
            "patrimoineZetyAu3juillet24",
            zety,
            au3juillet24,
            Set.of(ordinateur, vetements, argent, fraisScolarite, compteBancaire, fraisTenueCompte));

    var patrimoineZetyAu17septembre24 = patrimoineZetyAu3juillet24.projectionFuture(au17septembre24);

    var banque = new Argent("Banque", au18septembre24, 10_000_000);
    var dette = new FluxArgent("Dette", banque, au18septembre24, au18septembre24.plusYears(1), 11_000_000, 0);

    var patrimoineZetyAu18septembre24 = new Patrimoine(
            "patrimoineZetyAu18septembre24",
            zety,
            au18septembre24,
            Set.of(ordinateur, vetements, argent, fraisScolarite, compteBancaire, fraisTenueCompte, banque, dette));

    double diminutionPatrimoine = patrimoineZetyAu17septembre24.getValeurComptable() - patrimoineZetyAu18septembre24.getValeurComptable();

    assertEquals(-1.0601152E7, diminutionPatrimoine);
  }


  @Test
  void date_fin_espèces_de_Zety() {
    var zety = new Personne("Zety");
    var au3juillet2024 = LocalDate.of(2024, JULY, 3);
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
            LocalDate.of(2023, NOVEMBER, 27),
            LocalDate.of(2024, AUGUST, 27),
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
    do

    {
      var valeurPatrimoine = patrimoineZety.projectionFuture(dateProjection).getValeurComptable();
      assertTrue(argentEspeces.valeurComptableFuture(dateProjection) >= 0);
      dateProjection = dateProjection.plusDays(1);
      argentEspeces = new Argent("Espèces", dateProjection, argentEspeces.getValeurComptable());
    }
    while(argentEspeces.getValeurComptable()<=0);

    assertEquals(LocalDate.of(2024, JULY, 4),dateProjection);
  }


  @Test
  void patrimoine_de_Zety_le_14_février_2025() {
    var zety = new Personne("Zety");
    var au3juillet24 = LocalDate.of(2024, JULY, 3);
    var au14fevrier25 = LocalDate.of(2025, FEBRUARY, 14);

    var ordinateur = new Materiel("Ordinateur", au3juillet24, 1_200_000, au3juillet24, -0.10);
    var vetements = new Materiel("Vêtements", au3juillet24, 1_500_000, au3juillet24, -0.50);
    var argent = new Argent("Espèces", au3juillet24, 800_000);
    var fraisScolarite = new FluxArgent("Frais de scolarité", argent, LocalDate.of(2023, NOVEMBER, 27), LocalDate.of(2024, AUGUST, 27), -200_000, 27);
    var compteBancaire = new Argent("Compte bancaire", au3juillet24, 100_000);
    var fraisTenueCompte = new FluxArgent("Frais de tenue de compte", compteBancaire, au3juillet24.withDayOfMonth(25), LocalDate.of(2024, JULY, 25), -20_000, 25);

    var patrimoineZetyAu3juillet24 = new Patrimoine(
            "patrimoineZetyAu3juillet24",
            zety,
            au3juillet24,
            Set.of(ordinateur, vetements, argent, fraisScolarite, compteBancaire, fraisTenueCompte));

    var patrimoineZetyAu14fevrier25 = patrimoineZetyAu3juillet24.projectionFuture(au14fevrier25);

    assertEquals(2_641_314, patrimoineZetyAu14fevrier25.getValeurComptable());
  }

}