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

    // If date is equals debut or fin :
    var projectionFutureALaDiplomation = vieEstudiantine.projectionFuture(aLaDiplomation);
    assertTrue(
            vieEstudiantine.getValeurComptable() > projectionFutureALaDiplomation.getValeurComptable());

    // If date between debut and fin :
    var au26mai22 = Instant.parse("2022-05-26T00:00:00.00Z");
    var projectionFutureAu26Mai22 = vieEstudiantine.projectionFuture(au26mai22);
    assertTrue(
            vieEstudiantine.getValeurComptable() > projectionFutureAu26Mai22.getValeurComptable());

    // If not apply depense mesuelle in train de vie :
    var au26mai20 = Instant.parse("2020-05-26T00:00:00.00Z");
    var projectionFutureAu26Mai20 = vieEstudiantine.projectionFuture(au26mai20);
    assertEquals(
            vieEstudiantine.getValeurComptable(), projectionFutureAu26Mai20.getValeurComptable());
  }
}