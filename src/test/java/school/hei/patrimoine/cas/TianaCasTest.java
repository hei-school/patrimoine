package school.hei.patrimoine.cas;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.cas.example.TianaCas;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Patrimoine;

import java.time.LocalDate;

public class TianaCasTest {

    @Test
    void patrimoineDeTianaFinMars(){
        var cas = new TianaCas();
        LocalDate finAnnee = LocalDate.of(2026, 03, 31);

        Patrimoine patrimoineFinAnnee = cas.get().projectionFuture(finAnnee);

        Argent result = patrimoineFinAnnee.getValeurComptable();
        System.out.println(result);
    }
}
