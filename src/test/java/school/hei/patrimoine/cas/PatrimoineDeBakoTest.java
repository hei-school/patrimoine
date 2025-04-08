package school.hei.patrimoine.cas;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static school.hei.patrimoine.modele.Argent.ariary;

import java.time.LocalDate;
import java.time.Month;

import org.junit.jupiter.api.Test;

import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;

class BakoCasTest {

    @Test
    void testPatrimoineBakoFin2025() {

        BakoCas bakoCas = new BakoCas(2025, 2025, new Personne("Bako"), 5_000_000);

        LocalDate dateFuture = LocalDate.of(2025, Month.DECEMBER, 31);
        Patrimoine patrimoineFinal = bakoCas.projectionFuture(dateFuture);

        System.out.println("Valeur du patrimoine de Bako le 31 décembre 2025 : " + patrimoineFinal.getValeurComptable());

        assertNotNull(patrimoineFinal.getValeurComptable());


    }
}
