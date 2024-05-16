package school.hei.patrimoine.possession;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArgentTest {
    @Test
    void compte_courant_finance_un_train_de_vie() {
        var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
        var compteCourant = new Argent("Compte courant", au13mai24, 600_000);

        var aLOuvertureDeHEI = Instant.parse("2021-10-26T00:00:00.00Z");
        var aLaDiplomation = Instant.parse("2024-12-26T00:00:00.00Z");
        var trainDeVie = new TrainDeVie(
                "Ma pénible vie d'etudiant",
                300_000,
                aLOuvertureDeHEI,
                aLaDiplomation,
                compteCourant,
                1);

        var au26dec21 = Instant.parse("2021-12-26T00:00:00.00Z");
        var projectionApresUnMois = compteCourant.projectionFuture(au26dec21);
        int valeurComptableProjetee = projectionApresUnMois.getValeurComptable();

        assertEquals(0, valeurComptableProjetee);
    }
}
