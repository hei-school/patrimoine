package school.hei.patrimoine.possession;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ArgentTest {
    @Test
    void argent_dans_compte_courant_ponctionne_par_train_de_vie() {
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

        var au14juillet24 = Instant.parse("2024-07-14T00:00:00.00Z");
        assertEquals(-400_000, compteCourant.projectionFuture(au14juillet24).valeurComptable);
  }
}
