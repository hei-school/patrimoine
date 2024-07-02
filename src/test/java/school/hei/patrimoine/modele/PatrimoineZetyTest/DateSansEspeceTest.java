package school.hei.patrimoine.modele.PatrimoineZetyTest;

import static java.time.Month.JANUARY;
import static java.time.Month.SEPTEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.cas.PatrimoineZetyCas;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.Argent;

@Slf4j
class DateSansEspeceTest {
    private final PatrimoineZetyCas patrimoineDeZetyAu3JuilletSupplier = new PatrimoineZetyCas();

    private Argent argentEnEspècesDeZetyEn20242025() {
        return patrimoineDeZetyAu3JuilletSupplier.argentEnEspecesDeZetyEn20242025();
    }
    @Test
    void zety_étudie_en_2024_2025() {
        var argentEnEspècesDeZetyEn20242025 = argentEnEspècesDeZetyEn20242025();
        LocalDate dayOfFailureFrom18September = LocalDate.of(2024, SEPTEMBER, 18);

        int i = 0;
        do {
            LocalDate tFutur = dayOfFailureFrom18September.plusDays(i);
            var argentEnEspècesProjeté = argentEnEspècesDeZetyEn20242025.projectionFuture(tFutur);
            if (argentEnEspècesProjeté.getValeurComptable() <= 0) {
                dayOfFailureFrom18September = tFutur;
                break;
            }
            i++;
        } while (true);

        assertEquals(LocalDate.of(2025, JANUARY, 1), dayOfFailureFrom18September);
    }
}
