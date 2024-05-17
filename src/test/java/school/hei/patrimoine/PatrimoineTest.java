package school.hei.patrimoine;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.possession.Argent;
import school.hei.patrimoine.possession.Materiel;
import school.hei.patrimoine.possession.TrainDeVie;

import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
  void diminution_du_patrimoine_de_ilo_au_26_juin_2024(){
    var ilo = new Personne("Ilo");

    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var compteCourant = new Argent("Compte courant", au13mai24, 600_000);

    var au26oct21 = Instant.parse("2024-10-26T00:00:00.00Z");
    var au01jan24 = Instant.parse("2024-01-01T00:00:00.00Z");
    var futurProche = Instant.parse("2025-01-01T00:00:00.00Z");
    var patrimoineIloAu13mai24 = new Patrimoine(
            ilo,
            au13mai24,
            Set.of(
                    new Argent("Espèces", au13mai24, 400_000),
                    new Argent("Compte epargne", au13mai24, 200_000),
                    compteCourant,
                    new Materiel("ordinateur", au26oct21,
                            2_000_000, -.10),
                    new Materiel("effets vestimentaires", au01jan24,
                            1_000_000, -.20),
                    new TrainDeVie("train de vie", 500_000, au13mai24, futurProche,
                            compteCourant, 1)));

    var au26juin24 = Instant.parse("2024-06-26T00:00:00.00Z");
    Patrimoine patrimoineAu26Juin24 = patrimoineIloAu13mai24.projectionFuture(au26juin24);
    assertTrue(
            patrimoineIloAu13mai24.getValeurComptable() >= patrimoineAu26Juin24.getValeurComptable(),
            String.format("%s est plus petit que %s", patrimoineIloAu13mai24.getValeurComptable(), patrimoineAu26Juin24.getValeurComptable()));
  }

  @Test
  void diminution_du_patrimoine_de_ilo_au_14_juillet_2024(){
    var ilo = new Personne("Ilo");

    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var compteCourant = new Argent("Compte courant", au13mai24, 600_000);

    var au26oct21 = Instant.parse("2024-10-26T00:00:00.00Z");
    var au01jan24 = Instant.parse("2024-01-01T00:00:00.00Z");
    var futurProche = Instant.parse("2025-01-01T00:00:00.00Z");
    var patrimoineIloAu13mai24 = new Patrimoine(
            ilo,
            au13mai24,
            Set.of(
                    new Argent("Espèces", au13mai24, 400_000),
                    new Argent("Compte epargne", au13mai24, 200_000),
                    compteCourant,
                    new Materiel("ordinateur", au26oct21,
                            2_000_000, -.10),
                    new Materiel("effets vestimentaires", au01jan24,
                            1_000_000, -.20),
                    new TrainDeVie("train de vie", 500_000, au13mai24, futurProche,
                            compteCourant, 1)));

    var au14juil24 = Instant.parse("2024-07-14T00:00:00.00Z");
    Patrimoine patrimoineAu14Juillet24 = patrimoineIloAu13mai24.projectionFuture(au14juil24);
    assertTrue(
            patrimoineIloAu13mai24.getValeurComptable() >= patrimoineAu14Juillet24.getValeurComptable(),
            String.format("%s est plus petit que %s", patrimoineIloAu13mai24.getValeurComptable(), patrimoineAu14Juillet24.getValeurComptable()));
  }
}
