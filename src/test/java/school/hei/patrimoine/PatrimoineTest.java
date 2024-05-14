package school.hei.patrimoine;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class PatrimoineTest {
    @Test
    void patrimoine_vide_vaut_0(){
        var sullivan = new Personne("Sullivan");
        var patrimoineSullivan = new Patrimoine(
                sullivan,
                Instant.parse("2024-05-13T00:00:00.00Z"));

        Possession ordinateur = new Possession();
        patrimoineSullivan.addPossession(ordinateur);

        assertEquals(0, patrimoineSullivan.getValeurComptableActuelle());
    }
}