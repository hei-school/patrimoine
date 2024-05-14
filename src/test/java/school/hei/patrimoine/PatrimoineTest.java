package school.hei.patrimoine;

import org.junit.jupiter.api.Test;
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
    }
}