package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Devise;

import java.time.LocalDate;

import static java.time.Month.MAY;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArgentTest {
    @Test
    void argent_initialisation_correcte() {
        var dateOuverture = LocalDate.of(2024, MAY, 13);
        var dateEvaluation = LocalDate.of(2024, MAY, 14);
        var argent = new Argent("Espèces", dateOuverture, dateEvaluation, 600_000, Devise.NON_NOMMEE);

        assertEquals("Espèces", argent.getNom());
        assertEquals(dateOuverture, argent.getDateOuverture());
        assertEquals(600_000, argent.getValeurComptable());
        assertEquals(Devise.NON_NOMMEE, argent.getDevise());
    }

    @Test
    void argent_projectionFuture_avant_date_ouverture() {
        var dateOuverture = LocalDate.of(2024, MAY, 13);
        var dateFuture = LocalDate.of(2024, MAY, 12); // Avant l'ouverture
        var argent = new Argent("Espèces", dateOuverture, dateOuverture, 600_000);

        var projection = argent.projectionFuture(dateFuture);
        assertEquals(0, projection.getValeurComptable());
    }

    @Test
    void argent_projectionFuture_apres_date_ouverture() {
        var dateOuverture = LocalDate.of(2024, MAY, 13);
        var dateFuture = LocalDate.of(2024, MAY, 15); // Après l'ouverture
        var argent = new Argent("Espèces", dateOuverture, dateOuverture, 600_000);

        var projection = argent.projectionFuture(dateFuture);
        assertEquals(600_000, projection.getValeurComptable());
    }

    @Test
    void argent_projectionFuture_avec_flux() {
        var dateOuverture = LocalDate.of(2024, MAY, 13);
        var dateFuture = LocalDate.of(2024, MAY, 15);
        var argent = new Argent("Espèces", dateOuverture, dateOuverture, 600_000);
        var flux = new FluxArgent("Flux", argent, dateOuverture.minusDays(1), dateFuture, -100_000, 15);


        var projection = argent.projectionFuture(dateFuture);
        assertEquals(500_000, projection.getValeurComptable());
    }
}
