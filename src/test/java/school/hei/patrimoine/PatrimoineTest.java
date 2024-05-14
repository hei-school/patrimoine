package school.hei.patrimoine;

import org.junit.jupiter.api.Test;
import school.hei.possession.Argent;
import school.hei.possession.Possession;

import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PatrimoineTest {
    @Test
    void patrimoine_vide_vaut_0() {
        var sullivan = new Personne("Sullivan");
        var patrimoineSullivanAu13Mai2024 = new Patrimoine(
                sullivan,
                Instant.parse("2024-05-13T00:00:00.00Z"),
                Set.of());

        assertEquals(0, patrimoineSullivanAu13Mai2024.getValeurComptable());
    }

    @Test
    void patrimoine_a_de_l_argent() {
        var sullivan = new Personne("Sullivan");
        var au13mai2024 =Instant.parse("2024-05-13T00:00:00.00Z");
        var patrimoineSullivanAu13Mai2024 = new Patrimoine(
                sullivan,
                au13mai2024,
                Set.of(
                        new Argent("Especes", au13mai2024, 400_000),
                        new Argent("Compte Epargne", au13mai2024, 200_000),
                        new Argent("Compte Courant", au13mai2024, 600_000)
                ));

        assertEquals(1_200_000, patrimoineSullivanAu13Mai2024.getValeurComptable());
    }
}