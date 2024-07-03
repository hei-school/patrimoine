package school.hei.patrimoine.modele.Zety;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.SEPTEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatrimoineDeZetyTest {
    @Test
    void patrimoine_vaut_0() {
        var zety = new Personne("Zety");
        var patrimoineZetyAu17sept24 = new Patrimoine("patrimoineDeZetyAu17sept24",
                zety,
                LocalDate.of(2024, SEPTEMBER,17),
                Set.of());
        assertEquals(0, patrimoineZetyAu17sept24.getValeurComptable());
    }
}
