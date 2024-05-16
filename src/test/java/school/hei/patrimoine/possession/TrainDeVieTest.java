package school.hei.patrimoine.possession;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

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
    var tFutur = Instant.parse("2024-05-13T00:00:00.00Z");
    var projection = vieEstudiantine.projectionFuture(tFutur);

    int expectedValeurDisponible = 600_000 - (500_000 * (int) ChronoUnit.MONTHS.between(
            LocalDateTime.ofInstant(aLaDiplomation, ZoneId.of("UTC")),
            LocalDateTime.ofInstant(aLaDiplomation, ZoneId.of("UTC"))
    ) );
    assertTrue(expectedValeurDisponible > projection.getValeurComptable());
  }
}