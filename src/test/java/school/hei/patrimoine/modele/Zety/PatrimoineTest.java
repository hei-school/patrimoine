package school.hei.patrimoine.modele.Zety;

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

import static java.time.Month.SEPTEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatrimoineTest {

    @Test
    void patrimoine_zety() {
        // Définition des données au 3 juillet 2024
        var zety = new Personne("Zety");
        var au3Juillet24 = LocalDate.of(2024, 7, 3);

        // Création des possessions de Zety au 3 juillet 2024
        Set<Possession> possessions = new HashSet<>();

        // Ordinateur
        Materiel ordinateur = new Materiel("Ordinateur", au3Juillet24, 1_200_000, au3Juillet24, -0.10);
        possessions.add(ordinateur);

        // Vêtements
        Materiel vetements = new Materiel("Vêtements", au3Juillet24, 1_500_000, au3Juillet24, -0.50);
        possessions.add(vetements);

        // Espèces
        possessions.add(new Argent("Espèces", au3Juillet24, 800_000));

        // Compte bancaire
        possessions.add(new Argent("Compte bancaire", au3Juillet24, 100_000));

        // Simulation des frais de scolarité de novembre 2023 à août 2024
        FluxArgent fraisScolarite = new FluxArgent(
                "Frais de scolarité",
                new Argent("Espèces", au3Juillet24, 0),
                LocalDate.of(2023, 11, 27),
                LocalDate.of(2024, 8, 27),
                -200_000, 27);

        // Ajout des frais de scolarité à la liste des possessions
        possessions.add(fraisScolarite);

        // Calcul de la valeur comptable au 17 septembre 2024
        LocalDate au17Sept24 = LocalDate.of(2024, SEPTEMBER, 17);
        int valeurComptableAttendue =3600000;

        // Création du patrimoine de Zety au 17 septembre 2024
        var patrimoineZetyAu17Sept24 = new Patrimoine(
                "patrimoineZetyAu17Sept24",
                zety,
                au17Sept24,
                possessions
        );

        // Vérification
        assertEquals(valeurComptableAttendue, patrimoineZetyAu17Sept24.getValeurComptable());
    }
}
