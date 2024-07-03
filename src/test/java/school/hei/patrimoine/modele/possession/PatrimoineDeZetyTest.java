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

public class PatrimoineDeZetyTest {
    @Test
    void patrimoine_de_zety_le_17_septembre_2024() {
        var zety = new Personne("Zety");
        var au3juillet2024 = LocalDate.of(2024, JULY, 3);
        var ordinateur = new Materiel("Ordinateur", au3juillet2024, 1_200_000, au3juillet2024.minusDays(10), -0.1);
        var vetements = new Materiel("Vetements", au3juillet2024, 1_500_000, au3juillet2024.minusDays(1000), -0.5);
        var argent_espece = new Argent("Especes", au3juillet2024, 800_000);
        var frais_scolarite = new FluxArgent("Frais de scolarite", argent_espece, LocalDate.of(2023, NOVEMBER, 1), LocalDate.of(2024, AUGUST, 31), -200_000, 27);
        var argentCompte = new Argent("Espèces", LocalDate.of(2024, JULY, 3), 100_000);
        LocalDate dateFinIndefinie = LocalDate.of(3000, DECEMBER, 31);
        var fraisDeTenueDeCompte = new FluxArgent("Frais de tenue de compte", argent_espece, au3juillet2024.minusDays(2), dateFinIndefinie, -20_000, 25);
        var patrimoineZety = new Patrimoine(
                "Patrimoine de Zety",
                zety,
                au3juillet2024,
                Set.of(ordinateur, vetements, argentCompte, argent_espece, frais_scolarite, fraisDeTenueDeCompte)
        );
        var dateDeProjection = LocalDate.of(2024, SEPTEMBER, 17);
        assertEquals(2_978_848, patrimoineZety.projectionFuture(dateDeProjection).getValeurComptable());
    }

    @Test
    public void dette_de_zety() {
        var Zety = new Personne("Zety");
        var au3Juillet24 = LocalDate.of(2024, JULY, 3);
        var au17Sept24 = LocalDate.of(2024, SEPTEMBER, 17);
        var au18Sept24 = LocalDate.of(2024, SEPTEMBER, 18);

        var ordinateur = new Materiel(
                "ordinateur de zety",
                au3Juillet24,
                1_200_000,
                au3Juillet24,
                -0.10
        );
        var vetements = new Materiel(
                "vetements de zety",
                au3Juillet24,
                1_500_000,
                au3Juillet24,
                -0.50
        );
        var argent_en_espece = new Argent("Argent en espece de Zety", au3Juillet24, 800_000);
        var argent_ecollage_de_zety = new Argent("Ecollage de Zety", au3Juillet24, 200_000);
        var argent_dans_le_compte = new Argent("Argent dans le compte de Zety", au3Juillet24, 100_000);
        var frais_de_scolarite = new FluxArgent("Frais de scolarite de Zety", argent_ecollage_de_zety, LocalDate.of(2023, NOVEMBER, 1), LocalDate.of(2024, AUGUST, 31), -200_000, 27);
        var compte_bancaire = new FluxArgent("Compte bancaire de Zety", argent_dans_le_compte, au3Juillet24, au17Sept24, -20_000, 25);

        var emprunt = new Argent("Emprunt de Zety", au18Sept24, 10_000_000);

        var dette = new Argent("Dette de Zety", au18Sept24, -11_000_000);

        var patrimoine_Zety_le_3_juillet_2024 = new Patrimoine(
                "patrimoine de zety 17 septembre 2024",
                Zety,
                au3Juillet24,
                Set.of(new GroupePossession(
                                "possession de Zety",
                                au3Juillet24,
                                Set.of(ordinateur,
                                        vetements,
                                        argent_en_espece,
                                        frais_de_scolarite,
                                        compte_bancaire,
                                        emprunt,
                                        dette
                                )
                        )
                )
        );

        var patrimoine_le_17_septembre = patrimoine_Zety_le_3_juillet_2024.projectionFuture(au17Sept24).getValeurComptable();

        var patrimoine_le_18_septembre = patrimoine_Zety_le_3_juillet_2024.projectionFuture(au18Sept24).getValeurComptable();

        var diminution_de_patimoine = patrimoine_le_17_septembre - patrimoine_le_18_septembre;

        assertEquals(1002384, diminution_de_patimoine);
    }


    @Test
    void date_epuisement_especes_zety() {
        var zety = new Personne("Zety");
        var debut2024 = LocalDate.of(2024, JANUARY, 1);
        var debutOctobre2024 = LocalDate.of(2024, OCTOBER, 1);
        var fevrier132025 = LocalDate.of(2025, FEBRUARY, 13);

        var argentMensuel = new Argent("Argent mensuel", debut2024, 100_000);
        var trainDeVie = new FluxArgent("Train de vie mensuel", argentMensuel, debutOctobre2024, fevrier132025, -250_000, 1);

        var patrimoineZety = new Patrimoine(
                "Patrimoine de Zety",
                zety,
                debut2024,
                Set.of(argentMensuel, trainDeVie)
        );

        LocalDate dateEpuisement = debutOctobre2024;
        int montantEspeces = patrimoineZety.getValeurComptable();


        while (montantEspeces > 0) {
            montantEspeces += 100_000;
            if (dateEpuisement.isAfter(debutOctobre2024)) {
                montantEspeces -= 250_000;
            }

            dateEpuisement = dateEpuisement.plusMonths(1);
        }

        assertEquals(LocalDate.of(2025, JANUARY, 1), dateEpuisement);
    }
    @Test
    void valeur_patrimoine_zety_le_14_fevrier_2025() {
        var zety = new Personne("Zety");
        var au3juillet2024 = LocalDate.of(2024, JULY, 3);
        var ordinateur = new Materiel("Ordinateur", au3juillet2024, 1_200_000, au3juillet2024.minusDays(10), -0.1);
        var vetements = new Materiel("Vetements", au3juillet2024, 1_500_000, au3juillet2024.minusDays(1000), -0.5);
        var argent_espece = new Argent("Especes", au3juillet2024, 800_000);
        var frais_scolarite = new FluxArgent("Frais de scolarite", argent_espece, LocalDate.of(2023, NOVEMBER, 1), LocalDate.of(2024, AUGUST, 31), -200_000, 27);
        var argentCompte = new Argent("Espèces", LocalDate.of(2024, JULY, 3), 100_000);
        LocalDate dateFinIndefinie = LocalDate.of(3000, DECEMBER, 31);
        var fraisDeTenueDeCompte = new FluxArgent("Frais de tenue de compte", argent_espece, au3juillet2024.minusDays(2), dateFinIndefinie, -20_000, 25);

        var patrimoineZety = new Patrimoine(
                "Patrimoine de Zety",
                zety,
                au3juillet2024,
                Set.of(ordinateur, vetements, argentCompte, argent_espece, frais_scolarite, fraisDeTenueDeCompte)
        );

        var fevrier142025 = LocalDate.of(2025, FEBRUARY, 14);
        var patrimoineLe14Fevrier2025 = patrimoineZety.projectionFuture(fevrier142025);

        assertEquals(2_521_314, patrimoineLe14Fevrier2025.getValeurComptable());
    }

}



