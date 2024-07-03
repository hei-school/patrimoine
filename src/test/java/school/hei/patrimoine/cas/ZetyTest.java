package school.hei.patrimoine.cas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ZetyTest {

    Personne zety;
    Patrimoine patrimoineZety;
    LocalDate au17Septembre24;
    Argent compteBanquaire;
    LocalDate au3Juillet24;
    Materiel ordinateur;
    Materiel vêtements;
    Argent espèces;
    LocalDate au27Novembre23;
    LocalDate au27Août24;
    FluxArgent fraisDeScolarité;
    FluxArgent fraisDeTenue;

    @BeforeEach
    void patrimoineZety() {
        zety = new Personne("Zety");
        au3Juillet24 = LocalDate.of(2024, Month.JULY, 3);

        ordinateur = new Materiel(
                "Ordinateur",
                au3Juillet24,
                1_200_000,
                au3Juillet24,
                -0.1);

        vêtements = new Materiel(
                "Vêtements",
                au3Juillet24,
                1_500_000,
                au3Juillet24,
                -0.5);

        espèces = new Argent("Espèces", au3Juillet24, 800_000);

        au27Novembre23 = LocalDate.of(2023, Month.NOVEMBER, 27);
        au27Août24 = LocalDate.of(2024, Month.AUGUST, 27);
        au17Septembre24 = LocalDate.of(2024, Month.SEPTEMBER, 17);
        fraisDeScolarité = new FluxArgent(
                "Frais de scolarité",
                espèces,
                au27Novembre23,
                au27Août24,
                -200_000,
                27);

        compteBanquaire = new Argent("Compte Banquaire", au3Juillet24, 100_000);
        fraisDeTenue = new FluxArgent(
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
        assertEquals(patrimoineZety.projectionFuture(au17Septembre24).getValeurComptable(), 2_978_848, "La valeur du patrimoine de Zety le 17 Septembre 2024 est de " + 2_978_848);
    }

    @Test
    void zetySEndette() {
        var au18Septembre24 = LocalDate.of(2024, Month.SEPTEMBER, 18);
        var dette = new Dette(
                "Dette",
                au18Septembre24,
                -11_000_000
        );

        var fluxDette = new FluxArgent(
                "Flux dette",
                compteBanquaire,
                au18Septembre24,
                au18Septembre24.plusDays(1),
                10_000_000,
                18);

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
                        fraisDeTenue,
                        fluxDette,
                        dette
                ));

        assertTrue(patrimoineZety.projectionFuture(au17Septembre24).getValeurComptable() > patrimoineZety.projectionFuture(au18Septembre24).getValeurComptable(), "Zety a perdut du patrmoine entre le 17 et 18 Septembre 2024");
        assertEquals(patrimoineZety.projectionFuture(au17Septembre24).getValeurComptable() - patrimoineZety.projectionFuture(au18Septembre24).getValeurComptable(), 1_002_384, "Zety a perdu " + 1_002_384 + " de patrimoine entre le 17 et 18 Septembre 2024");
    }

}