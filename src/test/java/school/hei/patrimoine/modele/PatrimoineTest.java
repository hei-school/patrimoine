package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
  void patrimoine_de_zety_au_17_septembre(){
    var zety = new Personne("Zety");
    var au03juillet24 = LocalDate.of(2024, JULY, 3);
    var argent = new Argent("Espèces", au03juillet24, 800_000);
    var rapportDeTauxdAppreciationJournaliere = 365;
    var ordinateur = new Materiel(
            "Thinkpad",
            au03juillet24,
            1_200_000,
            au03juillet24,
            -0.10 / rapportDeTauxdAppreciationJournaliere);
    var vetements = new Materiel(
            "Vetements",
            au03juillet24,
            1_500_000,
            au03juillet24.minusDays(2),
            -0.50 / rapportDeTauxdAppreciationJournaliere
    );
    var financeur = new Argent("Compte bancaire", au03juillet24, 100_000);
    var aunovembre23 = LocalDate.of(2023, NOVEMBER, 27);
    var enaout24 = LocalDate.of(2024, AUGUST, 27);

    var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);

    var fraisDeScolarite = new FluxArgent(
            "Frais de scolarite",
            argent, aunovembre23, enaout24, -200_000,
            27);

    var fraisDeCompte = new FluxArgent(
            "frais de tenue de compte",
            financeur, au03juillet24, au17septembre24,
            -20_000, 25
    );

    var patrimoineZetyAu03Juillet24 = new Patrimoine(
            "patrimoineZetyAu03Juillet24",
            zety,
            au03juillet24,
            Set.of(ordinateur, vetements, argent, financeur,fraisDeScolarite, fraisDeCompte)
    );

    assertEquals(3_159_503, patrimoineZetyAu03Juillet24.projectionFuture(au17septembre24).getValeurComptable());
  }

  @Test
  public void zety_s_endette(){
    var zety = new Personne("Zety");
    var au03juillet24 = LocalDate.of(2024, JULY, 3);
    var argent = new Argent("Espèces", au03juillet24, 800_000);
    var rapportDeTauxdAppreciationJournaliere = 365;

    var ordinateur = new Materiel(
            "Thinkpad",
            au03juillet24,
            1_200_000,
            au03juillet24.minusDays(2),
            -0.10 / rapportDeTauxdAppreciationJournaliere);

    var vetements = new Materiel(
            "Vetements",
            au03juillet24,
            1_500_000,
            au03juillet24.minusDays(2),
            -0.50 / rapportDeTauxdAppreciationJournaliere
    );
    var financeur = new Argent("Compte bancaire", au03juillet24, 100_000);
    var aunovembre23 = LocalDate.of(2023, NOVEMBER, 27);
    var enaout24 = LocalDate.of(2024, AUGUST, 27);

    var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);

    var fraisDeScolarite = new FluxArgent(
            "Frais de scolarite",
            financeur, aunovembre23, enaout24, -200_000,
            27);

    var fraisDeCompte = new FluxArgent(
            "frais de tenue de compte",
            financeur, au03juillet24, au17septembre24,
            -20_000, 25
    );

    var au18septembre24 = LocalDate.of(2024, SEPTEMBER, 18);

    var detteDeZety = new Dette(
            "dette",
            au18septembre24,
            -10_000_000
    );

    var coutDuPret = new Argent(
            "cout du pret", au18septembre24,
            -1_000_000
    );

    var patrimoineZetyAu03Juillet24 = new Patrimoine(
            "patrimoineZetyAu03Juillet24",
            zety,
            au03juillet24,
            Set.of(ordinateur, vetements, argent, financeur,fraisDeScolarite, fraisDeCompte)
    );

    var financeurau18septembre = new Argent("Compte bancaire", au03juillet24, financeur.getValeurComptable() + 10_000_000);

    var patrimoineZetyAu18septembre24 = new Patrimoine(
            "patrimoineZetyAu18septembre24",
            zety,
            au18septembre24,
            Set.of(ordinateur.projectionFuture(au18septembre24), vetements.projectionFuture(au18septembre24), argent.projectionFuture(au18septembre24), financeurau18septembre, fraisDeCompte, detteDeZety, coutDuPret)
    );
    assertEquals(560_007, patrimoineZetyAu03Juillet24.projectionFuture(au17septembre24).getValeurComptable() - patrimoineZetyAu18septembre24.getValeurComptable());
  }

  @Test
  public void zety_n_a_plus_d_espece(){
    var zety = new Personne("Zety");
    var au03juillet24 = LocalDate.of(2024, JULY, 3);
    var argent = new Argent("Espèces", au03juillet24, 800_000);
    var rapportDeTauxdAppreciationJournaliere = 365;

    var ordinateur = new Materiel(
            "Thinkpad",
            au03juillet24,
            1_200_000,
            au03juillet24.minusDays(2),
            -0.10 / rapportDeTauxdAppreciationJournaliere);

    var vetements = new Materiel(
            "Vetements",
            au03juillet24,
            1_500_000,
            au03juillet24.minusDays(2),
            -0.50 / rapportDeTauxdAppreciationJournaliere
    );
    var financeur = new Argent("Compte bancaire", au03juillet24, 100_000);

    var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);

    var fraisDeCompte = new FluxArgent(
            "frais de tenue de compte",
            financeur, au03juillet24, au17septembre24,
            -20_000, 25
    );

    var au18septembre24 = LocalDate.of(2024, SEPTEMBER, 18);

    var detteDeZety = new Dette(
            "dette",
            au18septembre24,
            -10_000_000
    );

    var coutDuPret = new Argent(
            "cout du pret", au18septembre24,
            -1_000_000
    );

    var financeurau18septembre = new Argent("Compte bancaire", au03juillet24, financeur.getValeurComptable() + 10_000_000);

    var au21septembre24 = LocalDate.of(2024, SEPTEMBER, 21);

    var ecolage2425 = new FluxArgent(
            "ecolage 2024 - 2025",
            financeurau18septembre.projectionFuture(au21septembre24),
            au21septembre24,
            au21septembre24,
            2_500_000,
            21
    );

    var argentDesParentsDeZety = new Argent(
            "argent des parents de zety",
            LocalDate.of(2024, JANUARY, 1),
            100_000
    );

    var donDesParentsDeZety = new TransfertArgent(
            "don",
            argentDesParentsDeZety,
            argent,
            LocalDate.of(2024, JANUARY, 1),
            LocalDate.of(2025, FEBRUARY, 25),
            100_000,
            15
    );

    var trainDeVie = new FluxArgent(
            "train de vie de zety",
            argent,
            LocalDate.of(2024, OCTOBER, 1),
            LocalDate.of(2025, FEBRUARY, 13),
            -250_000,
            1
    );

    var patrimoineZetyAu01OCtobre24 = new Patrimoine(
            "patrimoineZetyAu01Octobre24",
            zety,
            LocalDate.of(2024, OCTOBER, 1),
            Set.of(ordinateur.projectionFuture(LocalDate.of(2024, OCTOBER, 1)), vetements.projectionFuture(LocalDate.of(2024, OCTOBER, 1)), donDesParentsDeZety, trainDeVie, ecolage2425, financeurau18septembre, fraisDeCompte, detteDeZety, coutDuPret)
    );

    LocalDate dateZeroEspecesAttendu = LocalDate.of(2025, JANUARY, 1);
    LocalDate dateDeProjectionActuel =  LocalDate.of(2024, OCTOBER, 1);

    var especesProjetees = trainDeVie.getArgent().projectionFuture(dateDeProjectionActuel);
    long maxIntervals = ChronoUnit.DAYS.between(dateDeProjectionActuel, dateZeroEspecesAttendu);

    for (int i = 0; i <= maxIntervals; i++) {
      dateDeProjectionActuel = dateDeProjectionActuel.plusDays(i);

      especesProjetees = trainDeVie.getArgent().projectionFuture(dateDeProjectionActuel);

      if (especesProjetees.getValeurComptable() == 0) {
        break;
      }
    }
    assertEquals(dateZeroEspecesAttendu, dateDeProjectionActuel);
    System.out.println(trainDeVie.getArgent().projectionFuture( LocalDate.of(2025, FEBRUARY, 13)).getValeurComptable());
  }

  @Test
  public void patrimoine_de_zety_au_14Fevrier25(){
    var zety = new Personne("Zety");
    var au03juillet24 = LocalDate.of(2024, JULY, 3);
    var argent = new Argent("Espèces", au03juillet24, 800_000);
    var rapportDeTauxdAppreciation_journaliere = 365;

    var ordinateur = new Materiel(
            "Thinkpad",
            au03juillet24,
            1_200_000,
            au03juillet24.minusDays(2),
            -0.10 / rapportDeTauxdAppreciation_journaliere);

    var vetements = new Materiel(
            "Vetements",
            au03juillet24,
            1_500_000,
            au03juillet24.minusDays(2),
            -0.50 / rapportDeTauxdAppreciation_journaliere
    );
    var financeur = new Argent("Compte bancaire", au03juillet24, 100_000);

    var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);

    var fraisDeCompte = new FluxArgent(
            "frais de tenue de compte",
            financeur, au03juillet24, au17septembre24,
            -20_000, 25
    );

    var au18septembre24 = LocalDate.of(2024, SEPTEMBER, 18);

    var detteDeZety = new Dette(
            "dette",
            au18septembre24,
            -10_000_000
    );

    var coutDuPret = new Argent(
            "cout du pret", au18septembre24,
            -1_000_000
    );

    var financeurau18septembre = new Argent("Compte bancaire", au03juillet24, financeur.getValeurComptable() + 10_000_000);

    var au21septembre24 = LocalDate.of(2024, SEPTEMBER, 21);

    var ecolage2425 = new FluxArgent(
            "ecolage 2024 - 2025",
            financeurau18septembre,
            au21septembre24,
            au21septembre24,
            2_500_000,
            21
    );

    var argentdesparentsdezety = new Argent(
            "argent des parents de zety",
            LocalDate.of(2024, JANUARY, 1),
            100_000
    );

    var dondesparentsdezety = new TransfertArgent(
            "don",
            argentdesparentsdezety,
            argent,
            LocalDate.of(2024, JANUARY, 1),
            LocalDate.of(2025, FEBRUARY, 25),
            100_000,
            15
    );

    var trainDeVie = new FluxArgent(
            "train de vie de zety",
            argent,
            LocalDate.of(2024, OCTOBER, 1),
            LocalDate.of(2025, FEBRUARY, 13),
            250_000,
            1
    );

    var patrimoineZetyAu01OCtobre24 = new Patrimoine(
            "patrimoineZetyAu01Octobre24",
            zety,
            LocalDate.of(2024, OCTOBER, 1),
            Set.of(ordinateur.projectionFuture(LocalDate.of(2024, OCTOBER, 1)), vetements.projectionFuture(LocalDate.of(2024, OCTOBER, 1)), dondesparentsdezety, trainDeVie, ecolage2425, financeurau18septembre, fraisDeCompte, detteDeZety, coutDuPret)
    );

    var patrimoineDeZetyAu14Fevrier2025 = patrimoineZetyAu01OCtobre24.projectionFuture(LocalDate.of(2025, FEBRUARY, 15));

    assertEquals(4_298_516, patrimoineDeZetyAu14Fevrier2025.getValeurComptable());
  }

  @Test
  public void zety_part_en_allemagne(){
    var zety = new Personne("Zety");
    var au03juillet24 = LocalDate.of(2024, JULY, 3);
    var argent = new Argent("Espèces", au03juillet24, 800_000);
    var rapportDeTauxdAppreciation_journaliere = 365;

    var ordinateur = new Materiel(
            "Thinkpad",
            au03juillet24,
            1_200_000,
            au03juillet24.minusDays(2),
            -0.10 / rapportDeTauxdAppreciation_journaliere);

    var vetements = new Materiel(
            "Vetements",
            au03juillet24,
            1_500_000,
            au03juillet24.minusDays(2),
            -0.50 / rapportDeTauxdAppreciation_journaliere
    );
    var financeur = new Argent("Compte bancaire", au03juillet24, 100_000);

    var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);

    var fraisDeCompte = new FluxArgent(
            "frais de tenue de compte",
            financeur, au03juillet24, au17septembre24,
            -20_000, 25
    );

    var au18septembre24 = LocalDate.of(2024, SEPTEMBER, 18);

    var detteDeZety = new Dette(
            "dette",
            au18septembre24,
            -10_000_000
    );

    var coutDuPret = new Argent(
            "cout du pret", au18septembre24,
            -1_000_000
    );

    var financeurau18septembre = new Argent("Compte bancaire", au03juillet24, financeur.getValeurComptable() + 10_000_000);

    var au21septembre24 = LocalDate.of(2024, SEPTEMBER, 21);

    var ecolage2425 = new FluxArgent(
            "ecolage 2024 - 2025",
            financeurau18septembre,
            au21septembre24,
            au21septembre24,
            2_500_000,
            21
    );

    var argentdesparentsdezety = new Argent(
            "argent des parents de zety",
            LocalDate.of(2024, JANUARY, 1),
            100_000
    );

    var dondesparentsdezety = new TransfertArgent(
            "don",
            argentdesparentsdezety,
            argent,
            LocalDate.of(2024, JANUARY, 1),
            LocalDate.of(2025, FEBRUARY, 25),
            100_000,
            15
    );

    var trainDeVie = new FluxArgent(
            "train de vie de zety",
            argent,
            LocalDate.of(2024, OCTOBER, 1),
            LocalDate.of(2025, FEBRUARY, 13),
            250_000,
            1
    );

    var patrimoineZetyAu01OCtobre24 = new Patrimoine(
            "patrimoineZetyAu01Octobre24",
            zety,
            LocalDate.of(2024, OCTOBER, 1),
            Set.of(ordinateur.projectionFuture(LocalDate.of(2024, OCTOBER, 1)), vetements.projectionFuture(LocalDate.of(2024, OCTOBER, 1)), dondesparentsdezety, trainDeVie, ecolage2425, financeurau18septembre, fraisDeCompte, detteDeZety, coutDuPret)
    );

    var patrimoineDeZetyAu14Fevrier2025 = patrimoineZetyAu01OCtobre24.projectionFuture(LocalDate.of(2025, FEBRUARY, 15));


  }
}