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

    var ApresUnMoisDOuvertureDeHEI = Instant.parse("2021-11-26T00:00:00.00Z");
    var projectApresUnMois = vieEstudiantine.projectionFuture(ApresUnMoisDOuvertureDeHEI);
    assertEquals(
    100_000,
    ((TrainDeVie) projectApresUnMois).getFinancePar().valeurComptableFuture(ApresUnMoisDOuvertureDeHEI));
  }

  @Test
  void un_train_de_vie_financé_par_argent() {
    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var financeur = new Argent("Espèces", au13mai24, 400_000);

    var debutTrainDeVie = Instant.parse("2024-01-01T00:00:00.00Z");
    var finTrainDeVie = Instant.parse("2024-09-31T00:00:00.00Z");
    var dateDePonction = 1;
    var depensesMensuelles = 3000;

    var trainDeVie =
            new TrainDeVie(
                    "train_de_vie",
                    depensesMensuelles,
                    debutTrainDeVie,
                    finTrainDeVie,
                    financeur,
                    dateDePonction);

    assertEquals(debutTrainDeVie, trainDeVie.getDebut());
    assertEquals(finTrainDeVie, trainDeVie.getFin());
    assertEquals(depensesMensuelles, trainDeVie.getDepensesMensuelle());
    assertEquals(financeur, trainDeVie.getFinancePar());
    assertEquals(dateDePonction, trainDeVie.getDateDePonction());
  }
}