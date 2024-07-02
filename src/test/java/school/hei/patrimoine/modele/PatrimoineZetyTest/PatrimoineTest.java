package school.hei.patrimoine.modele.PatrimoineZetyTest;

import static java.time.Month.SEPTEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.cas.PatrimoineZetyCas;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.Argent;

@Slf4j
class PatrimoineTest {
    private final PatrimoineZetyCas patrimoineDeZetyAu3JuilletSupplier = new PatrimoineZetyCas();

    private Patrimoine patrimoineDeZety3Jul2024() {
        return patrimoineDeZetyAu3JuilletSupplier.get();
    }

    @Test
    void zety_étudie_en_2023_2024() {
        var patrimoineDeZetyAu3Jul = patrimoineDeZety3Jul2024();
        var projeté = patrimoineDeZetyAu3Jul.projectionFuture(LocalDate.of(2024, SEPTEMBER, 17));

        assertEquals(3_600_000, patrimoineDeZetyAu3Jul.getValeurComptable());
        assertEquals(2_978_848, projeté.getValeurComptable());
    }

}