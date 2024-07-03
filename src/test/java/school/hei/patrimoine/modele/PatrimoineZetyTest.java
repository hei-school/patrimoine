package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.JULY;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatrimoineZetyTest {
    @Test
    void patrimoine_de_zety_vide_vaut_0() {
        var zety = new Personne("Zety");

        var patrimoineIloAu3juillet24 = new Patrimoine(
                "patrimoineIloAu3juillet24",
                zety,
                LocalDate.of(2024, JULY, 3),
                Set.of());

        assertEquals(0, patrimoineIloAu3juillet24.getValeurComptable());
    }
}
