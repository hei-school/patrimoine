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
    LocalDate au27Novembre23, au15Janvier24, au3Juillet24, au27Août24, au17Septembre24, au18Septembre24, au21Septembre24, au1Janvier25, au13Fevrier2025, au1Octobre2024, au26Octobre25;
    Argent compteBanquaire, espèces;
    Dette dette;
    Materiel ordinateur, vêtements;
    FluxArgent fraisDeScolaritéMensuel,fraisDeTenue, fraisDeScolaritéAnnuel, donParents, trainDeVie, fluxDette;

    @BeforeEach
    void patrimoineZety() {
        zety = new Personne("Zety");

        // Date
        au27Novembre23 = LocalDate.of(2023, Month.NOVEMBER, 27);
        au15Janvier24 = LocalDate.of(2024, Month.JANUARY, 15);
        au3Juillet24 = LocalDate.of(2024, Month.JULY, 3);
        au27Août24 = LocalDate.of(2024, Month.AUGUST, 27);
        au17Septembre24 = LocalDate.of(2024, Month.SEPTEMBER, 17);
        au18Septembre24 = LocalDate.of(2024, Month.SEPTEMBER, 18);
        au21Septembre24 = LocalDate.of(2024, Month.SEPTEMBER, 21);
        au1Janvier25 = LocalDate.of(2025, Month.DECEMBER, 1);
        au13Fevrier2025 = LocalDate.of(2025, Month.FEBRUARY, 13);
        au1Octobre2024 = LocalDate.of(2025, Month.OCTOBER, 10);
        au26Octobre25 = LocalDate.of(2025, Month.OCTOBER, 26);

        // Materiels
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
        fraisDeScolaritéMensuel = new FluxArgent(
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
                au26Octobre25,
                -20_000,
                25);

        dette = new Dette(
                "Dette",
                au18Septembre24,
                -11_000_000
        );

        fluxDette = new FluxArgent(
                "Flux dette",
                compteBanquaire,
                au18Septembre24,
                au18Septembre24.plusDays(1),
                10_000_000,
                18);

        fraisDeScolaritéAnnuel = new FluxArgent(
                "Frais de scolarité 2024-2025",
                compteBanquaire,
                au21Septembre24,
                au21Septembre24.plusDays(1),
                -2_500_000,
                21);

        trainDeVie = new FluxArgent(
                "Train de vie",
                espèces,
                au1Octobre2024,
                au26Octobre25,
                -250_000,
                1
        );

        patrimoineZety = new Patrimoine(
                "Patrimoine de Zety",
                zety,
                au3Juillet24,
                Set.of(
                        ordinateur,
                        vêtements,
                        espèces,
                        fraisDeScolaritéMensuel,
                        compteBanquaire,
                        fraisDeTenue,
                        fluxDette,
                        dette,
                        fraisDeScolaritéAnnuel,
                        trainDeVie
                ));
    }

    @Test
    void valeurPatrimoineZetyLe17Septembre2024() {
        assertEquals(patrimoineZety.projectionFuture(au17Septembre24).getValeurComptable(), 2_978_848, "La valeur du patrimoine de Zety le 17 Septembre 2024 est de " + 2_978_848);
    }

    @Test
    void zetySEndette() {
        assertTrue(patrimoineZety.projectionFuture(au17Septembre24).getValeurComptable() > patrimoineZety.projectionFuture(au18Septembre24).getValeurComptable(), "Zety a perdut du patrmoine entre le 17 et 18 Septembre 2024");
        assertEquals(patrimoineZety.projectionFuture(au17Septembre24).getValeurComptable() - patrimoineZety.projectionFuture(au18Septembre24).getValeurComptable(), 1_002_384, "Zety a perdu " + 1_002_384 + " de patrimoine entre le 17 et 18 Septembre 2024");
    }

    @Test
    void zetyÉtudieEn2024_2025() {
        donParents = new FluxArgent(
                "Don des Parents",
                espèces,
                au15Janvier24,
                au26Octobre25,
                100_000,
                15
        );

        patrimoineZety = new Patrimoine(
                "Patrimoine de Zety",
                zety,
                au3Juillet24,
                Set.of(
                        ordinateur,
                        vêtements,
                        espèces,
                        fraisDeScolaritéMensuel,
                        compteBanquaire,
                        fraisDeTenue,
                        fluxDette,
                        dette,
                        fraisDeScolaritéAnnuel,
                        trainDeVie,
                        donParents
                ));

        assertTrue(espèces.projectionFuture(au1Janvier25).getValeurComptable() <= 0);
    }

}