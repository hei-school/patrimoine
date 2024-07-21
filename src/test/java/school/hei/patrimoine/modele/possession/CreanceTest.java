package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Devise;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class CreanceTest {
    @Test
    void testCreanceCreationAvecDevise() {
        LocalDate date = LocalDate.of(2024, 7, 20);
        Devise devise = new Devise("USD", 4000, date, 0.02);
        Creance creance = new Creance("Facture", date, 5000, devise);

        assertEquals("Facture", creance.getNom());
        assertEquals(date, creance.getT());
        assertEquals(5000, creance.getValeurComptable());
        assertEquals(devise, creance.getDevise());
    }

    @Test
    void testCreanceCreationSansDevise() {
        LocalDate date = LocalDate.of(2024, 7, 20);
        Creance creance = new Creance("Facture", date, 5000);

        assertEquals("Facture", creance.getNom());
        assertEquals(date, creance.getT());
        assertEquals(5000, creance.getValeurComptable());
        assertEquals(Devise.NON_NOMMEE, creance.getDevise());
    }

    @Test
    void testCreanceCreationValeurNegative() {
        LocalDate date = LocalDate.of(2024, 7, 20);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new Creance("Facture", date, -5000);
        });

        assertNotNull(thrown);
    }

    @Test
    void testCreanceProjectionFutureAvecDateFutur() {
        LocalDate date = LocalDate.of(2024, 7, 20);
        LocalDate dateFuture = LocalDate.of(2025, 7, 20);
        Creance creance = new Creance("Facture", date, 5000);

        Creance futureCreance = creance.projectionFuture(dateFuture);
        assertNotNull(futureCreance);
        assertEquals("Facture", futureCreance.getNom());
        assertEquals(dateFuture, futureCreance.getT());
    }

    @Test
    void testCreanceProjectionFutureAvecDatePassee() {
        LocalDate date = LocalDate.of(2024, 7, 20);
        LocalDate datePassee = LocalDate.of(2023, 7, 20);
        Creance creance = new Creance("Facture", date, 5000);

        Creance futureCreance = creance.projectionFuture(datePassee);
        assertNotNull(futureCreance);
        assertEquals("Facture", futureCreance.getNom());
        assertEquals(datePassee, futureCreance.getT());
    }

    @Test
    void testCreanceProjectionFutureAvecDevise() {
        LocalDate date = LocalDate.of(2024, 7, 20);
        LocalDate dateFuture = LocalDate.of(2025, 7, 20);
        Devise devise = new Devise("USD", 4000, date, 0.02);
        Creance creance = new Creance("Facture", date, 5000, devise);

        Creance futureCreance = creance.projectionFuture(dateFuture);
        assertNotNull(futureCreance);
        assertEquals("Facture", futureCreance.getNom());
        assertEquals(dateFuture, futureCreance.getT());
        assertEquals(devise, futureCreance.getDevise());
    }

    @Test
    void testCreanceGetValeurComptable() {
        LocalDate date = LocalDate.of(2024, 7, 20);
        Creance creance = new Creance("Facture", date, 5000);

        assertEquals(5000, creance.getValeurComptable());
    }

    @Test
    void testCreanceGetNom() {
        LocalDate date = LocalDate.of(2024, 7, 20);
        Creance creance = new Creance("Facture", date, 5000);

        assertEquals("Facture", creance.getNom());
    }
}
