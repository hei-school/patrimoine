package school.hei.patrimoine.modele.Zety;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.cas.PatrimoineZetyCas;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Espece {
    @Test
    void testPatrimoineZetyAvecEtudes2024_2025() {
        Patrimoine patrimoineZety = PatrimoineZetyCas.patrimoineZetyAvecEtudes2024_2025();

        // Détermine la date à partir de laquelle Zety n'a plus d'espèces
        LocalDate dateVerifiee = LocalDate.of(2024, 9, 21);
        int especesRestantes = patrimoineZety.possessions().stream()
                .filter(p -> p instanceof Argent && ((Argent) p).getNom().equals("Espèces"))
                .mapToInt(p -> ((Argent) p).getValeurComptable())
                .sum();

        while (especesRestantes >= 0) {
            // Appliquer les flux financiers
            for (Possession possession : patrimoineZety.possessions()) {
                if (possession instanceof FluxArgent) {
                    FluxArgent flux = (FluxArgent) possession;
                    if (!flux.getDebut().isAfter(dateVerifiee) && !flux.getFin().isBefore(dateVerifiee) && flux.getDateOperation() == dateVerifiee.getDayOfMonth()) {
                        especesRestantes += flux.getArgent().getValeurComptable();
                    }
                }
            }
            if (especesRestantes < 0) {
                break;
            }
            dateVerifiee = dateVerifiee.plusDays(1);
        }

        // Date attendue à partir de laquelle Zety n'a plus d'espèces
        LocalDate dateAttendue = LocalDate.of(2025, 1, 1);

        assertEquals(dateAttendue, dateVerifiee.minusDays(1));
    }
}
