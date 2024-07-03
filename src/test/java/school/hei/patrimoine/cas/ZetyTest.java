package school.hei.patrimoine.cas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ZetyTest {

    Personne zety;
    Patrimoine patrimoineZety;
    LocalDate au17Septembre24;

    @BeforeEach
    void patrimoineZety() {
        zety = new Personne("Zety");
        var au3Juillet24 = LocalDate.of(2024, Month.JULY, 3);

        var ordinateur = new Materiel(
                "Ordinateur",
                au3Juillet24,
                1_200_000,
                au3Juillet24,
                -0.1);

        var vêtements = new Materiel(
                "Vêtements",
                au3Juillet24,
                1_500_000,
                au3Juillet24,
                -0.5);

        var espèces = new Argent("Espèces", au3Juillet24, 800_000);

        var au27Novembre23 = LocalDate.of(2023, Month.NOVEMBER, 27);
        var au27Août24 = LocalDate.of(2024, Month.AUGUST, 27);
        au17Septembre24 = LocalDate.of(2024, Month.SEPTEMBER, 17);
        var fraisDeScolarité = new FluxArgent(
                "Frais de scolarité",
                espèces,
                au27Novembre23,
                au27Août24,
                -200_000,
                27);

        var compteBanquaire = new Argent("Compte Banquaire", au3Juillet24, 100_000);
        var fraisDeTenue = new FluxArgent(
                "Frais de tenue",
                compteBanquaire,
                au3Juillet24,
                au17Septembre24,
                -20_000,
                25);

        patrimoineZety = new Patrimoine(
                "Patrimoine de Zety",
                zety,
                au3Juillet24,
                Set.of(
                        ordinateur,
                        vêtements,
                        espèces,
                        fraisDeScolarité,
                        compteBanquaire,
                        fraisDeTenue
                ));
    }

    @Test
    void valeurPatrimoineZetyLe17Septembre2024() {
        assertEquals(patrimoineZety.projectionFuture(au17Septembre24).getValeurComptable(), 2_978_848);
    }

}