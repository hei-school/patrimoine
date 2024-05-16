package school.hei.patrimoine.possession;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.assertEquals;
class ArgentTest {
    @Test
    void testProjectionFutureSansDepenses() {
        Instant t0 = Instant.parse("2024-05-13T00:00:00.00Z");
        Argent compteCourant = new Argent("Compte courant", t0, 600_000);
        Instant tFutur = Instant.parse("2025-05-13T00:00:00.00Z");
        Argent projection = compteCourant.projectionFuture(tFutur);
        assertEquals(600_000, projection.getValeurComptable());
    }
    @Test
    void testProjectionFutureAvecDepenses() {
        Instant t0 = Instant.parse("2024-05-13T00:00:00.00Z");
        Argent compteCourant = new Argent("Compte courant", t0, 600_000);
        Instant debut = Instant.parse("2023-05-13T00:00:00.00Z");
        Instant fin = Instant.parse("2025-05-13T00:00:00.00Z");
        TrainDeVie vieEtudiante = new TrainDeVie("Vie étudiante", 10_000, debut, fin, compteCourant, 1);
        compteCourant.addFinancés(vieEtudiante);
        Instant tFutur = Instant.parse("2025-05-13T00:00:00.00Z");
        Argent projection = compteCourant.projectionFuture(tFutur);
        int expectedValeurComptable = 600_000 - 240_000;
        assertEquals(expectedValeurComptable, projection.getValeurComptable());
    }
}
