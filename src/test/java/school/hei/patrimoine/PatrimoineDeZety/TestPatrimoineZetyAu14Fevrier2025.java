package school.hei.patrimoine.PatrimoineDeZety;


import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPatrimoineZetyAu14Fevrier2025 {

    @Test
    void valeurPatrimoineAu14Fevrier2025() {
        var zety = new Personne("Zety");
        var au3juillet2024 = LocalDate.of(2024, Month.JULY, 3);
        var ordinateur = new Materiel(
                "Ordinateur",
                au3juillet2024,
                1_200_000,
                au3juillet2024.minusDays(2),
                -0.10);

        var vetements = new Materiel(
                "Vêtements",
                au3juillet2024,
                1_500_000,
                au3juillet2024.minusDays(2),
                -0.50);

        var argentEspeces = new Argent("Espèces", au3juillet2024, 800_000);
        var fraisScolarite = new FluxArgent(
                "Frais de scolarité",
                argentEspeces,
                LocalDate.of(2023, Month.NOVEMBER, 27),
                LocalDate.of(2024, Month.AUGUST, 27),
                -200_000,
                30);

        var compteBancaire = new Argent("Compte bancaire", au3juillet2024, 100_000);

        var fraisTenueCompte = new FluxArgent(
                "Frais de tenue de compte",
                compteBancaire,
                au3juillet2024.minusMonths(1),
                au3juillet2024.plusYears(1),
                -20_000,
                30);

        Set<Possession> possessions = new HashSet<>();
        possessions.add(ordinateur);
        possessions.add(vetements);
        possessions.add(argentEspeces);
        possessions.add(fraisScolarite);
        possessions.add(compteBancaire);
        possessions.add(fraisTenueCompte);

        var patrimoineZety = new Patrimoine(
                "Patrimoine de Zety",
                zety,
                au3juillet2024,
                possessions);

        // Projection jusqu'au 14 février 2025
        LocalDate au14Fevrier2025 = LocalDate.of(2025, Month.FEBRUARY, 14);
        var patrimoineAu14Fevrier2025 = patrimoineZety.projectionFuture(au14Fevrier2025);

        // Affichage de la valeur comptable totale du patrimoine de Zety au 14 février 2025
        System.out.println("Valeur du patrimoine de Zety au 14 février 2025 : " + patrimoineAu14Fevrier2025.getValeurComptable());

    }

}

