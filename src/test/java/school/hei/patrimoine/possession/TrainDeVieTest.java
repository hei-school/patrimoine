package school.hei.patrimoine.possession;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrainDeVieTest {
  @Test
  void train_de_vie_est_finance_par_compte_courant() {
    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var compteCourant = new Argent("Compte courant", au13mai24, 600_000);

    var aLOuvertureDeHEI = Instant.parse("2021-10-26T00:00:00.00Z");
    var aLaDiplomation = Instant.parse("2024-12-26T00:00:00.00Z");
    var vieEstudiantine = new TrainDeVie(
        "Ma super(?) vie d'etudiant",
        500_000,
        aLOuvertureDeHEI,
        aLaDiplomation,
        compteCourant,
        1);
    //TODO: assert something useful
    var au26juin24 = Instant.parse("2024-06-26T00:00:00.00Z");
    int expectedDepenses = vieEstudiantine.calculerDepenses(au26juin24);


    TrainDeVie vieEstudiantineFutur = vieEstudiantine.projectionFuture(au26juin24);
    assertEquals(expectedDepenses, vieEstudiantineFutur.calculerDepenses(au26juin24));

    assertTrue(compteCourant.getValeurComptable() >= 0);
  }

  @Test
  void un_train_de_vie_finance_par_argent() {
    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var financeur = new Argent("Espèces", au13mai24, 400_000);

    var trainDeVie = new TrainDeVie("Test TrainDeVie", 10_000, au13mai24, au13mai24.plusSeconds(86400 * 30), financeur, 1);

    var au13juin24 = Instant.parse("2024-06-13T00:00:00.00Z");
    int expectedDepenses = trainDeVie.calculerDepenses(au13juin24);

    TrainDeVie trainDeVieFutur = trainDeVie.projectionFuture(au13juin24);
    assertEquals(expectedDepenses, trainDeVieFutur.calculerDepenses(au13juin24));
  }
}