package school.hei.patrimoine.possession;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.Patrimoine;
import school.hei.patrimoine.Personne;

import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ArgentTest {

  @Test
  void argent_de_ilo_le_14_juillet_2024() {
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
    assertTrue(compteCourant.getValeurComptable() > compteCourant.valeurComptableFuture(au14juillet24));
  }
}
