package school.hei.patrimoine;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.possession.Argent;
import school.hei.patrimoine.possession.Materiel;
import school.hei.patrimoine.possession.Possession;
import school.hei.patrimoine.possession.TrainDeVie;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    assertEquals("Espèces", trainDeVie.getFinancePar().getNom());
    assertEquals(400_000, trainDeVie.getFinancePar().getValeurComptable());
  }

  @Test
  void patrimoine_le_26_juin_2024() {
    var ilo = new Personne("Ilo");
    var au26juin24= Instant.parse("2024-06-26T00:00:00.00Z");
    var possessions = Set.of(
            new Argent("Espèces",au26juin24, 400_000),
            new Argent("Compte epargne",au26juin24, 200_000),
            new Argent("Compte courant",au26juin24, 600_000),
            new Materiel("Ordinateur",au26juin24, 2_000_000, -0.10),
            new Materiel("Vêtements",au26juin24, 1_000_000, -0.20),
            new TrainDeVie("Train de vie", 500_000, Instant.parse("2021-10-26T00:00:00.00Z"),au26juin24,new Argent("Compte courant",au26juin24, 600_000), 1)
    );
    assertEquals(4_200_000, new Patrimoine(ilo,au26juin24, possessions).getValeurComptable());
  }

}