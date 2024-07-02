package school.hei.patrimoine.modele.PatrimoineZetyTest;

import static java.time.Month.SEPTEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.cas.PatrimoineZetyCas;
import school.hei.patrimoine.modele.Patrimoine;

@Slf4j
class EndettementTest {
    private final PatrimoineZetyCas patrimoineDeZetyAu3JuilletSupplier = new PatrimoineZetyCas();

    private Patrimoine patrimoineDeZety3Jul2024() {
        return patrimoineDeZetyAu3JuilletSupplier.get();
    }

    private Patrimoine patrimoineDeZetySendette() {
        return patrimoineDeZetyAu3JuilletSupplier.zetySendette();
    }

    @Test
    void zety_s_endette() {
        var patrimoineDu03Juillet2024 = patrimoineDeZety3Jul2024();
        var patrimoineDu17Septembre = patrimoineDu03Juillet2024.projectionFuture(LocalDate.of(2024, SEPTEMBER, 17));
        var patrimoineDeZetySendette = patrimoineDeZetySendette();

        var differenceEntreLesDeuxPatrimoines = patrimoineDu17Septembre.getValeurComptable() - patrimoineDeZetySendette.getValeurComptable();

        assertEquals(2_978_848, patrimoineDu17Septembre.getValeurComptable());
        assertEquals(1_976_464, patrimoineDeZetySendette.getValeurComptable());
        assertEquals(1_002_384, differenceEntreLesDeuxPatrimoines);
    }
}
