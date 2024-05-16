package school.hei.patrimoine.possession;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrainDeVieTest {
  @Test
  void train_de_vie_est_finance_par_compte_courant() {
    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var compteCourant = new Argent("Compte courant", au13mai24, 600_000);

    var aLOuvertureDeHEI = Instant.parse("2021-10-26T00:00:00.00Z");
    var aLaDiplomation = Instant.parse("2024-12-26T00:00:00.00Z");
    var vieEstudiantine = new TrainDeVie("Ma super(?) vie d'etudiant",
            500_000,
            aLOuvertureDeHEI,
            aLaDiplomation,
            compteCourant,
            1);

    assertTrue(compteCourant.getValeurComptable() > vieEstudiantine.getValeurComptable());
  }

  @Test
  void un_train_de_vie_financé_par_argent() {
    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var financeur = new Argent("Espèces", au13mai24, 400_000);

    var trainDeVie = new TrainDeVie("Voyage en train", 200_000,
            Instant.parse("2024-06-01T00:00:00.00Z"),
            Instant.parse("2024-06-30T00:00:00.00Z"),
            financeur,
            15);

    assertEquals("Voyage en train", trainDeVie.getNom());
    assertEquals(200_000, trainDeVie.getDepensesMensuelle());
    assertEquals(Instant.parse("2024-06-01T00:00:00.00Z"), trainDeVie.getDebut());
    assertEquals(Instant.parse("2024-06-30T00:00:00.00Z"), trainDeVie.getFin());
    assertEquals(financeur, trainDeVie.getFinancePar());
    assertEquals(15, trainDeVie.getDateDePonction());
  }
}
