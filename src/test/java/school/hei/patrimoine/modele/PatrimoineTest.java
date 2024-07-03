package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.time.Year;
import java.util.Set;

import static java.time.Month.*;
import static java.time.temporal.ChronoUnit.MONTHS;
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
    var rapport_de_taux_d_appreciation_journaliere = 365;
    var ordinateur = new Materiel(
            "Thinkpad",
            au03juillet24,
            1_200_000,
            au03juillet24.minusDays(2),
            -0.10 / rapport_de_taux_d_appreciation_journaliere);
    var vetements = new Materiel(
            "Vetements",
            au03juillet24,
            1_500_000,
            au03juillet24.minusDays(2),
            -0.50 / rapport_de_taux_d_appreciation_journaliere
    );
    var financeur = new Argent("Compte bancaire", au03juillet24, 100_000);
    var aunovembre23 = LocalDate.of(2023, NOVEMBER, 27);
    var enaout24 = LocalDate.of(2024, AUGUST, 27);

    var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);

    var frais_de_scolarite = new FluxArgent(
            "Frais de scolarite",
            financeur, aunovembre23, enaout24, -200_000,
            27);

    var frais_de_compte = new FluxArgent(
            "frais de tenue de compte",
            financeur, au03juillet24, au17septembre24,
            -20_000, 25
    );

    var patrimoineZetyAu03Juillet24 = new Patrimoine(
            "patrimoineZetyAu03Juillet24",
            zety,
            au03juillet24,
            Set.of(ordinateur, vetements, argent, financeur,frais_de_scolarite, frais_de_compte)
    );

    assertEquals(3_159_503, patrimoineZetyAu03Juillet24.projectionFuture(au17septembre24).getValeurComptable());
  }

  @Test
  public void zety_s_endette(){
    var zety = new Personne("Zety");
    var au03juillet24 = LocalDate.of(2024, JULY, 3);
    var argent = new Argent("Espèces", au03juillet24, 800_000);
    var rapport_de_taux_d_appreciation_journaliere = 365;

    var ordinateur = new Materiel(
            "Thinkpad",
            au03juillet24,
            1_200_000,
            au03juillet24.minusDays(2),
            -0.10 / rapport_de_taux_d_appreciation_journaliere);

    var vetements = new Materiel(
            "Vetements",
            au03juillet24,
            1_500_000,
            au03juillet24.minusDays(2),
            -0.50 / rapport_de_taux_d_appreciation_journaliere
    );
    var financeur = new Argent("Compte bancaire", au03juillet24, 100_000);
    var aunovembre23 = LocalDate.of(2023, NOVEMBER, 27);
    var enaout24 = LocalDate.of(2024, AUGUST, 27);

    var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);

    var frais_de_scolarite = new FluxArgent(
            "Frais de scolarite",
            financeur, aunovembre23, enaout24, -200_000,
            27);

    var frais_de_compte = new FluxArgent(
            "frais de tenue de compte",
            financeur, au03juillet24, au17septembre24,
            -20_000, 25
    );

    var au18septembre24 = LocalDate.of(2024, SEPTEMBER, 18);

    var dette_de_zety = new Dette(
            "dette",
            au18septembre24,
            -10_000_000
    );

    var cout_du_pret = new Argent(
            "cout du pret", au18septembre24,
            -1_000_000
    );

    var patrimoineZetyAu03Juillet24 = new Patrimoine(
            "patrimoineZetyAu03Juillet24",
            zety,
            au03juillet24,
            Set.of(ordinateur, vetements, argent, financeur,frais_de_scolarite, frais_de_compte)
    );

    var financeurau18septembre = new Argent("Compte bancaire", au03juillet24, financeur.getValeurComptable() + 10_000_000);

    var patrimoineZetyAu18septembre24 = new Patrimoine(
            "patrimoineZetyAu18septembre24",
            zety,
            au18septembre24,
            Set.of(ordinateur.projectionFuture(au18septembre24), vetements.projectionFuture(au18septembre24), argent.projectionFuture(au18septembre24), financeurau18septembre, frais_de_compte, dette_de_zety, cout_du_pret)
    );

    assertEquals(560_007, patrimoineZetyAu03Juillet24.projectionFuture(au17septembre24).getValeurComptable() - patrimoineZetyAu18septembre24.getValeurComptable());
  }
}