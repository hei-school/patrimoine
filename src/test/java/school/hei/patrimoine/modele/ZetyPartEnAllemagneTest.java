package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ZetyPartEnAllemagneTest {
    @Test
    void le_patrimoine_de_zety_le14fevrier2025(){
        var zety = new Personne("Zety");

        var ordinateur = new Materiel(
                "ordinateur",
                LocalDate.of(2024, 9, 17),
                1_200_000,
                LocalDate.of(2024, 7, 3),
                -0.1
        );

        var vetement = new Materiel(
                "vetement",
                LocalDate.of(2024, 7, 3),
                1_500_000,
                LocalDate.of(2024, 7, 3),
                -0.5
        );

        var argentEnEspece = new Argent(
                "argent_en_espece",
                LocalDate.of(2024, 7, 3),
                800_000
        );

        var compteEnBanque = new Argent(
                "compte_bancaire",
                LocalDate.of(2024, 7, 3),
                100_000
        );

        var date3Juillet2024 = LocalDate.of(2024, 7, 3);

        var date14Fevrier2025 = LocalDate.of(2025, 2, 14);
        var patrimoine_de_zety = new Patrimoine(
                "patrimoine_de_zety",
                zety,
                date3Juillet2024,
                Set.of(ordinateur, vetement, argentEnEspece, compteEnBanque)
        );

        assertEquals(3_086_300, patrimoine_de_zety.projectionFuture(date14Fevrier2025).getValeurComptable());
    }
}
