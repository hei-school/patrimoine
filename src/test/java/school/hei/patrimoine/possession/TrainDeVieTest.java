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

    var au11Novembre26 = Instant.parse("2021-11-26T00:00:00.00Z");
    var nouveautraindevie = (TrainDeVie) vieEstudiantine.projectionFuture(au11Novembre26);
    assertEquals(100_000, nouveautraindevie.getFinancePar().getValeurComptable());
    assertEquals(vieEstudiantine.getValeurComptable(), nouveautraindevie.getValeurComptable());
  }
}