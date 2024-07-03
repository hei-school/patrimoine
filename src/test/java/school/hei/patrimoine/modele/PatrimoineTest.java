package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

import static java.time.Month.MAY;
import static java.util.Calendar.SEPTEMBER;
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
  void Patrimoine_17_Septembre_2024_de_Zety() {
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
            "Frais tenue de compte",
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
  void diminution_Patrimoine_de_Zety(){
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
            "Frais  tenue de compte",
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

    var au17Septembre2024 = LocalDate.of(2024, Month.SEPTEMBER, 17);
    var au18Septembre2024 = LocalDate.of(2024, Month.SEPTEMBER, 18);
    var valeurPatrimoineAu17Septembre2024 = patrimoineZety.projectionFuture(au17Septembre2024).getValeurComptable();
    var valeurPatrimoineAu18Septembre2024 = patrimoineZety.projectionFuture(au18Septembre2024).getValeurComptable();
    var diminutionPatrimoine = valeurPatrimoineAu18Septembre2024 - valeurPatrimoineAu17Septembre2024;
    assertEquals(-2384, diminutionPatrimoine);
  }
  @Test
  void zety_date_faillite() {
    var zety = new Personne("Zety");
    var dateDebut = LocalDate.of(2024, Month.JULY, 3);
    var argentEspeces = new Argent("Espèces", dateDebut, 800_000);
    var ordinateur = new Materiel(
            "Ordinateur",
            dateDebut,
            1_200_000,
            dateDebut.minusDays(2),
            -0.10);
    var vetements = new Materiel(
            "Vêtements",
            dateDebut,
            1_500_000,
            dateDebut.minusDays(2),
            -0.50);
    var fraisScolarite = new FluxArgent(
            "Frais de scolarité",
            argentEspeces,
            LocalDate.of(2023, Month.NOVEMBER, 27),
            LocalDate.of(2024, Month.AUGUST, 27),
            -200_000,
            30);
    var compteBancaire = new Argent("Compte bancaire", dateDebut, 100_000);
    var fraisTenueCompte = new FluxArgent(
            "Frais tenue de compte",
            compteBancaire,
            dateDebut.minusMonths(1),
            dateDebut.plusYears(1),
            -20_000,
            30);

    var patrimoineZety = new Patrimoine(
            "Patrimoine de Zety",
            zety,
            dateDebut,
            Set.of(argentEspeces, ordinateur, vetements, fraisScolarite, compteBancaire, fraisTenueCompte));

    LocalDate dateSimulation = dateDebut;
    while (true) {
      if (dateSimulation.equals(LocalDate.of(2024, Month.SEPTEMBER, 21))) {
        argentEspeces = new Argent("Espèces", dateSimulation, argentEspeces.getValeurComptable() - 2_500_000);
      }
      if (dateSimulation.getDayOfMonth() == 15 && dateSimulation.getMonthValue() >= 1 && dateSimulation.getYear() == 2024) {
        argentEspeces = new Argent("Espèces", dateSimulation, argentEspeces.getValeurComptable() + 100_000);
      }
      if (dateSimulation.getMonthValue() >= 10 && dateSimulation.getDayOfMonth() == 1 &&
              dateSimulation.isBefore(LocalDate.of(2025, Month.FEBRUARY, 14))) {
        argentEspeces = new Argent("Espèces", dateSimulation, argentEspeces.getValeurComptable() - 250_000);
      }
      if (argentEspeces.getValeurComptable() <= 0) {
        break;
      }
      dateSimulation = dateSimulation.plusDays(1);
    }
    assertEquals(LocalDate.of(2024, Month.SEPTEMBER, 21), dateSimulation);
  }
  @Test
  void Patrimoine_Zety_14_Fevrier_2025() {
    var zety = new Personne("Zety");
    var dateDebut = LocalDate.of(2024, Month.JULY, 3);
    var argentEspeces = new Argent("Espèces", dateDebut, 800_000);
    var ordinateur = new Materiel(
            "Ordinateur",
            dateDebut,
            1_200_000,
            dateDebut.minusDays(2),
            -0.10);

    var vetements = new Materiel(
            "Vêtements",
            dateDebut,
            1_500_000,
            dateDebut.minusDays(2),
            -0.50);

    var fraisScolarite = new FluxArgent(
            "Frais de scolarité",
            argentEspeces,
            LocalDate.of(2023, Month.NOVEMBER, 27),
            LocalDate.of(2024, Month.AUGUST, 27),
            -200_000,
            30);

    var compteBancaire = new Argent("Compte bancaire", dateDebut, 100_000);

    var fraisTenueCompte = new FluxArgent(
            "Frais de tenue de compte",
            compteBancaire,
            dateDebut.minusMonths(1),
            dateDebut.plusYears(1),
            -20_000,
            30);

    var patrimoineZety = new Patrimoine(
            "Patrimoine de Zety",
            zety,
            dateDebut,
            Set.of(argentEspeces, ordinateur, vetements, fraisScolarite, compteBancaire, fraisTenueCompte));

    var dateProjection = LocalDate.of(2025, Month.FEBRUARY, 14);
    var valeurPatrimoineAu14Fevrier2025 = patrimoineZety.projectionFuture(dateProjection).getValeurComptable();

    assertEquals(2721314, valeurPatrimoineAu14Fevrier2025);
  }
}