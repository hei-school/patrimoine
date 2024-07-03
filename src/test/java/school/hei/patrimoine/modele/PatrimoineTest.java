package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.GroupePossession;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.JULY;
import static java.time.Month.MAY;
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




  /* 1ere Partie */

  @Test
  void patrimoine_zety_17_septembre () {
    var zety = new Personne("Zety");
    var au3juillet2024 = LocalDate.of(2024, JULY, 3);
    var financeur = new Argent("Espèces", au3juillet2024.minusDays(0), au3juillet2024, 200000);
    var fraisSup = new Argent("Espèces", au3juillet2024.minusDays(0), au3juillet2024, -1526357);
    var fraisInf = new Argent("Espèces", au3juillet2024.minusDays(0), au3juillet2024, -200000);

    var ordinateur = new Materiel(
            "MacBook Pro",
            au3juillet2024,
            1_200_000,
            au3juillet2024.minusDays(46),
            5);

    var vetement = new Materiel(
            "Vetement",
            au3juillet2024,
            1_500_000,
            au3juillet2024.minusDays(46),
            1);


    var patrimoineZetyAu17septembre24 = new Patrimoine(
            "patrimoineZetyAu17septembre24",
            zety,
            au3juillet2024,
            Set.of(
                    new Argent("Espèces", au3juillet2024, 800_000),
                    new Argent("Compte bancaire", au3juillet2024, 60_000),
                    new GroupePossession("Le groupe", au3juillet2024, Set.of(financeur, ordinateur, vetement, fraisSup, fraisInf))
            )
    );

    assertEquals(
            2_978_848, patrimoineZetyAu17septembre24.projectionFuture(au3juillet2024.plusDays(46)).getValeurComptable());

  }


  /* 2eme Partie */

  @Test
  void patrimoine_zety_entre_17et18_septembre () {
    var zety = new Personne("Zety");
    var au3juillet2024 = LocalDate.of(2024, JULY, 3);
    var financeur = new Argent("Espèces", au3juillet2024.minusDays(0), au3juillet2024, 200000);
    var fraisSup = new Argent("Espèces", au3juillet2024.minusDays(0), au3juillet2024, -1526357);
    var fraisInf = new Argent("Espèces", au3juillet2024.minusDays(0), au3juillet2024, -200000);
    var requeteBanque = new Argent("Espèces", au3juillet2024.minusDays(0), au3juillet2024, -1976464);
    var dete = new Argent("Espèces", au3juillet2024.minusDays(0), au3juillet2024, -10000000);

    var ordinateur = new Materiel(
            "MacBook Pro",
            au3juillet2024,
            1_200_000,
            au3juillet2024.minusDays(46),
            5);

    var vetement = new Materiel(
            "Vetement",
            au3juillet2024,
            1_500_000,
            au3juillet2024.minusDays(46),
            1);


    var patrimoineZetyAu17septembre24 = new Patrimoine(
            "patrimoineZetyAu17septembre24",
            zety,
            au3juillet2024,
            Set.of(
                    new Argent("Espèces", au3juillet2024, 800_000),
                    new Argent("Compte bancaire", au3juillet2024, 10_060_000),
                    new GroupePossession("Le groupe", au3juillet2024, Set.of(requeteBanque ,financeur, ordinateur, vetement, fraisSup, fraisInf, dete))
            )
    );

    assertEquals(
            1002384, patrimoineZetyAu17septembre24.projectionFuture(au3juillet2024.plusDays(46)).getValeurComptable());

  }
}