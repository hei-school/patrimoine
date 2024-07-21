package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FluxImpossiblesTest {
    @Test
    void testFluxImpossiblesCreation() {
        LocalDate date = LocalDate.of(2024, 7, 20);
        Argent argent =  new Argent("Banque", date, 0);
        FluxArgent fluxArgent1 = new FluxArgent("Flux 1", argent, date, date, 1000, date.getDayOfMonth());
        FluxArgent fluxArgent2 = new FluxArgent("Flux 2", argent, date, date, 500, date.getDayOfMonth());
        Set<FluxArgent> fluxSet = Set.of(fluxArgent1, fluxArgent2);

        FluxImpossibles fluxImpossibles = new FluxImpossibles(date, "Compte Test", 1500, fluxSet);

        assertEquals(date, fluxImpossibles.date());
        assertEquals("Compte Test", fluxImpossibles.nomArgent());
        assertEquals(1500, fluxImpossibles.valeurArgent());
        assertTrue(fluxImpossibles.flux().contains(fluxArgent1));
        assertTrue(fluxImpossibles.flux().contains(fluxArgent2));
        assertEquals(2, fluxImpossibles.flux().size());
    }

    @Test
    void testFluxImpossiblesCreationAvecFluxVide() {
        LocalDate date = LocalDate.of(2024, 7, 20);
        Set<FluxArgent> fluxSet = Set.of();

        FluxImpossibles fluxImpossibles = new FluxImpossibles(date, "Compte Test", 1500, fluxSet);

        assertEquals(date, fluxImpossibles.date());
        assertEquals("Compte Test", fluxImpossibles.nomArgent());
        assertEquals(1500, fluxImpossibles.valeurArgent());
        assertTrue(fluxImpossibles.flux().isEmpty());
    }

    @Test
    void testFluxImpossiblesEquals() {
        LocalDate date = LocalDate.of(2024, 7, 20);
        Argent argent =  new Argent("Banque", date, 0);
        FluxArgent fluxArgent1 = new FluxArgent("Flux 1", argent, date, date, 1000, date.getDayOfMonth());
        FluxArgent fluxArgent2 = new FluxArgent("Flux 2", argent, date, date, 500, date.getDayOfMonth());
        Set<FluxArgent> fluxSet1 = Set.of(fluxArgent1, fluxArgent2);
        Set<FluxArgent> fluxSet2 = Set.of(fluxArgent1, fluxArgent2);

        FluxImpossibles fluxImpossibles1 = new FluxImpossibles(date, "Compte Test", 1500, fluxSet1);
        FluxImpossibles fluxImpossibles2 = new FluxImpossibles(date, "Compte Test", 1500, fluxSet2);

        assertEquals(fluxImpossibles1, fluxImpossibles2);
    }

    @Test
    void testFluxImpossiblesToString() {
        LocalDate date = LocalDate.of(2024, 7, 20);
        Argent argent =  new Argent("Banque", date, 0);
        FluxArgent fluxArgent1 = new FluxArgent("Flux 1", argent, date, date, 1000, date.getDayOfMonth());
        FluxArgent fluxArgent2 = new FluxArgent("Flux 2", argent, date, date, 500, date.getDayOfMonth());
        Set<FluxArgent> fluxSet = Set.of(fluxArgent1, fluxArgent2);

        FluxImpossibles fluxImpossibles = new FluxImpossibles(date, "Compte Test", 1500, fluxSet);

        String expectedString = "FluxImpossibles[date=" + date
                + ", nomArgent=Compte Test"
                + ", valeurArgent=1500"
                + ", flux=" + fluxSet + "]";

        assertEquals(expectedString, fluxImpossibles.toString());
    }
}
