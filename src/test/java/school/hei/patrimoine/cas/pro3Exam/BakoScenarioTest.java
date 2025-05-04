package school.hei.patrimoine.cas.pro3Exam;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Personne;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Argent.ariary;

public class BakoScenarioTest {
    @Test
    void testBakoTotalAssetsAtEndOfYear() {
        var bako = new Personne("Bako");
        var startDate = LocalDate.of(2025, 4, 8);
        var endDate = LocalDate.of(2025, 12, 31);
        var bakoScenario = new BakoScenario(startDate, endDate, Map.of(bako, 1.0));

        var totalAssets = bakoScenario.patrimoine().getValeurComptable();
        assertEquals(ariary(7_375_000), totalAssets);
    }
} 