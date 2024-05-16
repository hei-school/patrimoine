package school.hei.patrimoine.possession;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

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
    TrainDeVie futureProjection = (TrainDeVie) vieEstudiantine.projectionFuture(aLaDiplomation);
    assertTrue(
            compteCourant.getValeurComptable() < futureProjection.depensesTotalesFuture);
  }
  @Test
  void un_train_de_vie_financé_par_argent() {
    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var financeur = new Argent("Espèces", au13mai24, 500_000);
    var debut = Instant.parse("2024-03-01T00:00:00.00Z");
    var fin = Instant.parse("2024-12-31T23:59:59.00Z");
    var trainDeVie = new TrainDeVie("vacance", 200_000, debut, fin, financeur, 2);
    var projectionDate = Instant.parse("2024-12-31T00:00:00.00Z");
    assertEquals(480_000, trainDeVie.projectionFuture(projectionDate).getFinancePar().getValeurComptable());
  }
}