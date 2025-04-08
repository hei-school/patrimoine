package school.hei.patrimoine.cas;

import java.time.LocalDate;

import static java.time.Month.APRIL;
import static java.time.Month.DECEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.cas.example.PatrimoineBakoFinAnnee2025.*;
import static school.hei.patrimoine.modele.Argent.ariary;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.cas.example.PatrimoineBakoFinAnnee2025;
import school.hei.patrimoine.modele.Patrimoine;

@Slf4j
class PatrimoineDeBakoTest {

    private final PatrimoineBakoFinAnnee2025 patrimoineBakoSupplier = new PatrimoineBakoFinAnnee2025();

    private Patrimoine patrimoineDeBakoAu8Avril2025() {
        return patrimoineBakoSupplier.get();
    }

    @Test
    void testPatrimoineBakoFinAnnee2025() {

        Patrimoine patrimoineProjeté = patrimoineDeBakoAu8Avril2025();
        assertEquals(ariary(14_640_000), patrimoineProjeté.getValeurComptable());

        log.debug("Patrimoine total projeté de Bako au {} : {}", FIN_PROJECTION, patrimoineProjeté.getValeurComptable());
    }
}
