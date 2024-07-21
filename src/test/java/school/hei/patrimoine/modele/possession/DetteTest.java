package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Devise;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class DetteTest {

    @Test
    void testDetteCreationAvecDevise() {
        LocalDate date = LocalDate.of(2024, 7, 20);
        Devise devise = new Devise("USD", 4000, date, 0.02);
        Dette dette = new Dette("Emprunt", date, -5000, devise);

        assertEquals("Emprunt", dette.getNom());
        assertEquals(date, dette.getT());
        assertEquals(-5000, dette.getValeurComptable());
        assertEquals(devise, dette.getDevise());
    }

    @Test
    void testDetteCreationSansDevise() {
        LocalDate date = LocalDate.of(2024, 7, 20);
        Dette dette = new Dette("Emprunt", date, -5000);

        assertEquals("Emprunt", dette.getNom());
        assertEquals(date, dette.getT());
        assertEquals(-5000, dette.getValeurComptable());
        assertEquals(Devise.NON_NOMMEE, dette.getDevise());
    }

    @Test
    void testDetteCreationValeurPositive() {
        LocalDate date = LocalDate.of(2024, 7, 20);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new Dette("Emprunt", date, 5000);
        });

        assertNotNull(thrown);
    }

    @Test
    void testDetteProjectionFutureAvecDateFutur() {
        LocalDate date = LocalDate.of(2024, 7, 20);
        LocalDate dateFuture = LocalDate.of(2025, 7, 20);
        Dette dette = new Dette("Emprunt", date, -5000);

        Dette futureDette = dette.projectionFuture(dateFuture);
        assertNotNull(futureDette);
        assertEquals("Emprunt", futureDette.getNom());
        assertEquals(dateFuture, futureDette.getT());
    }

    @Test
    void testDetteProjectionFutureAvecDatePassee() {
        LocalDate date = LocalDate.of(2024, 7, 20);
        LocalDate datePassee = LocalDate.of(2023, 7, 20);
        Dette dette = new Dette("Emprunt", date, -5000);

        Dette futureDette = dette.projectionFuture(datePassee);
        assertNotNull(futureDette);
        assertEquals("Emprunt", futureDette.getNom());
        assertEquals(datePassee, futureDette.getT());
    }

    @Test
    void testDetteProjectionFutureAvecDevise() {
        LocalDate date = LocalDate.of(2024, 7, 20);
        LocalDate dateFuture = LocalDate.of(2025, 7, 20);
        Devise devise = new Devise("USD", 4000, date, 0.02);
        Dette dette = new Dette("Emprunt", date, -5000, devise);

        Dette futureDette = dette.projectionFuture(dateFuture);
        assertNotNull(futureDette);
        assertEquals("Emprunt", futureDette.getNom());
        assertEquals(dateFuture, futureDette.getT());
        assertEquals(devise, futureDette.getDevise());
    }

    @Test
    void testDetteGetValeurComptable() {
        LocalDate date = LocalDate.of(2024, 7, 20);
        Dette dette = new Dette("Emprunt", date, -5000);

        assertEquals(-5000, dette.getValeurComptable());
    }

    @Test
    void testDetteGetNom() {
        LocalDate date = LocalDate.of(2024, 7, 20);
        Dette dette = new Dette("Emprunt", date, -5000);

        assertEquals("Emprunt", dette.getNom());
    }
}
