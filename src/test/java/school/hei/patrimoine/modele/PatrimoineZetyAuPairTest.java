package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatrimoineZetyAuPairTest {
    @Test
    void testPatrimoineAu14Fevrier2025() {
        // Date de départ de Zety en Allemagne
        LocalDate dateDepart = LocalDate.of(2025, 2, 14);


        var au1octobre24 = LocalDate.of(2024, 10, 1);
        var ordinateur = new Materiel("Ordinateur", au1octobre24, 1_200_000, au1octobre24, -0.10);
        var vetements = new Materiel("Vêtements", au1octobre24, 1_500_000, au1octobre24, -0.50);
        var argentEspeces = new Argent("Espèces", au1octobre24, 800_000);
        var compteBancaire = new Argent("Compte bancaire", au1octobre24, 100_000);

        // Flux de frais de scolarité jusqu'à février 2025
        var fraisScolarite = new FluxArgent(
                "Frais de scolarité", argentEspeces, LocalDate.of(2023, 11, 27),
                LocalDate.of(2024, 8, 27), -200_000, 27);

        // Flux de dons mensuels des parents
        var donsParents = new FluxArgent(
                "Dons mensuels des parents", argentEspeces, LocalDate.of(2024, 1, 15),
                dateDepart.minusDays(1), 100_000, 15);

        // Projection des valeurs des possessions à la date de départ
        double valeurOrdinateur = ordinateur.projectionFuture(dateDepart).getValeurComptable();
        double valeurVetements = vetements.projectionFuture(dateDepart).getValeurComptable();
        double valeurArgentEspeces = argentEspeces.projectionFuture(dateDepart).getValeurComptable();
        double valeurCompteBancaire = compteBancaire.projectionFuture(dateDepart).getValeurComptable();

        double valeurTotalePatrimoine = valeurOrdinateur + valeurVetements + valeurArgentEspeces + valeurCompteBancaire;

        double expectedValue = 1_200_000 * Math.pow(1 - 0.0002738, 137) +
                1_500_000 * Math.pow(1 - 0.001898, 137) +
                800_000 - 2_500_000;

        assertEquals(expectedValue, valeurTotalePatrimoine, 0.01);
    }
}
