package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ZetyPatrimoineTest {

    @Test
    public void testValeurPatrimoineLe17Septembre2024() {
        // Date actuelle et future
        LocalDate dateActuelle = LocalDate.of(2024, 7, 3);
        LocalDate dateFuture = LocalDate.of(2024, 9, 17);

        // Instanciation des possessions
        Materiel ordinateur = new Materiel("Ordinateur", dateActuelle, 1200000, dateActuelle, -10.0 / 100);
        Materiel vetements = new Materiel("Vêtements", dateActuelle, 1500000, dateActuelle, -50.0 / 100);
        Argent especes = new Argent("Argent en espèces", dateActuelle, 800000);
        Argent compteBancaire = new Argent("Compte bancaire", dateActuelle, 100000);
        FluxArgent fraisTenueCompte = new FluxArgent(
                "Frais de tenue de compte",
                compteBancaire,
                dateActuelle,
                LocalDate.of(2999, 12, 31),
                -20000,
                25);
        FluxArgent fraisScolarite = new FluxArgent(
                "Frais de scolarité",
                especes,
                LocalDate.of(2023, 11, 1),
                LocalDate.of(2024, 8, 31),
                -200000,
                27);

        // Instanciation du patrimoine
        Set<Possession> possessions = Set.of(ordinateur, vetements, especes, compteBancaire, fraisTenueCompte, fraisScolarite);
        Patrimoine patrimoine = new Patrimoine("Patrimoine de Zety", new Personne("Zety"), dateActuelle, possessions);

        // Projection future
        Patrimoine patrimoineFuture = patrimoine.projectionFuture(dateFuture);

        // Calcul de la valeur totale du patrimoine au 17 septembre 2024
        int valeurPatrimoineFuture = patrimoineFuture.getValeurComptable();

        // Valeur attendue
        int valeurAttendue = 2978848;

        // Vérification
        assertEquals(valeurAttendue, valeurPatrimoineFuture);
    }
}

