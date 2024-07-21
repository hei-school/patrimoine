package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Devise;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TransfertArgentTest {
    @Test
    void testTransfertArgentCreationAvecDevise() {
        LocalDate date = LocalDate.of(2024, 7, 20);
        Devise devise = new Devise("USD", 4000, date, 0.02);
        Argent depuisArgent = new Argent("Compte Source", date, 10000, devise);
        Argent versArgent = new Argent("Compte Destination", date, 5000, devise);
        TransfertArgent transfert = new TransfertArgent("Transfert Test", depuisArgent, versArgent, date, date, 1000, date.getDayOfMonth(), devise);

        assertEquals("Transfert Test", transfert.getNom());
        assertEquals(date, transfert.getT());
        assertEquals(devise, transfert.getDevise());
    }

    @Test
    void testTransfertArgentCreationSansDevise() {
        LocalDate date = LocalDate.of(2024, 7, 20);
        Argent depuisArgent = new Argent("Compte Source", date, 10000);
        Argent versArgent = new Argent("Compte Destination", date, 5000);
        TransfertArgent transfert = new TransfertArgent("Transfert Test", depuisArgent, versArgent, date, date, 1000, date.getDayOfMonth());

        assertEquals("Transfert Test", transfert.getNom());
        assertEquals(date, transfert.getT());
        assertEquals(Devise.NON_NOMMEE, transfert.getDevise());
    }

    @Test
    void testTransfertArgentProjectionFutureAvecDateFutur() {
        LocalDate date = LocalDate.of(2024, 7, 20);
        LocalDate dateFuture = LocalDate.of(2025, 7, 20);
        Devise devise = new Devise("USD", 4000, date, 0.02);
        Argent depuisArgent = new Argent("Compte Source", date, 10000, devise);
        Argent versArgent = new Argent("Compte Destination", date, 5000, devise);
        TransfertArgent transfert = new TransfertArgent("Transfert Test", depuisArgent, versArgent, date, date, 1000, date.getDayOfMonth(), devise);

        Possession futureTransfert =  transfert.projectionFuture(dateFuture);
        assertNotNull(futureTransfert);
        assertEquals("Transfert Test", futureTransfert.getNom());
        assertEquals(dateFuture, futureTransfert.getT());
    }

    @Test
    void testTransfertArgentProjectionFutureAvecDatePassee() {
        LocalDate date = LocalDate.of(2024, 7, 20);
        LocalDate datePassee =LocalDate.of(2023, 7, 20);
        Devise devise = new Devise("USD", 4000, date, 0.02);
        Argent depuisArgent = new Argent("Compte Source", date, 10000, devise);
        Argent versArgent = new Argent("Compte Destination", date, 5000, devise);
        TransfertArgent transfert = new TransfertArgent("Transfert Test", depuisArgent, versArgent, date, date, 1000, date.getDayOfMonth(), devise);

        Possession pastTransfert = transfert.projectionFuture(datePassee);
        assertNotNull(pastTransfert);
        assertEquals("Transfert Test", pastTransfert.getNom());
        assertEquals(datePassee, pastTransfert.getT());
    }

    @Test
    void testTransfertArgentGetValeurComptable() {
        LocalDate date = LocalDate.of(2024, 7, 20);
        Devise devise = new Devise("USD", 4000, date, 0.02);
        Argent depuisArgent = new Argent("Compte Source", date, 10000, devise);
        Argent versArgent = new Argent("Compte Destination", date, 5000, devise);
        TransfertArgent transfert = new TransfertArgent("Transfert Test", depuisArgent, versArgent, date, date, 1000, date.getDayOfMonth(), devise);

        assertEquals(0, transfert.getValeurComptable());
    }

    @Test
    void testTransfertArgentGetNom() {
        LocalDate date = LocalDate.of(2024, 7, 20);
        Devise devise = new Devise("USD", 4000, date, 0.02);
        Argent depuisArgent = new Argent("Compte Source", date, 10000, devise);
        Argent versArgent = new Argent("Compte Destination", date, 5000, devise);
        TransfertArgent transfert = new TransfertArgent("Transfert Test", depuisArgent, versArgent, date, date, 1000, date.getDayOfMonth(), devise);

        assertEquals("Transfert Test", transfert.getNom());
    }

    @Test
    void testTransfertArgentCreationAvecMontantUnique() {
        LocalDate date = LocalDate.of(2024, 7, 20);
        Devise devise = new Devise("USD", 4000, date, 0.02);
        Argent depuisArgent = new Argent("Compte Source", date, 10000, devise);
        Argent versArgent = new Argent("Compte Destination", date, 5000, devise);
        TransfertArgent transfert = new TransfertArgent("Transfert Unique", depuisArgent, versArgent, date, 5000);

        assertEquals("Transfert Unique", transfert.getNom());
        assertEquals(date, transfert.getT());
        assertEquals(Devise.NON_NOMMEE, transfert.getDevise());
    }

    @Test
    void testTransfertArgentProjectionFutureAvecMontantUnique() {
        LocalDate date = LocalDate.of(2024, 7, 20);
        LocalDate dateFuture = LocalDate.of(2025, 7, 20);
        Devise devise = new Devise("USD", 4000, date, 0.02);
        Argent depuisArgent = new Argent("Compte Source", date, 10000, devise);
        Argent versArgent = new Argent("Compte Destination", date, 5000, devise);
        TransfertArgent transfert = new TransfertArgent("Transfert Unique", depuisArgent, versArgent, date, 5000);

        Possession futureTransfert =  transfert.projectionFuture(dateFuture);
        assertNotNull(futureTransfert);
        assertEquals("Transfert Unique", futureTransfert.getNom());
        assertEquals(dateFuture, futureTransfert.getT());
    }
}
