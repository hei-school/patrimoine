package school.hei.patrimoine.possession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.Duration;
import org.junit.jupiter.api.Test;

import java.time.Instant;

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

    assertEquals(compteCourant, vieEstudiantine.getFinancePar());

    assertTrue(compteCourant.getFinances().contains(vieEstudiantine));
  }

  @Test
  void train_de_vie_projection_future() {
    Instant debutActuel = Instant.parse("2024-05-01T00:00:00.00Z");
    Instant finActuelle = Instant.parse("2024-06-01T00:00:00.00Z");
    Argent compteCourant = new Argent("Compte courant", Instant.now(), 1000000);
    TrainDeVie trainDeVie = new TrainDeVie("Voyage", 5000, debutActuel, finActuelle, compteCourant, 1);

    Instant instantFutur = Instant.parse("2024-07-01T00:00:00.00Z");

    TrainDeVie trainDeVieProjection = trainDeVie.projectionFuture(instantFutur);

    Instant nouveauDebutAttendu = debutActuel.plus(Duration.between(debutActuel, instantFutur));
    Instant nouveauFinAttendu = finActuelle.plus(Duration.between(finActuelle, instantFutur));

    assertEquals(nouveauDebutAttendu, trainDeVieProjection.getDebut());
    assertEquals(nouveauFinAttendu, trainDeVieProjection.getFin());
  }

  @Test
  void un_train_de_vie_financé_par_argent() {
    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var financeur = new Argent("Espèces", au13mai24, 400_000);

    var trainDeVie = new TrainDeVie(null, 0, null, null, financeur, 0);
  }
}