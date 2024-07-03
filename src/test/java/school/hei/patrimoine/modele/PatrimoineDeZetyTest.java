package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.Currency;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatrimoineDeZetyTest {

    //QUESTION 1
    @Test
    void connaitre_le_patrimoine_de_zety() {
        // Date de référence
        var au3Juillet2024 = LocalDate.of(2024, JULY, 3);
        var au17Sept2024 = LocalDate.of(2024, SEPTEMBER, 17);

        // Création des possessions et flux d'argent
        Possession ordinateur = new Materiel("Ordinateur", au3Juillet2024, 1_200_000, au3Juillet2024, -0.1);
        Possession vetements = new Materiel("Vêtements", au3Juillet2024, 1_500_000, au3Juillet2024, -0.5);
        Possession argentEspece = new Argent("Espèces", au3Juillet2024, 800_000);
        FluxArgent fraisScolarite = new FluxArgent("Frais de scolarité 2023-2024",
                new Argent("Frais de Scolarité", LocalDate.of(2023, 11, 27), 0), LocalDate.of(2023, 11, 27), LocalDate.of(2024, AUGUST, 27), -200_000, 27);
        FluxArgent fraisTenueCompte = new FluxArgent("Frais de tenue de compte",
                new Argent("Compte Bancaire", au3Juillet2024, 100_000), au3Juillet2024, au17Sept2024, -20_000, 25);

        // Création du patrimoine de Zety
        Patrimoine zetyPatrimoine = new Patrimoine(
                "Zety Patrimoine", new Personne("Zety"), au3Juillet2024,
                Set.of(ordinateur, vetements, argentEspece, fraisScolarite, fraisTenueCompte));

        // Projection de la valeur comptable du patrimoine le 17 septembre 2024
        LocalDate dateEvaluation = LocalDate.of(2024, 9, 17);
        int valeurPatrimoine = zetyPatrimoine.projectionFuture(dateEvaluation).getValeurComptable();

        assertEquals(2_978_848, valeurPatrimoine);
        /*FIN QUESTION 1*/

        //QUESTION 2
        LocalDate au18Sept24 = LocalDate.of(2024, 9, 18);
        int valeurPatrimoine2 = valeurPatrimoine - 1_000_000; //qui est le cout du pret

        assertEquals(1_978_848, valeurPatrimoine);

        /*FIN QUESTION 2*/

        //QUESTION 3
        FluxArgent donEspece = new FluxArgent("Don de la part des parents de Zety",
                new Argent("Espece", LocalDate.of(2024, JANUARY, 1), 100_000), LocalDate.of(2024, JANUARY, 1), LocalDate.of(2024, AUGUST, 27), 100_000, 15);
        FluxArgent trainDeVie = new FluxArgent("Train de Vie",
                new Argent("Train de Vie", LocalDate.of(2024, OCTOBER, 1), 250_000), LocalDate.of(2024, OCTOBER, 1), LocalDate.of(2025, FEBRUARY, 13), -250_000, 1);

        /*FIN QUESTION 3*/

        //QUESTION 4
        // Projection du patrimoine au 14 février 2025
        LocalDate au03juillet24 = LocalDate.of(2024, 7, 3);
        LocalDate dateEvaluation2 = LocalDate.of(2025, 2, 14);


        Argent argentEspeces = new Argent("Argent en espèces", au03juillet24, 800_000);
        Argent compteBancaire = new Argent("Compte bancaire", au03juillet24, 100_000);
        FluxArgent fraisCompte = new FluxArgent("Frais de compte", compteBancaire, LocalDate.of(2024, 7, 25), LocalDate.MAX, -20_000, 25);


        Set<Possession> possessions = Set.of(ordinateur, vetements, argentEspeces, compteBancaire, fraisScolarite, fraisCompte);
        Personne possesseur = new Personne("Zety");
        Patrimoine patrimoineDeZety = new Patrimoine("Patrimoine de Zety", possesseur, au03juillet24, possessions);


        double valeurComptable = patrimoineDeZety.projectionFuture(dateEvaluation2).getValeurComptable();

        assertEquals(2_521_314, valeurComptable);


        Currency euro = Currency.getInstance("EUR");
        Currency ar = Currency.getInstance("MGA");
        double tauxChange = 4821;
        double tauxAppreciation = -0.10;

    }
}
