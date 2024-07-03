package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;

import java.time.LocalDate;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpuisementTest {
    @Test
    void testDateEpuisementEspeces() {
        LocalDate au21septembre24 = LocalDate.of(2024, Calendar.SEPTEMBER, 21);
        LocalDate debut2024 = LocalDate.of(2024, Calendar.JANUARY, 1);
        LocalDate au1octobre24 = LocalDate.of(2024, Calendar.OCTOBER, 1);
        LocalDate finTrainDeVie = LocalDate.of(2025, Calendar.FEBRUARY, 13);

        Argent argentEspeces = new Argent("Espèces", au21septembre24.minusDays(1), 0); // Début avec 0 Ar
        Argent compteBancaire = new Argent("Compte bancaire", au21septembre24, 0); // Compte vide initialement

        FluxArgent fraisScolarite = new FluxArgent(
                "Frais de scolarité 2024-2025", compteBancaire, au21septembre24, au21septembre24, -2_500_000, 0);

        FluxArgent donsParents = new FluxArgent(
                "Dons des parents", argentEspeces, debut2024, LocalDate.of(9999, Calendar.DECEMBER, 31), 100_000, 15);


        FluxArgent trainDeVie = new FluxArgent(
                "Train de vie mensuel", argentEspeces, au1octobre24, finTrainDeVie, -250_000, 1);

        LocalDate currentDate = au1octobre24.minusMonths(1).withDayOfMonth(1);
        double valeurEspeces = argentEspeces.projectionFuture(currentDate).getValeurComptable();
        while (valeurEspeces > 0) {
            currentDate = currentDate.plusMonths(1).withDayOfMonth(1);
            valeurEspeces = argentEspeces.projectionFuture(currentDate).getValeurComptable();
        }

        LocalDate dateEpuisementEspeces = currentDate.minusMonths(1).withDayOfMonth(1);

        assertEquals(LocalDate.of(2025, Calendar.FEBRUARY, 1), dateEpuisementEspeces);
    }
}
