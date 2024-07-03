package school.hei.patrimoine.modele.PatrimoineZetyTest;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.cas.PatrimoineZetyCas;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.Possession;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatrimoineAllemagneZetyTest {

    private static final Map<String, Double> TAUX_DE_CHANGE = new HashMap<>();
    private static final Map<String, Double> TAUX_APPRECIATION = new HashMap<>();

    static {
        TAUX_DE_CHANGE.put("Ar_€", 1 / 4821.0);
        TAUX_APPRECIATION.put("Ar_€", -0.10);
    }

    @Test
    void testValeurPatrimoineZety14Fevrier2025() {
        testValeurPatrimoine(LocalDate.of(2025, Month.FEBRUARY, 14), 0.0);
    }

    @Test
    void testValeurPatrimoineZety26Octobre2025() {
        testValeurPatrimoine(LocalDate.of(2025, 10, 26), 0.0);
    }

    private void testValeurPatrimoine(LocalDate date, double valeurAttendue) {
        Patrimoine patrimoineZety = PatrimoineZetyCas.patrimoineZetyAvecEtudes2024_2025();

        double valeurPatrimoine = patrimoineZety.possessions().stream()
                .mapToDouble(possession -> possession.convertirValeurComptable(date, "€", TAUX_DE_CHANGE, TAUX_APPRECIATION))
                .sum();

        assertEquals(valeurAttendue, valeurPatrimoine, 1e-2);
    }
}