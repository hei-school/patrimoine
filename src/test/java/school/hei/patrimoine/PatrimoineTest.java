package school.hei.patrimoine;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.possession.Argent;
import school.hei.patrimoine.possession.Materiel;
import school.hei.patrimoine.possession.TrainDeVie;

import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PatrimoineTest {

  @Test
  void patrimoine_vide_vaut_0() {
    var ilo = new Personne("Ilo");

    var patrimoineIloAu13mai24 = new Patrimoine(
        ilo,
        Instant.parse("2024-05-13T00:00:00.00Z"),
        Set.of());

    assertEquals(0, patrimoineIloAu13mai24.getValeurComptable());
  }

  @Test
  void patrimoine_a_de_l_argent() {
    var ilo = new Personne("Ilo");

    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var patrimoineIloAu13mai24 = new Patrimoine(
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

    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var financeur = new Argent("Espèces", au13mai24, 400_000);

    var trainDeVie = new TrainDeVie(null, 0, null, null, financeur, 0);

    var patrimoineIloAu13mai24 = new Patrimoine(
        ilo,
        au13mai24,
        Set.of(financeur, trainDeVie));
  }

  @Test
  void patrimoine_ilo_le_26_juin_2024() {
    var ilo = new Personne("Ilo");

    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var especes = new Argent("Espèces", au13mai24, 400_000);
    var compteEpargne = new Argent("Compte epargne", au13mai24, 200_000);
    var compteCourant = new Argent("Compte courant", au13mai24, 600_000);

    var ordinateur = new Materiel("Ordinateur", au13mai24, 2_000_000, -0.10);
    var effetsVestimentaires = new Materiel(
            "Effets vestimentaires",
            au13mai24,
            1_000_000,
            -0.20);

    var aLOuvertureDeHEI = Instant.parse("2021-10-26T00:00:00.00Z");
    var aLaDiplomation = Instant.parse("2024-12-26T00:00:00.00Z");
    var trainDeVie = new TrainDeVie(
            "Train de vie",
            500_000,
            aLOuvertureDeHEI,
            aLaDiplomation,
            compteCourant,
            1);

    var patrimoineIloAu13mai24 = new Patrimoine(
            ilo,
            au13mai24,
            Set.of(especes, compteEpargne, compteCourant, ordinateur, effetsVestimentaires, trainDeVie));

    var au26juin24 = Instant.parse("2024-06-26T00:00:00.00Z");
    assertTrue(patrimoineIloAu13mai24.projectionFuture(au26juin24).getValeurComptable() < 3_700_000);
  }

  @Test
  void patrimoine_ilo_le_14_juillet_2024() {
    var ilo = new Personne("Ilo");

    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var especes = new Argent("Espèces", au13mai24, 400_000);
    var compteEpargne = new Argent("Compte epargne", au13mai24, 200_000);
    var compteCourant = new Argent("Compte courant", au13mai24, 600_000);

    var ordinateur = new Materiel("Ordinateur", au13mai24, 2_000_000, -0.10);
    var effetsVestimentaires = new Materiel(
            "Effets vestimentaires",
            au13mai24,
            1_000_000,
            -0.20);

    var aLOuvertureDeHEI = Instant.parse("2021-10-26T00:00:00.00Z");
    var aLaDiplomation = Instant.parse("2024-12-26T00:00:00.00Z");
    var trainDeVie = new TrainDeVie(
            "Train de vie",
            500_000,
            aLOuvertureDeHEI,
            aLaDiplomation,
            compteCourant,
            1);

    var patrimoineIloAu13mai24 = new Patrimoine(
            ilo,
            au13mai24,
            Set.of(especes, compteEpargne, compteCourant, ordinateur, effetsVestimentaires, trainDeVie));

    var au14juillet24 = Instant.parse("2024-07-14T00:00:00.00Z");
    assertTrue(patrimoineIloAu13mai24.projectionFuture(au14juillet24).getValeurComptable() < 3_700_000);
  }
}