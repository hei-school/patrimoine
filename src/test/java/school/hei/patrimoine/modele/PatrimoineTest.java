package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import static java.time.Month.*;
import static java.time.Month.JULY;
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
    var au17Septembre = LocalDate.of(2024, SEPTEMBER, 17);
    var ordinateur = new Materiel("Ordinateur", LocalDate.of(2024, JULY, 3), 1_200_000, LocalDate.of(2024, 7, 3), -0.10);
    var vetements = new Materiel("Vêtements", LocalDate.of(2024, JULY, 3), 1_500_000, LocalDate.of(2024, 7, 3), -0.50);
    var argentEnEspece = new Argent("Argent en espèces", LocalDate.of(2024, JULY, 3), 800_000);
    var compteBancaire = new Argent("Compte bancaire", LocalDate.of(2024, JULY, 3), 100_000);
    var fraisTenueCompte = new FluxArgent("Frais de tenue de compte", compteBancaire, LocalDate.of(2024, JULY, 3), LocalDate.of(2024, 7, 3).plusMonths(12), -20_000, 25);
    var fraisScolarite = new Argent("Frais de scolarité", LocalDate.of(2024, 7, 3), 0);
    var fluxFraisScolarite = new FluxArgent("Frais de scolarité", fraisScolarite, LocalDate.of(2023, 11, 27), LocalDate.of(2024, 8, 27), -200_000, 27);

    var patrimoineZety = new Patrimoine("Patrimoine Zety", new Personne("Zety"), LocalDate.of(2024, 7, 3), Set.of(ordinateur, vetements, argentEnEspece, compteBancaire, fraisScolarite, fluxFraisScolarite, fraisTenueCompte));

    var projectionFuture = patrimoineZety.projectionFuture(au17Septembre);
    assertEquals(2_978_848, projectionFuture.getValeurComptable());
  }
  @Test
  void patrimoine_de_zety_apres_emprunt() {
    var zety = new Personne("Zety");
    var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);
    var au18septembre24 = LocalDate.of(2024, SEPTEMBER, 18);

    var patrimoineZetyAu18septembre24 = getPatrimoine(au17septembre24, au18septembre24, zety);

    var evolutionPatrimoine = new EvolutionPatrimoine(
            "Nom",
            patrimoineZetyAu18septembre24,
            au17septembre24,
            au18septembre24);

    var evolutionJournaliere = evolutionPatrimoine.getEvolutionJournaliere();
    assertEquals(-1_000_000, evolutionJournaliere.get(au18septembre24).getValeurComptable() - evolutionJournaliere.get(au17septembre24).getValeurComptable());
  }

  @Test
  void date_ou_zety_n_a_plus_d_espèces() {
    var zety = new Personne("Zety");
    var au1janvier24 = LocalDate.of(2024, JANUARY, 1);
    var au21septembre24 = LocalDate.of(2024, SEPTEMBER, 21);
    var au15janvier24 = LocalDate.of(2024, JANUARY, 15);
    var au1eroctobre24 = LocalDate.of(2024, OCTOBER, 1);
    var au1erfevrier25 = LocalDate.of(2025, FEBRUARY, 1);
    var au31janvier25 = LocalDate.of(2025, JANUARY, 31);

    var compteBancaire = new Argent("Compte bancaire", au1janvier24, 100_000);

    var fraisScolarite = new FluxArgent("Frais de scolarité", compteBancaire, au21septembre24, au21septembre24, -2_500_000, 21);

    var donParents = new FluxArgent("Don des parents", compteBancaire, au15janvier24, au1erfevrier25, 100_000, 15);

    var trainDeVie = new FluxArgent("Train de vie", compteBancaire, au1eroctobre24, au1erfevrier25, -250_000, 1);

    var fraisTenueCompte = new FluxArgent("Frais de tenue de compte", compteBancaire, au1janvier24, au31janvier25, -20_000, 25);

    var patrimoineZetyAu1janvier24 = new Patrimoine(
            "patrimoineZetyAu1janvier24",
            zety,
            au1janvier24,
            Set.of(compteBancaire, fraisScolarite, donParents, trainDeVie, fraisTenueCompte));

    var evolutionPatrimoine = new EvolutionPatrimoine(
            "Nom",
            patrimoineZetyAu1janvier24,
            au1janvier24,
            au31janvier25);

    var evolutionJournaliere = evolutionPatrimoine.getEvolutionJournaliere();

    var dateSansEspèces = evolutionJournaliere.entrySet().stream()
            .filter(entry -> entry.getValue().getValeurComptable() < 0)
            .map(Map.Entry::getKey)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Zety never runs out of cash."));

    assertEquals(LocalDate.of(2025, JANUARY, 31), dateSansEspèces);
  }
  private static Patrimoine getPatrimoine(LocalDate au17septembre24, LocalDate au18septembre24, Personne zety) {
    var compteBancaire = new Argent("Compte bancaire", au17septembre24, 100_000);

    var emprunt = new Dette("Emprunt bancaire", au18septembre24, -11_000_000);
    var fluxEmprunt = new FluxArgent("Emprunt", compteBancaire, au18septembre24, au18septembre24, 10_000_000, au18septembre24.getDayOfMonth());


    var patrimoineZetyAu18septembre24 = new Patrimoine(
            "patrimoineZetyAu18septembre24",
            zety,
            au18septembre24,
            Set.of(compteBancaire, emprunt, fluxEmprunt));
    return patrimoineZetyAu18septembre24;
  }

}