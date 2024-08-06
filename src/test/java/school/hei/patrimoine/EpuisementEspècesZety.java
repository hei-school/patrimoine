package school.hei.patrimoine;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpuisementEspècesZety {


    @Test
    public void testEpuisementEspèces() {
        var zety = new Personne("Zety");
        var au1erOctobre24 = LocalDate.of(2024, Month.OCTOBER, 1);
        var au13Fevrier25 = LocalDate.of(2025, Month.FEBRUARY, 13);


        var argentEspeces = new Argent("Argent en espèces de Zety", au1erOctobre24, 800_000);
        var trainDeVieMensuel = new FluxArgent("Train de vie de Zety", argentEspeces, au1erOctobre24, au13Fevrier25, -250_000, 1);
        var donMensuelParents = new FluxArgent("Don mensuel des parents", argentEspeces, LocalDate.of(2024, Month.JANUARY, 15), au13Fevrier25, 100_000, 15);
        var fraisScolarite = new FluxArgent("Frais de scolarité 2024-2025", argentEspeces, LocalDate.of(2024, Month.SEPTEMBER, 21), LocalDate.of(2024, Month.SEPTEMBER, 21), -2_500_000, 21);


        LocalDate dateEpuisement = au1erOctobre24;
        while (argentEspeces.getValeurComptable() > 0 && !dateEpuisement.isAfter(LocalDate.of(2025, Month.JANUARY, 1))) {
            LocalDate prochaineDate = dateEpuisement.plusMonths(1).withDayOfMonth(1);
            argentEspeces = argentEspeces.projectionFuture(prochaineDate);
            dateEpuisement = prochaineDate;
        }
        assertEquals(LocalDate.of(2025, Month.JANUARY, 1), dateEpuisement);
    }



}
