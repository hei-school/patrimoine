package school.hei.patrimoine.modele;

import org.junit.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatrimoineZetyTest {
    @Test
    public void patrimoine_vide_vaut_0(){
        var zety = new Personne("Zety");
        var patrimoineZetyAu3Juillet2024 = new Patrimoine(
                "patrimoine_de_zety",
                zety,
                LocalDate.of(2024, 7, 3),
                Set.of()
        );
        assertEquals(0, patrimoineZetyAu3Juillet2024.getValeurComptable());
    }

    @Test
    public void patrimoine_de_zety_a_la_date_du_17_septembre_2024(){
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

        var date3Juillet2024 = LocalDate.of(2024, 7, 3);

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

        var date17Septembre2024 = LocalDate.of(2024, 9, 17);
        var patrimoine_de_zety = new Patrimoine(
                "patrimoine_de_zety",
                zety,
                date3Juillet2024,
                Set.of(ordinateur, vetement, fluxArgentCompteBancaire, fluxArgentFraisDeScolarite)
        );

        assertEquals(2_543_835, patrimoine_de_zety.projectionFuture(date17Septembre2024).getValeurComptable());
    }
 }
