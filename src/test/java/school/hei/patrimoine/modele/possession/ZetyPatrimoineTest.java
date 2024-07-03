package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ZetyPatrimoineTest {

    @Test
    void patrimoine_de_zety_le_17_septembre_2024() {

        LocalDate au3juillet24 = LocalDate.of(2024, JULY, 3);

        // Création des possessions de Zety
        Materiel ordinateur = new Materiel(
                "Ordinateur",
                au3juillet24,
                1_200_000,
                au3juillet24.minusDays(2),
                -0.10); // Taux de dépréciation quotidien -0.10%

        Materiel vetements = new Materiel(
                "Vêtements",
                au3juillet24,
                1_500_000,
                au3juillet24.minusDays(2),
                -0.50); // Taux de dépréciation quotidien -0.50%

        Argent especes = new Argent("Espèces", au3juillet24, 800_000); // Argent en espèces

        Argent compteBancaire = new Argent("Compte bancaire", au3juillet24, 100_000); // Compte bancaire

        FluxArgent fraisScolarite = new FluxArgent(
                "Frais de scolarité",
                especes, LocalDate.of(2023, NOVEMBER, 27), LocalDate.of(2024, AUGUST, 27), -200_000,
                30); // Frais de scolarité mensuels

        FluxArgent fraisTenueCompte = new FluxArgent(
                "Frais de tenue de compte",
                compteBancaire, au3juillet24, au3juillet24.plusYears(100), -20_000,
                30); // Frais de tenue de compte mensuels

        // Création du patrimoine de Zety
        Patrimoine patrimoineZety = new Patrimoine(
                "patrimoineZety",
                new Personne("Zety"),
                au3juillet24,
                Set.of(ordinateur, vetements, especes, compteBancaire, fraisScolarite, fraisTenueCompte));

        // Date de projection
        LocalDate projectionDate = LocalDate.of(2024, SEPTEMBER, 17);

        assertEquals(3_178_848, patrimoineZety.projectionFuture(projectionDate).getValeurComptable());
    }
}
