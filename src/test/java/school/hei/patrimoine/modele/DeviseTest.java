package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static java.time.LocalDate.now;
import static java.time.Month.JULY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeviseTest {
    @Test
    void testValeurEnAriaryPourMGA() {
        Devise devise = Devise.MGA;
        assertEquals(1, devise.valeurEnAriary(now()));
    }

    @Test
    void testValeurEnAriaryPourNonNommee() {
        Devise devise = Devise.NON_NOMMEE;
        assertEquals(1, devise.valeurEnAriary(now()));
    }

    @Test
    void testValeurEnAriaryPourEUR() {
        Devise devise = Devise.EUR;
        LocalDate dateDeBase = LocalDate.of(2024, JULY, 3);
        assertTrue(devise.valeurEnAriary(dateDeBase) > 0);
    }

    @Test
    void testValeurEnAriaryPourCAD() {
        Devise devise = Devise.CAD;
        LocalDate dateDeBase = LocalDate.of(2024, JULY, 8);
        assertTrue(devise.valeurEnAriary(dateDeBase) > 0);
    }

    @Test
    void testValeurEnAriaryAvecAppréciationNegative() {
        Devise devise = new Devise("TEST", 1000, LocalDate.of(2024, JULY, 1), -0.05);
        LocalDate futureDate = LocalDate.of(2024, JULY, 21);
        assertTrue(devise.valeurEnAriary(futureDate) > 0);
    }

    @Test
    void testValeurEnAriaryAvecAppréciationPositive() {
        Devise devise = new Devise("TEST", 1000, LocalDate.of(2024, JULY, 1), 0.05);
        LocalDate futureDate = LocalDate.of(2024, JULY, 21);
        assertTrue(devise.valeurEnAriary(futureDate) <= 1000);
    }

    @Test
    void testValeurEnAriaryAvecDateFuture() {
        Devise devise = new Devise("TEST", 1000, LocalDate.of(2024, JULY, 1), 0.05);
        LocalDate futureDate = LocalDate.of(2025, JULY, 1);
        assertTrue(devise.valeurEnAriary(futureDate) < 1000);
    }

    @Test
    void testValeurEnAriaryAvecDatePassée() {
        Devise devise = new Devise("TEST", 1000, LocalDate.of(2024, JULY, 1), 0.05);
        LocalDate pastDate = LocalDate.of(2023, JULY, 1);
        assertTrue(devise.valeurEnAriary(pastDate) > 1000);
    }
}
