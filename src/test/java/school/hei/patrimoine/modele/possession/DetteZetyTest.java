package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DetteZetyTest {

    @Test
    void dette_zety_entre_17_et_18_septembre_2024() {
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

        LocalDate date3Juillet2024 = LocalDate.of(2024, 7, 3);

        var fluxArgentFraisDeScolarite = new FluxArgent(
                "frais de scolarité",
                argentEnEspece,
                LocalDate.of(2024, Month.NOVEMBER, 1),
                LocalDate.of(2024, Month.AUGUST, 31),
                -200_000,
                27
        );

        var compteBancaire = new Argent("compte bancaire", LocalDate.of(2024, 7, 3), 100_000);

        var fluxArgentCompteBancaire = new FluxArgent(
                "compte bancaire",
                compteBancaire,
                LocalDate.of(2024, 7, 3),
                LocalDate.now().plusYears(10),
                -20_000,
                25
        );

        var dette = new Argent("dette", LocalDate.of(2024, 9, 17).plusDays(1), 10_000_000);

        var detteDeZety = new FluxArgent(
                "dette",
                dette,
                LocalDate.of(2024, 9, 17).plusDays(1),
                LocalDate.of(2024, 9, 17).plusYears(1).plusDays(1),
                -1_000_000,
                18
        );

        var patrimoine_de_zety = new Patrimoine(
                "patrimoine_de_zety",
                zety,
                date3Juillet2024,
                Set.of(ordinateur, vetement, argentEnEspece, compteBancaire, fluxArgentCompteBancaire, fluxArgentFraisDeScolarite, detteDeZety)
        );

        var valeurDuPatrimoineFutur18Septembre2024 = patrimoine_de_zety.projectionFuture(LocalDate.of(2024, 9, 18)).getValeurComptable();
        assertEquals(3_401_451, valeurDuPatrimoineFutur18Septembre2024);
    }
}