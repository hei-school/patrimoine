package school.hei.patrimoine.modele.PatrimoineZetyTest;

import static school.hei.patrimoine.cas.PatrimoineZetyCas.AU_3_JUILLET_2024;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.cas.PatrimoineZetyCas;

import school.hei.patrimoine.modele.possession.Devise;

@Slf4j
class PatrimoineAlleMagneZetyTest {
    private final PatrimoineZetyCas patrimoineDeZetyAu3JuilletSupplier = new PatrimoineZetyCas();

    @Test

    void zety_part_en_Allemagne() {
        Devise euro = new Devise("euro", 4821, AU_3_JUILLET_2024, -0.1);
        var patrimoineDeZetyLe14Fevrier2025 = patrimoineDeZetyAu3JuilletSupplier ;
        // Not implemented

    }
}
