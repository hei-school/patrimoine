package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Devise;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AchatMaterielAuComptantTest {
    @Test
    void testCreationAvecDeviseNonNommee() {
        LocalDate dateAchat = LocalDate.of(2024, 7, 20);
        AchatMaterielAuComptant achat = new AchatMaterielAuComptant(
                "Ordinateur",
                dateAchat,
                1000,
                0.05,
                new Argent("Banque", dateAchat, 10000)
        );

        assertEquals("Ordinateur", achat.getNom());
        assertEquals(dateAchat, achat.getT());
        assertEquals(1000, achat.getValeurComptable());
        assertEquals(Devise.NON_NOMMEE, achat.getDevise());
    }

    @Test
    void testCreationAvecDeviseSpecifique() {
        LocalDate dateAchat = LocalDate.of(2024, 7, 20);
        Devise devise = new Devise("USD", 4000, dateAchat, 0.02);
        AchatMaterielAuComptant achat = new AchatMaterielAuComptant(
                "Ordinateur",
                dateAchat,
                1000,
                0.05,
                new Argent("Banque", dateAchat, 10000),
                devise
        );

        assertEquals("Ordinateur", achat.getNom());
        assertEquals(dateAchat, achat.getT());
        assertEquals(1000, achat.getValeurComptable());
        assertEquals(devise, achat.getDevise());
    }

    @Test
    void testProjectionFutureAvecDateFutur() {
        LocalDate dateAchat = LocalDate.of(2024, 7, 20);
        LocalDate dateFuture = LocalDate.of(2025, 7, 20);
        AchatMaterielAuComptant achat = new AchatMaterielAuComptant(
                "Ordinateur",
                dateAchat,
                1000,
                0.05,
                new Argent("Banque", dateAchat, 10000)
        );

        Possession futurePossession = achat.projectionFuture(dateFuture);
        assertNotNull(futurePossession);
    }

    @Test
    void testProjectionFutureAvecDatePassee() {
        LocalDate dateAchat = LocalDate.of(2024, 7, 20);
        LocalDate datePassee = LocalDate.of(2023, 7, 20);
        AchatMaterielAuComptant achat = new AchatMaterielAuComptant(
                "Ordinateur",
                dateAchat,
                1000,
                0.05,
                new Argent("Banque", dateAchat, 10000)
        );

        Possession futurePossession = achat.projectionFuture(datePassee);
        assertNotNull(futurePossession);
    }

    @Test
    void testFluxArgentCreation() {
        LocalDate dateAchat = LocalDate.of(2024, 7, 20);
        AchatMaterielAuComptant achat = new AchatMaterielAuComptant(
                "Ordinateur",
                dateAchat,
                1000,
                0.05,
                new Argent("Banque", dateAchat, 10000)
        );

        assertEquals("Financement AchatMaterielAuComptant: Ordinateur",
                achat.getAchatCommeGroupe().getPossessions().stream()
                        .filter(possession -> possession instanceof FluxArgent)
                        .map(FluxArgent.class::cast)
                        .findFirst()
                        .get()
                        .getNom());
    }

    @Test
    void testMaterielCreation() {
        LocalDate dateAchat = LocalDate.of(2024, 7, 20);
        AchatMaterielAuComptant achat = new AchatMaterielAuComptant(
                "Ordinateur",
                dateAchat,
                1000,
                0.05,
                new Argent("Banque", dateAchat, 10000)
        );

        assertEquals("Ordinateur",
                achat.getAchatCommeGroupe().getPossessions().stream()
                        .filter(possession -> possession instanceof Materiel)
                        .map(Materiel.class::cast)
                        .findFirst()
                        .get()
                        .getNom());
    }


    @Test
    void testValeurComptableAchat() {
        LocalDate dateAchat = LocalDate.of(2024, 7, 20);
        AchatMaterielAuComptant achat = new AchatMaterielAuComptant(
                "Ordinateur",
                dateAchat,
                1000,
                0.05,
                new Argent("Banque", dateAchat, 10000)
        );

        assertEquals(1000, achat.getValeurComptable());
    }

    @Test
    void testNomAchatMateriel() {
        LocalDate dateAchat = LocalDate.of(2024, 7, 20);
        AchatMaterielAuComptant achat = new AchatMaterielAuComptant(
                "Ordinateur",
                dateAchat,
                1000,
                0.05,
                new Argent("Banque", dateAchat, 10000)
        );

        assertEquals("Ordinateur", achat.getNom());
    }

}
