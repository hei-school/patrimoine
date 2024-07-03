package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static java.time.Month.*;

public class FluxArgentZetyTest {

    @Test
    void flux_d_argent_de_zety() {
        var compteCourant = new Argent("Compte courant", LocalDate.of(2024, JULY, 3), 0);
        compteCourant.addFinancés(new FluxArgent("Initial", compteCourant, LocalDate.of(2024, JULY, 3), LocalDate.of(2024, JULY, 3), 0, 0));

        var fraisScolarite = new FluxArgent("Frais de scolarité", compteCourant, LocalDate.of(2024, SEPTEMBER, 21), LocalDate.of(2024, SEPTEMBER, 21), -2500_000, 1);

        var donsParentaux = new FluxArgent("Dons parentaux", compteCourant, LocalDate.of(2024, JANUARY, 15), LocalDate.of(2024, DECEMBER, 15).plusMonths(11), 100_000, 12);

        var trainDeVieZety = new FluxArgent("Train de vie", compteCourant, LocalDate.of(2024, OCTOBER, 1), LocalDate.of(2025, FEBRUARY, 13), -250_000, 1);

    }
}
