package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FluxArgentZetyTest {

    @Test
    void flux_d_argent_de_zety() {
        var zety = new Personne("zety");
        var espece = new Argent("espece", LocalDate.of(2024, JULY, 3), 100_000);
        espece.addFinancés(new FluxArgent("Initial", espece, LocalDate.of(2024, JULY, 3), LocalDate.of(2024, JULY, 3), 0, 0));

        var fraisScolarite = new FluxArgent("Frais de scolarité", espece, LocalDate.of(2024, SEPTEMBER, 21), LocalDate.of(2024, SEPTEMBER, 21), -2_500_000, 1);

        var donsParentaux = new FluxArgent("Dons parentaux", espece, LocalDate.of(2024, JANUARY, 15), LocalDate.of(2024, DECEMBER, 15).plusMonths(11), 100_000, 15);

        var trainDeVieZety = new FluxArgent("Train de vie", espece, LocalDate.of(2024, OCTOBER, 1), LocalDate.of(2025, FEBRUARY, 13), -250_000, 1);

        var patrimoineDeZety = new Patrimoine(
                "patrimoine de zety",
                zety,
                LocalDate.of(2025, 1, 1).plusDays(1),
                Set.of(espece, fraisScolarite, donsParentaux, trainDeVieZety)
        );

        assertEquals(0, espece.getValeurComptable());
    }
}
