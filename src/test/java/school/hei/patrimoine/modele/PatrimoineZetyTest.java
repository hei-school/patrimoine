package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.GroupePossession;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatrimoineZetyTest {
    @Test
    void patrimoine_de_zety_le_17_septembre_2024() {
        var zety = new Personne("Zety");
        var au3juillet2024 = LocalDate.of(2024, JULY, 3);
        var ordinateur = new Materiel("Ordinateur", au3juillet2024, 1200000, au3juillet2024.minusDays(10), -0.1);
        var vetements = new Materiel("Vetements", au3juillet2024, 1500000, au3juillet2024.minusDays(1000), -0.5);
        var argentEspece = new Argent("Especes", au3juillet2024, 800000);
        var fraisScolarite = new FluxArgent("Frais de scolarite", argentEspece, LocalDate.of(2023, NOVEMBER, 1), LocalDate.of(2024, AUGUST, 31), -200000, 27);
        var argentCompte = new Argent("Espèces", LocalDate.of(2024, JULY, 3), 100000);
        LocalDate dateFinIndefinie = LocalDate.of(9999, DECEMBER, 31);
        var fraisDeTenueDeCompte = new FluxArgent("Frais de tenue de compte", argentEspece,au3juillet2024.minusDays(2), dateFinIndefinie, -20000, 25);
        var patrimoineZety=  new Patrimoine(
                "Patrimoine de Zety",
                zety,
                au3juillet2024,
                Set.of(ordinateur, vetements, argentCompte, argentEspece, fraisScolarite, fraisDeTenueDeCompte)
        );
        var dateDeProjection  = LocalDate.of(2024,SEPTEMBER,17);
        assertEquals(2978848,patrimoineZety.projectionFuture(dateDeProjection).getValeurComptable());

    }
    @Test
    public void Zety_s_endette() {
        var Zety = new Personne("Zety");
        var au3Juillet24 = LocalDate.of(2024, JULY, 3);
        var au17Sept24 = LocalDate.of(2024, SEPTEMBER, 17);
        var au18Sept24 = LocalDate.of(2024, SEPTEMBER, 18);
        var vetements = new Materiel(
                "vetements",
                au3Juillet24,
                1_500_000,
                au3Juillet24,
                -0.50
        );
        var ordinateur = new Materiel(
                "ordinateur",
                au3Juillet24,
                1_200_000,
                au3Juillet24,
                -0.10
        );
        var argentEnEspece = new Argent("argent en espece", au3Juillet24, 800_000);
        var argentEcolage = new Argent("ecolage", au3Juillet24, 200_000);
        var argentDansLeCompte = new Argent("Argent dans le compte de Zety", au3Juillet24, 100_000);
        var fraisDeScolarite = new FluxArgent("Frais de scolarite", argentEcolage, LocalDate.of(2023, NOVEMBER, 1), LocalDate.of(2024, AUGUST, 31), -200_000, 27);
        var compteBancaire = new FluxArgent("Compte bancaire", argentDansLeCompte, au3Juillet24, au17Sept24, -20_000, 25);
        var emprunt = new Argent("Emprunt", au18Sept24, 10_000_000);
        var dette = new Argent("Dette", au18Sept24, -11_000_000);
        var patrimoine = new Patrimoine(
                "patrimoine de zety 17 septembre 2024",
                Zety,
                au3Juillet24,
                Set.of(new GroupePossession("possession de Zety", au3Juillet24,
                        Set.of(ordinateur, vetements, argentEnEspece, fraisDeScolarite, compteBancaire, emprunt, dette))
                )
        );
        //calcul de la valeur du patrimoine a date de 17 septembre
        var patrimoineLe17Septembre = patrimoine.projectionFuture(au17Sept24).getValeurComptable();
        //calcul de la valeur du patrimoine a date de 17 septembre
        var patrimoineLe18Septembre = patrimoine.projectionFuture(au18Sept24).getValeurComptable();
        var diminutionPatimoine = patrimoineLe17Septembre - patrimoineLe18Septembre;
        assertEquals(1002384, diminutionPatimoine);
    }
    @Test
    void epuisement_de_espece() {
        var zety = new Personne("Zety");
        var debutOctobre = LocalDate.of(2024, OCTOBER, 1);
        var fin = LocalDate.of(2025, FEBRUARY, 13);
        var debut2024 = LocalDate.of(2024, JANUARY, 1);
        var argentMensuel = new Argent("Argent mensuel", debut2024, 100_000);
        var trainDeVie = new FluxArgent("Train de vie mensuel", argentMensuel, debutOctobre,fin, -250_000, 1);
        var patrimoineZety = new Patrimoine(
                "Patrimoine de Zety",
                zety,
                debut2024,
                Set.of(argentMensuel, trainDeVie)
        );
        LocalDate dateEpuisement = debutOctobre;
        int montantEspeces = patrimoineZety.getValeurComptable();
        for (;montantEspeces > 0; dateEpuisement = dateEpuisement.plusMonths(1)) {
            montantEspeces += 100000;
            // Vérifie si la date d'épuisement est après la date de début spécifiée
            if (dateEpuisement.isAfter(debutOctobre)) {
                montantEspeces -= 250000;
            }
        }
        var debutJanvier2025 = LocalDate.of(2025, JANUARY, 1) ;
        assertEquals(debutJanvier2025, dateEpuisement);
    }
}


