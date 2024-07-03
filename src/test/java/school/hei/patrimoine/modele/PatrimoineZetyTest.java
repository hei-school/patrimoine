package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatrimoineZetyTest {
    @Test
    void patrimoine_de_zety_vide_vaut_0() {
        var zety = new Personne("Zety");

        var patrimoineZetyAu3juillet24 = new Patrimoine(
                "patrimoineIloAu3juillet24",
                zety,
                LocalDate.of(2024, JULY, 3),
                Set.of());

        assertEquals(0, patrimoineZetyAu3juillet24.getValeurComptable());
    }
    @Test
    void patrimoine_de_zety_a_de_l_argent() {
        var zety = new Personne("Zety");

        var au3july24 = LocalDate.of(2024, JULY, 3);
        var patrimoineZetyAu3july24 = new Patrimoine(
                "patrimoineIloAu13mai24",
                zety,
                au3july24 ,
                Set.of(
                        new Argent("Espèces", au3july24 , 800_000),
                        new Argent("Compte bancaire", au3july24 , 100_000)
                )
        );
        assertEquals(900_000, patrimoineZetyAu3july24.getValeurComptable());
    }
    @Test
    void patrimoine_de_zety_possede_un_frais_de_scolarité_financé_par_argent() {
        var zety = new Personne("Zety");
        var au3july24 = LocalDate.of(2024, MAY, 13);
        var financeur = new Argent("Espèces", au3july24, 800_000);
        var fraisDescolarité = new FluxArgent(
                "frais de scolarité",
                financeur, au3july24.minusDays(100), au3july24.plusDays(100), -200_000,
                27);

        var patrimoineZetyAu3july24 = new Patrimoine(
                "patrimoineZetyAu3july24",
                zety,
                au3july24,
                Set.of(financeur, fraisDescolarité));

        assertEquals(800_000, patrimoineZetyAu3july24.projectionFuture(au3july24.plusDays(10)).getValeurComptable());
        assertEquals(200_000, patrimoineZetyAu3july24.projectionFuture(au3july24.plusDays(100)).getValeurComptable());
        assertEquals(200_000, patrimoineZetyAu3july24.projectionFuture(au3july24.plusDays(1_000)).getValeurComptable());
    }

    @Test
    void patrimoine_de_zety_possede_un_frais_de_tenue_de_compte_financé_par_argent() {
        var zety = new Personne("Zety");
        var au3july24 = LocalDate.of(2024, MAY, 13);
        var financeur = new Argent("Compte bancaire", au3july24, 100_000);
        var fraisDeTenueDeCompte = new FluxArgent(
                "frais de tenue de compte",
                financeur, au3july24.minusDays(100), au3july24.plusDays(100), -20_000,
                25);

        var patrimoineZetyAu3july24 = new Patrimoine(
                "patrimoineZetyAu3july24",
                zety,
                au3july24,
                Set.of(financeur, fraisDeTenueDeCompte));

        assertEquals(100_000, patrimoineZetyAu3july24.projectionFuture(au3july24.plusDays(10)).getValeurComptable());
        assertEquals(40_000, patrimoineZetyAu3july24.projectionFuture(au3july24.plusDays(100)).getValeurComptable());
        assertEquals(40_000, patrimoineZetyAu3july24.projectionFuture(au3july24.plusDays(1_000)).getValeurComptable());
    }
}
