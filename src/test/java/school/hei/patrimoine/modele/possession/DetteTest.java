package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static java.time.Month.JULY;
import static java.time.Month.SEPTEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DetteTest {
    @Test
    void Zety_s_endette() {
        var au18Sept24 = LocalDate.of(2024, SEPTEMBER, 18);
        var au03Juil24 = LocalDate.of(2024, JULY, 3);

        var detteAupresDeBanque = new Dette("Dette auprès de banque", au18Sept24, -11_000_000);
        var compteBancaire = new Argent("Compte bancaire",  au03Juil24, 100_000);

        var detteSurSonCompte  = new FluxArgent(
                "Dette sur son compte",
                compteBancaire,
                au18Sept24,
                au18Sept24,
                detteAupresDeBanque.valeurComptable,
                au18Sept24.getDayOfMonth()
        );

        assertEquals(-10900000, compteBancaire.projectionFuture(au18Sept24).valeurComptable);
    }
}
