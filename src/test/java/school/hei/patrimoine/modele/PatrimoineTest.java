package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.GroupePossession;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PatrimoineTest {

  @Test
  void patrimoine_vide_vaut_0() {
    var ilo = new Personne("Ilo");
    var zety = new Personne("Zety");

    var patrimoineZetyAu13Juillet24 = new Patrimoine(
            " patrimoineZetyAu13Juillet24",
            zety,LocalDate.of(2024,JULY,13),
            Set.of());

    var patrimoineIloAu13mai24 = new Patrimoine(
        "patrimoineIloAu13mai24",
        ilo,
        LocalDate.of(2024, MAY, 13),
        Set.of());

    assertEquals(0, patrimoineIloAu13mai24.getValeurComptable());
    assertEquals(0,patrimoineZetyAu13Juillet24.getValeurComptable());
  }

  @Test
  void patrimoine_a_de_l_argent() {
    var ilo = new Personne("Ilo");
    var zety = new Personne("Zety");

    var au13Juillet2024 = LocalDate.of(2024,JULY,13);
    var patrimoineZetyAu13Juillet24 = new Patrimoine(
            " patrimoineZetyAu13Juillet24",
            zety,LocalDate.of(2024,JULY,13),
            Set.of(
                    new Argent("Espèces", au13Juillet2024, 800_000),
                    new Argent("compte bancaire",au13Juillet2024,100_000)));

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
    assertEquals(900_000,patrimoineZetyAu13Juillet24.getValeurComptable());
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
    var Zety = new Personne("Zety");
    var au3juillet2024 = LocalDate.of(2024, JULY, 3);
    var au17Sept24 = LocalDate.of(2024, SEPTEMBER, 17);
    var pc = new Materiel(
            "ordinateur",
            au3juillet2024,
            1_200_000,
            au3juillet2024,
            -0.10);

    var vetements = new Materiel(
            "vêtements",
            au3juillet2024,
            1_500_000,
            au3juillet2024,
            -0.50);

    var argentEspeces = new Argent(
            "argent en espèces",
            au3juillet2024,
            800_000);

    int totalFraisScolarite = 0;
    var au27Juillet24 = LocalDate.of(2024, JULY, 27);
    var au27Aout24 = LocalDate.of(2024, AUGUST, 27);
    var datePaiement = au27Juillet24;
    while (!datePaiement.isAfter(au27Aout24)) {
      totalFraisScolarite += 200_000;
      datePaiement = datePaiement.plusMonths(1);
    }
    var fraisDeScolarite = new Argent("frais de scolarité", au3juillet2024, totalFraisScolarite);

    var fraisTenueCompte = 0;
    var debutCompte = LocalDate.of(2024, JULY, 25);
    var datePonction = debutCompte;
    while (!datePonction.isAfter(au17Sept24)) {
      fraisTenueCompte += 20_000;
      datePonction = datePonction.plusMonths(1);
    }

    var compteBancaire = new Argent(
            "compte bancaire",
            au3juillet2024,
            100_000 - fraisTenueCompte);

    var au17septembre2024 = LocalDate.of(2024, SEPTEMBER, 17);

    var patrimoine_de_zety = new Patrimoine(
            "patrimoineDeZety",
            Zety, au17septembre2024,
            Set.of(pc, vetements, compteBancaire, fraisDeScolarite));

    assertEquals(2_978_848, patrimoine_de_zety.projectionFuture(au17septembre2024).getValeurComptable());
  }

  @Test
  public void testPatrimoineZetyAu14Fevrier2025() {
    var patrimoine = initPatrimoineAu13Fevrier2025();

    var dateFin = LocalDate.of(2025, FEBRUARY, 14);
    patrimoine = patrimoine.projectionFuture(dateFin);

    var valeurPatrimoineAttendue = 0;
    var valeurPatrimoineActuelle = patrimoine.getValeurComptable();
    assertEquals(valeurPatrimoineAttendue, valeurPatrimoineActuelle);
  }

  // Initialisation du patrimoine de Zety au 13 février 2025
  private Patrimoine initPatrimoineAu13Fevrier2025() {
    var argent = new Argent(
            "Espèces",
            LocalDate.of(2025, FEBRUARY, 13),
            0);

    return new Patrimoine(
            "Zety",
            new Personne("Zety"),
            LocalDate.of(2025,FEBRUARY, 13),
            Set.of(argent));
  }
}