package school.hei.patrimoine.possession;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrainDeVieTest {
  @Test
  void train_de_vie_apres_fin_financement() {
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

    var apresLaDiplomation = Instant.parse("2025-12-26T00:00:00.00Z");
    assertEquals(
            0,
            vieEstudiantine.projectionFuture(apresLaDiplomation).getDepensesMensuelle());
  }

  @Test
  void train_de_vie_apres_1_mois_de_financement_et_datePonction_passer() {
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

    var unMoisApresOuvertureDeHEI = Instant.parse("2021-11-26T00:00:00.00Z");
    var trainDeApresUnMoisDourvertureDeHEI = vieEstudiantine.projectionFuture(unMoisApresOuvertureDeHEI);
    assertEquals(
            100_000,
            trainDeApresUnMoisDourvertureDeHEI.getFinancePar().getValeurComptable());
    assertEquals(
            vieEstudiantine.getDepensesMensuelle(),
            trainDeApresUnMoisDourvertureDeHEI.getDepensesMensuelle());
  }

  @Test
  void train_de_vie_apres_1_mois_de_financement_et_datePonction_pas_passer() {
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
            2);

    var unMoisApresOuvertureDeHEI = Instant.parse("2021-11-01T00:00:00.00Z");
    var trainDeApresUnMoisDourvertureDeHEI = vieEstudiantine.projectionFuture(unMoisApresOuvertureDeHEI);
    assertEquals(
            600_000,
            trainDeApresUnMoisDourvertureDeHEI.getFinancePar().getValeurComptable());
    assertEquals(
            vieEstudiantine.getDepensesMensuelle(),
            trainDeApresUnMoisDourvertureDeHEI.getDepensesMensuelle());
  }
}