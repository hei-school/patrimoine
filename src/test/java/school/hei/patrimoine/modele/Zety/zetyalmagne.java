package school.hei.patrimoine.modele.Zety;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.cas.PatrimoineZetyCas;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.Possession;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class zetyalmagne {
    @Test
    void testValeurPatrimoineZety14Fevrier2025() {
        Patrimoine patrimoineZety = PatrimoineZetyCas.patrimoineZetyAvecEtudes2024_2025();

        LocalDate date = LocalDate.of(2025, 2, 14);
        Map<String, Double> tauxDeChange = new HashMap<>();
        tauxDeChange.put("Ar_€", 1 / 4821.0);
        Map<String, Double> tauxAppreciation = new HashMap<>();
        tauxAppreciation.put("Ar_€", -0.10);

        double valeurPatrimoine = 0.0;

        for (Possession possession : patrimoineZety.possessions()) {
            valeurPatrimoine += possession.convertirValeurComptable(date, "€", tauxDeChange, tauxAppreciation);
        }
        double valeurAttendue = 0;
        assertEquals(valeurAttendue, valeurPatrimoine, 1e-2);
    }

    @Test
    void testValeurPatrimoineZety26Octobre2025() {
        Patrimoine patrimoineZety = PatrimoineZetyCas.patrimoineZetyAvecEtudes2024_2025();

        LocalDate date = LocalDate.of(2025, 10, 26);
        Map<String, Double> tauxDeChange = new HashMap<>();
        tauxDeChange.put("Ar_€", 1 / 4821.0);
        Map<String, Double> tauxAppreciation = new HashMap<>();
        tauxAppreciation.put("Ar_€", -0.10);

        double valeurPatrimoine = 0.0;

        for (Possession possession : patrimoineZety.possessions()) {
            valeurPatrimoine += possession.convertirValeurComptable(date, "€", tauxDeChange, tauxAppreciation);
        }

        // Supposons que la valeur attendue soit calculée manuellement
        double valeurAttendue = 0;
        assertEquals(valeurAttendue, valeurPatrimoine, 1e-2);
    }
}
