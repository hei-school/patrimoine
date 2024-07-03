package school.hei.patrimoine.PatrimoineDeZety;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.Set;

import static java.util.Calendar.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestZety {

    @Test

    void zety_etudie_2023_2024(){
        var zety = new Personne("Zety");
        var au3juillet24 = LocalDate.of(2024, JULY, 3);

        var ordinateur = new Materiel(
                "Ordinateur",
                au3juillet24,
                1_200_000,
                au3juillet24,
                -0.10);

        var vetements = new Materiel(
                "Vêtements",
                au3juillet24,
                1_500_000,
                au3juillet24,
                -0.50);

        var espece = new Argent("espèce", au3juillet24, 800_000);

        var debutScolarite = LocalDate.of(2023, NOVEMBER, 1);
        var finScolarite = LocalDate.of(2024, AUGUST, 30);
        var fraisDeScolarite = new FluxArgent(
                "Frais de Scolarité",
                espece,
                debutScolarite,
                finScolarite,
                -200_000,
                27);

        var compteBancaire = new Argent("CompteBancaire", au3juillet24, 100_000);
        var fraisDuCompte = new FluxArgent(
                "Frais de compte",
                compteBancaire,
                au3juillet24,
                LocalDate.MAX,
                -20_000,
                25);

        var patrimoineZetyAu3juillet24 = new Patrimoine(
                "patrimoineZetyAu3juillet24",
                zety,
                au3juillet24,
                Set.of(ordinateur,
                        vetements,
                        espece,
                        fraisDeScolarite,
                        compteBancaire,
                        fraisDuCompte
                )
        );

        var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);
        assertTrue(patrimoineZetyAu3juillet24.getValeurComptable() > patrimoineZetyAu3juillet24.projectionFuture(au17septembre24).getValeurComptable());
        assertEquals(2_978_848, patrimoineZetyAu3juillet24.projectionFuture(au17septembre24).getValeurComptable());
    }

}
