package school.hei.patrimoine.cas;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.Month;

import org.junit.jupiter.api.Test;

import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;

class TianaCasTest {

    @Test
    void testPatrimoineTianaFinMars2026() {
        TianaCas tianaCas = new TianaCas(2025, 2026, new Personne("Tiana"));

        LocalDate dateFuture = LocalDate.of(2026, Month.MARCH, 31);

        // Calculer la projection du patrimoine
        Patrimoine patrimoineFinal = tianaCas.projectionFuture(dateFuture);

        System.out.println("Valeur du patrimoine de Tiana le 31 mars 2026 : " + patrimoineFinal.getValeurComptable());

        assertNotNull(patrimoineFinal.getValeurComptable());
    }
}
