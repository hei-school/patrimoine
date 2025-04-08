package school.hei.patrimoine.cas;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.cas.example.BakoCas;
import school.hei.patrimoine.cas.example.PatrimoineZetyAu3Juillet2024;
import school.hei.patrimoine.modele.Patrimoine;

import java.time.LocalDate;

import static java.time.Month.DECEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Argent.ariary;

@Slf4j
public class BakoTest {
    private final BakoCas patrimoineDeBakoAu8AvrilSupplier =
            new BakoCas();

    private Patrimoine patrimoineDeBako8Avril() {
        return patrimoineDeBakoAu8AvrilSupplier.patrimoine();
    }

    @Test
    void patrimoine_bako_le_31_decembre_2025(){
        var patrimoine_de_bako = patrimoineDeBako8Avril();
        var projeté = patrimoine_de_bako.projectionFuture(LocalDate.of(2025, DECEMBER, 31));
        System.out.println(projeté.getValeurComptable());

        assertEquals(ariary(7375000), patrimoineDeBako8Avril().getValeurComptable());
        assertEquals(ariary(12_450_000), projeté.getValeurComptable());
    }
}
