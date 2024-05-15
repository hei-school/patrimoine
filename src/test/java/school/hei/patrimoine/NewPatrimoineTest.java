import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.Personne;
import school.hei.patrimoine.Patrimoine;
import school.hei.patrimoine.possession.Possession;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatrimoineTest {

    private Patrimoine patrimoine;

    @BeforeEach
    void setUp() {
        Personne possesseur = new Personne("Rakoto Paul");
        Instant instant = Instant.now();
        Set<Possession> possessions = new HashSet<>();
        possessions.add(new Possession( "Asus Rog", Instant.now(), 1000000));
        patrimoine = new Patrimoine(possesseur, instant, possessions);
    }

@Test
    void testGetValeurComptableWithEmptyPossessions() {
        Patrimoine emptyPatrimoine = new Patrimoine(new Personne("Rakoto Jack"), Instant.now(), new HashSet<>());
        assertEquals(0, emptyPatrimoine.getValeurComptable());
    }

    @Test
    void testProjectionFuture() {
        Instant futureInstant = Instant.now().plusSeconds(3600);
        Patrimoine futurePatrimoine = patrimoine.projectionFuture(futureInstant);
        assertEquals(futureInstant, futurePatrimoine.getT());
    }
