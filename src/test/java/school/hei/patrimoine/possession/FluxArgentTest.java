package school.hei.patrimoine.possession;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FluxArgentTest {
  @Test
  void train_de_vie_est_finance_par_compte_courant() {
    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var compteCourant = new Argent("Compte courant", au13mai24, 600_000);

    var aLOuvertureDeHEI = Instant.parse("2021-10-26T00:00:00.00Z");
    var aLaDiplomation = Instant.parse("2024-12-26T00:00:00.00Z");
    var vieEstudiantine = new FluxArgent(
        "Ma super(?) vie d'etudiant",
        -500_000,
        aLOuvertureDeHEI,
        aLaDiplomation,
        compteCourant,
        1);
    var donsDePapaEtMamanAuDebut = new FluxArgent(
        "La générosité des parents au début",
        400_000,
        aLOuvertureDeHEI,
        aLOuvertureDeHEI.plus(100, DAYS),
        compteCourant,
        30);
    var donsDePapaEtMamanALaFin = new FluxArgent(
        "La générosité des parents à la fin",
        400_000,
        aLaDiplomation,
        aLaDiplomation.minus(100, DAYS),
        compteCourant,
        30);

    var au26juin24 = Instant.parse("2024-06-26T00:00:00.00Z");
    assertEquals(600_000, compteCourant.projectionFuture(au13mai24.minus(100, DAYS)).valeurComptable);
    assertEquals(600_000, compteCourant.projectionFuture(au13mai24).valeurComptable);
    assertEquals(500_000, compteCourant.projectionFuture(au26juin24).valeurComptable);
    assertEquals(-1_300_000, compteCourant.projectionFuture(aLaDiplomation).valeurComptable);
    assertEquals(-1_300_000, compteCourant.projectionFuture(aLaDiplomation.plus(100, DAYS)).valeurComptable);
  }
}