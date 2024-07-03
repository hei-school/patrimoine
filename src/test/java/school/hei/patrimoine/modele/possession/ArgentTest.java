package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static java.time.Month.JULY;
import static java.time.Month.SEPTEMBER;

public class ArgentTest {
    @Test
    void argentDeZety(){
        var au3Juil24 = LocalDate.of(2024, JULY, 23);
        var argent = new Argent("Espèces", au3Juil24, 800_000);

        var au17Sept24 = LocalDate.of(2024, SEPTEMBER, 26);

        var projectionFuture = argent.projectionFuture(au17Sept24);

        assertEquals(800_000, projectionFuture.getValeurComptable());
    }
}
