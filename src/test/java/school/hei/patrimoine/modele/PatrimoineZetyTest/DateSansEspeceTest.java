package school.hei.patrimoine.modele.PatrimoineZetyTest;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.EvolutionPatrimoine;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import static java.time.Month.*;
import static java.time.Month.JANUARY;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateSansEspeceTest {
    @Test
    void date_ou_zety_n_a_plus_d_espèces() {
        var zety = new Personne("Zety");
        var au1janvier24 = LocalDate.of(2024, JANUARY, 1);
        var au21septembre24 = LocalDate.of(2024, SEPTEMBER, 21);
        var au15janvier24 = LocalDate.of(2024, JANUARY, 15);
        var au1eroctobre24 = LocalDate.of(2024, OCTOBER, 1);
        var au1erfevrier25 = LocalDate.of(2025, FEBRUARY, 1);
        var au31janvier25 = LocalDate.of(2025, JANUARY, 31);

        var compteBancaire = new Argent("Compte bancaire", au1janvier24, 100_000);

        var fraisScolarite = new FluxArgent("Frais de scolarité", compteBancaire, au21septembre24, au21septembre24, -2_500_000, 21);

        var donParents = new FluxArgent("Don des parents", compteBancaire, au15janvier24, au1erfevrier25, 100_000, 15);

        var trainDeVie = new FluxArgent("Train de vie", compteBancaire, au1eroctobre24, au1erfevrier25, -250_000, 1);

        var fraisTenueCompte = new FluxArgent("Frais de tenue de compte", compteBancaire, au1janvier24, au31janvier25, -20_000, 25);

        var patrimoineZetyAu1janvier24 = new Patrimoine(
                "patrimoineZetyAu1janvier24",
                zety,
                au1janvier24,
                Set.of(compteBancaire, fraisScolarite, donParents, trainDeVie, fraisTenueCompte));

        var evolutionPatrimoine = new EvolutionPatrimoine(
                "Nom",
                patrimoineZetyAu1janvier24,
                au1janvier24,
                au31janvier25);

        var evolutionJournaliere = evolutionPatrimoine.getEvolutionJournaliere();

        var dateSansEspèces = evolutionJournaliere.entrySet().stream()
                .filter(entry -> entry.getValue().getValeurComptable() < 0)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Zety never runs out of cash."));

        assertEquals(LocalDate.of(2025, JANUARY, 31), dateSansEspèces);
    }
}
