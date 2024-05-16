package school.hei.patrimoine.possession;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArgentTest {
    @Test
    void valeur_comptable_argent() {
        Instant au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
        Instant aLOuvertureDeHEI = Instant.parse("2021-10-26T00:00:00.00Z");
        Instant aLaDiplomation = Instant.parse("2024-12-26T00:00:00.00Z");
        Instant au14juillet2024 = Instant.parse("2024-07-14T00:00:00.00Z");
        Argent compteCourant = new Argent("Compte courant", au13mai24, 600_000);
        TrainDeVie trainDeVieDebut = new TrainDeVie(
                "train de vie de Ilo",
                500_000,
                aLOuvertureDeHEI,
                aLaDiplomation,
                compteCourant,
                1
        );

        compteCourant.addFinancés(trainDeVieDebut);
        Argent compteCourantProjection = compteCourant.projectionFuture(au14juillet2024);
        assertEquals(au14juillet2024, compteCourantProjection.getT());
    }
}
