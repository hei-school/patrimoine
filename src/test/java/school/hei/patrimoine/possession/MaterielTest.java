package school.hei.patrimoine.possession;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class MaterielTest {
    @Test
    void mon_mas_s_apprecie_negativement_dans_le_futur(){
        var au26Oct2021 = Instant.parse("2021-10-26T00:00:00.00Z");
        var mac = new Materiel(
                "MacBook Pro",
                au26Oct2021,
                2_000_000,
                -0.10
        );

        var au26Juin2024 = Instant.parse("2024-06-26T00:00:00.00Z");
        assertTrue(
                mon_mas_s_apprecie_negativement_dans_le_futur() > mac.valeurComptableFuture(au26Juin2024);
    );
    }
}