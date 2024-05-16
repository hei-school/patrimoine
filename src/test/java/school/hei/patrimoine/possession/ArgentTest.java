package school.hei.patrimoine.possession;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArgentTest {
    @Test
    void projection_future() {
        Instant au16Mai24 = Instant.parse("2024-05-16T00:00:00.00Z");
        Instant au25Dec24 = Instant.parse("2024-12-25T00:00:00.00Z");

        Argent argent = new Argent("Compte courant", au16Mai24, 400_000_000);

        TrainDeVie trainDeVie = new TrainDeVie("Train de vie", 300_000, Instant.now(), au25Dec24, argent, 1);

        argent.addFinancés(trainDeVie);

        Argent argentProjeted = argent.projectionFuture(au25Dec24);

        assertEquals(394450000, argentProjeted.getValeurComptable());
    }

}
