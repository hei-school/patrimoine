package school.hei.patrimoine.possession;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import org.junit.jupiter.api.Test;

class TrainDeVieTest {
  @Test
  void train_de_vie_est_finance_par_compte_courant() {
    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var compteCourant = new Argent("Compte courant", au13mai24, 600_000);

    var aLOuvertureDeHEI = Instant.parse("2021-10-26T00:00:00.00Z");
    var aLaDiplomation = Instant.parse("2024-12-26T00:00:00.00Z");
    var vieEstudiantine =
        new TrainDeVie(
            "Ma super(?) vie d'etudiant",
            500_000,
            aLOuvertureDeHEI,
            aLaDiplomation,
            compteCourant,
            1);
    // TODO: assert something useful
    assertEquals(compteCourant, vieEstudiantine.getFinancePar());
  }

  @Test
  void un_train_de_vie_financé_par_argent() {
    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var financeur = new Argent("Espèces", au13mai24, 400_000);

    var debutTrainDeVie = Instant.parse("2024-05-01T00:00:00.00Z");
    var finTrainDeVie = Instant.parse("2024-12-31T00:00:00.00Z");
    var depensesMensuelles = 2000;
    var dateDePonction = 1;

    var trainDeVie =
        new TrainDeVie(
            "TrainDeVie",
            depensesMensuelles,
            debutTrainDeVie,
            finTrainDeVie,
            financeur,
            dateDePonction);

    assertEquals(financeur, trainDeVie.getFinancePar());
    assertEquals(depensesMensuelles, trainDeVie.getDepensesMensuelle());
    assertEquals(debutTrainDeVie, trainDeVie.getDebut());
    assertEquals(finTrainDeVie, trainDeVie.getFin());
    assertEquals(dateDePonction, trainDeVie.getDateDePonction());
  }
}
