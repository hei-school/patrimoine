package school.hei.patrimoine.cas;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.Possession;

import java.time.LocalDate;
import java.time.Month;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ZetyTest {

    @Test
    void zety23_24() {
        Zety23_24 zety23_24 = new Zety23_24();
        LocalDate au17Septembre24 = LocalDate.of(2024, Month.SEPTEMBER, 17);
        assertEquals(zety23_24.get().projectionFuture(au17Septembre24).getValeurComptable(), 2_978_848, "La valeur du patrimoine de Zety le 17 Septembre 2024 est de " + 2_978_848);
    }

    @Test
    void zetySEndette() {
        ZetySEndette zetySEndette = new ZetySEndette();
        Patrimoine patrimoineZety = zetySEndette.get();
        LocalDate au17Septembre24 = LocalDate.of(2024, Month.SEPTEMBER, 17);
        LocalDate au18Septembre24 = au17Septembre24.plusDays(1);

        assertTrue(patrimoineZety.projectionFuture(au17Septembre24).getValeurComptable() > patrimoineZety.projectionFuture(au18Septembre24).getValeurComptable(), "Zety a perdut du patrmoine entre le 17 et 18 Septembre 2024");
        assertEquals(patrimoineZety.projectionFuture(au17Septembre24).getValeurComptable() - patrimoineZety.projectionFuture(au18Septembre24).getValeurComptable(), 1_002_384, "Zety a perdu " + 1_002_384 + " de patrimoine entre le 17 et 18 Septembre 2024");
    }

    @Test
    void zety24_25() {
        Zety24_25 zety24_25 = new Zety24_25();
        Possession espèces = zety24_25.get().possessions().stream()
                .filter(possession -> Objects.equals(possession.getNom(), "Espèces"))
                .findFirst().get();

        var au1Janvier25 = LocalDate.of(2025, Month.JANUARY, 1);

        assertTrue(espèces.projectionFuture(au1Janvier25).getValeurComptable() <= 0);
    }

    @Test
    void zetyPartir() {
        Zety24_25 zety24_25 = new Zety24_25();
        var au14Fevrier25 = LocalDate.of(2025, Month.FEBRUARY, 14);

        assertEquals(zety24_25.get().projectionFuture(au14Fevrier25).getValeurComptable(), -1_528_686);
    }

}