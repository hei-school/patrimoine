package school.hei.patrimoine;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.CompteBancaire;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPatrimoineDeZetyAu14Fevrier2025 {

    @Test
    public void _Valeur_Patrimoine_Zety_Au_14_Fevrier_2025(){
        var zety = new Personne("Zety");

        var dateDepart = LocalDate.of(2025, Month.FEBRUARY, 14);
        var au1erOctobre24 = LocalDate.of(2024, Month.OCTOBER, 1);
        var au13Fevrier25 = LocalDate.of(2025, Month.FEBRUARY, 13);

        var argentEspeces = new Argent("Argent en espèces de Zety", au1erOctobre24, 800_000);
        var compteBancaire = new Argent("Compte bancaire de Zety", au1erOctobre24, 100_000);

        var trainDeVieMensuel = new FluxArgent("Train de vie de Zety", argentEspeces, au1erOctobre24, au13Fevrier25, -250_000, 1);
        var donMensuelParents = new FluxArgent("Don mensuel des parents", argentEspeces, LocalDate.of(2024, Month.JANUARY, 15), au13Fevrier25, 100_000, 15);

        var fraisScolarite = new FluxArgent("Frais de scolarité 2024-2025", compteBancaire, LocalDate.of(2024, Month.SEPTEMBER, 21), LocalDate.of(2024, Month.SEPTEMBER, 21), -2_500_000, 21);

        Set<Possession> possessions = Set.of(argentEspeces, compteBancaire, trainDeVieMensuel, donMensuelParents, fraisScolarite);
        var patrimoineDeZety = new Patrimoine("Patrimoine de Zety au 1er octobre 2024", zety, au1erOctobre24, possessions);

        var patrimoineFutur = patrimoineDeZety.projectionFuture(dateDepart);
        int valeurPatrimoine = patrimoineFutur.getValeurComptable();

        assertEquals(50_000, valeurPatrimoine);
    }
}
