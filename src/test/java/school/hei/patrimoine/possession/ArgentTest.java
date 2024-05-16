package school.hei.patrimoine.possession;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.Patrimoine;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ArgentTest {

    @Test
    void diminution_du_compte_courrant_de_ilo_au_14_juillet_2024(){
        var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
        var compteCourant = new Argent("Compte courant", au13mai24, 600_000);

        var futurProche = Instant.parse("2025-01-01T00:00:00.00Z");
        var trainDevie = new TrainDeVie("train de vie", 500_000,
                au13mai24, futurProche, compteCourant, 1);

        var au14juil24 = Instant.parse("2024-07-14T00:00:00.00Z");
        var projectionDuCompte = compteCourant.projectionFuture(au14juil24);
        assertTrue(
                compteCourant.getValeurComptable() >= projectionDuCompte.getValeurComptable(),
                String.format("%s est plus petit que %s", compteCourant.getValeurComptable(), projectionDuCompte.getValeurComptable()));
    }
}