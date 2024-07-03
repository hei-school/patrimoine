package school.hei.patrimoine.Zety;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.EvolutionPatrimoine;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.visualisation.xchart.GrapheurEvolutionPatrimoine;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EtudeTest {
    private final GrapheurEvolutionPatrimoine grapheurEvolutionPatrimoine = new GrapheurEvolutionPatrimoine();

    @Test
    void etude_2023_2024() {
        var zety = new Personne("Zety");
        var au3juillet2024 = LocalDate.of(2024, JULY, 3);
        var au28Aout2024 = LocalDate.of(2024, AUGUST, 28);
        var au17Sept2024 = LocalDate.of(2024, SEPTEMBER, 17);

        var ordinateur = new Materiel(
                "Ordinateur",
                au3juillet2024,
                1_200_000,
                au3juillet2024,
                -.1);
        var vetements = new Materiel(
                "Vetements",
                au3juillet2024,
                1_500_000,
                au3juillet2024,
                -.5);
        var argentEspece = new Argent("espece", au3juillet2024, 800_000);
        var fraisScolarite = new FluxArgent(
                "Frais 2023-2024",
                argentEspece,
                au3juillet2024,
                au28Aout2024,
                -200_000,
                27);
        var compteBancaire = new Argent("Compte bancaire", au3juillet2024, 100_000);
        var fraisDeTenueDeCompte = new FluxArgent(
                "frais de tenue de compte",
                compteBancaire,
                au3juillet2024,
                au17Sept2024,
                -100_000,
                25);

        var patrimoineDeZety = new Patrimoine(
                "Patrimoine de Zety",
                zety,
                au3juillet2024,
                Set.of(ordinateur, vetements, argentEspece, fraisScolarite,
                        compteBancaire, fraisDeTenueDeCompte));

        var patrimoineAuJusquau17Sep = new EvolutionPatrimoine(
                "Test",
                patrimoineDeZety,
                au3juillet2024,
                au17Sept2024
        );
        // var viz = grapheurEvolutionPatrimoine.apply(patrimoineAuJusquau17Sep);

        assertEquals(281_8848, patrimoineDeZety.projectionFuture(au17Sept2024).getValeurComptable());
    }
}
