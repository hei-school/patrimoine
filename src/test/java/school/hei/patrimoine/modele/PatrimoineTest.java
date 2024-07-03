package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
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
  void zety_etudie_2023_2024(){
    var zety = new Personne("Zety");
    var au3juillet24 = LocalDate.of(2024, JULY, 3);

    var ordinateur = new Materiel(
            "Ordinateur",
            au3juillet24,
            1_200_000,
            au3juillet24,
            -0.10);

    var vetements = new Materiel(
            "Vêtements",
            au3juillet24,
            1_500_000,
            au3juillet24,
            -0.50);

    var espece = new Argent("espèce", au3juillet24, 800_000);

    var debutScolarite = LocalDate.of(2023, NOVEMBER, 1);
    var finScolarite = LocalDate.of(2024, AUGUST, 30);
    var fraisDeScolarite = new FluxArgent(
            "Frais de Scolarité",
            espece,
            debutScolarite,
            finScolarite,
            -200_000,
            27);

    var compteBancaire = new Argent("CompteBancaire", au3juillet24, 100_000);
    var fraisDuCompte = new FluxArgent(
            "Frais de compte",
            compteBancaire,
            au3juillet24,
            LocalDate.MAX,
            -20_000,
            25);

    var patrimoineZetyAu3juillet24 = new Patrimoine(
            "patrimoineZetyAu3juillet24",
            zety,
            au3juillet24,
            Set.of(ordinateur,
                    vetements,
                    espece,
                    fraisDeScolarite,
                    compteBancaire,
                    fraisDuCompte
            )
    );

    var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);
    assertTrue(patrimoineZetyAu3juillet24.getValeurComptable() > patrimoineZetyAu3juillet24.projectionFuture(au17septembre24).getValeurComptable());
    assertEquals(2_978_848, patrimoineZetyAu3juillet24.projectionFuture(au17septembre24).getValeurComptable());
  }
}