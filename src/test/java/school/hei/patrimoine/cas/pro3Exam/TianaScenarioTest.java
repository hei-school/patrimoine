package school.hei.patrimoine.cas.pro3Exam;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Personne;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Argent.ariary;

public class TianaScenarioTest {
    @Test
    void testTianaTotalAssetsAtEndOfYear() {
        var tiana = new Personne("Tiana");
        var startDate = LocalDate.of(2025, 4, 8);
        var endDate = LocalDate.of(2026, 3, 31);
        var tianaScenario = new TianaScenario(startDate, endDate, Map.of(tiana, 1.0));

        var totalAssets = tianaScenario.patrimoine().getValeurComptable();
        assertEquals(ariary(160_000_000), totalAssets);
    }
}