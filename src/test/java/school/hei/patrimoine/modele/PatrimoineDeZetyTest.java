package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatrimoineDeZetyTest {
    @Test
    void connaitre_le_patrimoine_de_zety(){
        // Date de référence
        var au3Juillet2024 = LocalDate.of(2024,JULY,3);
        var au17Sept2024 = LocalDate.of(2024,SEPTEMBER,17);

        // Création des possessions et flux d'argent
        Possession ordinateur = new Materiel("Ordinateur", au3Juillet2024, 1_200_000, au3Juillet2024,-0.1);
        Possession vetements = new Materiel("Vêtements", au3Juillet2024, 1_500_000, au3Juillet2024,-0.5);
        Possession argentEspece = new Argent("Espèces", au3Juillet2024, 800_000);
        FluxArgent fraisScolarite = new FluxArgent("Frais de scolarité 2023-2024",
                new Argent("Frais de Scolarité", LocalDate.of(2023, 11, 27) , 0), LocalDate.of(2023, 11, 27), LocalDate.of(2024, AUGUST, 27), -200_000, 27);
        FluxArgent fraisTenueCompte = new FluxArgent("Frais de tenue de compte",
                new Argent("Compte Bancaire", au3Juillet2024, 100_000), au3Juillet2024, au17Sept2024, -20_000, 25);

        // Création du patrimoine de Zety
        Patrimoine zetyPatrimoine = new Patrimoine(
                "Zety Patrimoine", new Personne("Zety"), au3Juillet2024,
                Set.of(ordinateur, vetements, argentEspece, fraisScolarite, fraisTenueCompte));

        // Projection de la valeur comptable du patrimoine le 17 septembre 2024
        LocalDate dateEvaluation = LocalDate.of(2024, 9, 17);
        int valeurPatrimoine = zetyPatrimoine.projectionFuture(dateEvaluation).getValeurComptable();
        int expected = (int) (
                ordinateur.getValeurComptable() +
                        vetements.getValeurComptable() +
                        argentEspece.getValeurComptable() +
                        fraisScolarite.getValeurComptable() +
                        fraisTenueCompte.getValeurComptable()
        );
        assertEquals(expected, valeurPatrimoine);

    }

}
