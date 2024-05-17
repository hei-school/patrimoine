package school.hei.patrimoine.possession;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
  }

  @Test
  void projectionFuture() {
    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    Instant aLOuvertureDeHEI = Instant.parse("2021-10-26T00:00:00.00Z");
    Instant aLaDiplomation = Instant.parse("2024-12-26T00:00:00.00Z");
    Instant apresLaDiplomation = Instant.parse("2025-12-26T00:00:00.00Z");
    Instant unMoisApresOuvertureDeHEI = Instant.parse("2021-11-26T00:00:00.00Z");
    Instant avantLaDateDePonction = Instant.parse("2021-11-01T00:00:00.00Z");
    Argent compteCourant = new Argent("Compte courant", au13mai24, 600_000);
    TrainDeVie vieEstudiantine = new TrainDeVie(
            "Ma super(?) vie d'etudiant",
            500_000,
            aLOuvertureDeHEI,
            aLaDiplomation,
            compteCourant,
            1);
    assertEquals(0, vieEstudiantine.projectionFuture(apresLaDiplomation).getDepensesMensuelle());
    assertEquals(100_000, vieEstudiantine.projectionFuture(unMoisApresOuvertureDeHEI).getFinancePar().getValeurComptable());
    assertEquals(600_000, vieEstudiantine.projectionFuture(avantLaDateDePonction).getFinancePar().getValeurComptable());
  }
}