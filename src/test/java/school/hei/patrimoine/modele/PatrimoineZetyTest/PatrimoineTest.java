package school.hei.patrimoine.modele.PatrimoineZetyTest;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatrimoineTest {
    @Test
    void patrimoine_zety_le_17_septembre_2024() {
        var zety = new Personne("Zety");
        var au3juillet24 = LocalDate.of(2024, JULY, 3);
        var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);

        var ordinateur = new Materiel("Ordinateur", au3juillet24, 1_200_000, au3juillet24, -0.10);
        var vetements = new Materiel("Vêtements", au3juillet24, 1_500_000, au3juillet24, -0.50);
        var argentEnEspece = new Argent("Argent en espèces", au3juillet24, 800_000);
        var compteBancaire = new Argent("Compte bancaire", au3juillet24, 100_000);
        var fraisTenueCompte = new FluxArgent("Frais de tenue de compte", compteBancaire, au3juillet24, au3juillet24.plusMonths(12), -20_000, 25);
        var fraisScolarite = new Argent("Frais de scolarité", au3juillet24, 0);
        var fluxFraisScolarite = new FluxArgent("Frais de scolarité", fraisScolarite, LocalDate.of(2023, 11, 27), LocalDate.of(2024, 8, 27), -200_000, 27);

        var patrimoineZetyAu3juillet24 = new Patrimoine(
                "Patrimoine Zety",
                zety,
                au3juillet24,
                Set.of(ordinateur, vetements, argentEnEspece, compteBancaire, fraisScolarite, fluxFraisScolarite, fraisTenueCompte));

        var patrimoineZetyAu17septembre24 = patrimoineZetyAu3juillet24.projectionFuture(au17septembre24);

        assertEquals(2_978_848, patrimoineZetyAu17septembre24.getValeurComptable());
    }
}