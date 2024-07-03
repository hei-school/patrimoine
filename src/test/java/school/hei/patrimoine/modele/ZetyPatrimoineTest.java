package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.AchatMaterielAuComptant;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ZetyPatrimoineTest {

    @Test
    void patrimoine_zety_au_17_septembre_2024() {
        var zety = new Personne("Zety");
        var au3juillet24 = LocalDate.of(2024, JULY, 3);
        var au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);

        var compteBancaire = new Argent("Compte bancaire", au3juillet24, 100_000);
        var especes = new Argent("Espèces", au3juillet24, 800_000);

        var ordinateur = new AchatMaterielAuComptant(
                "Ordinateur",
                au3juillet24,
                1_200_000,
                -10.0 / 365,
                especes);

        var vetements = new Materiel(
                "Vêtements",
                au3juillet24,
                1_500_000,
                au3juillet24,
                -50.0 / 365);

        var fraisScolarite = new FluxArgent(
                "Frais de scolarité",
                especes,
                LocalDate.of(2023, NOVEMBER, 27),
                LocalDate.of(2024, AUGUST, 27),
                -200_000,
                27);

        var fraisTenueCompte = new FluxArgent(
                "Frais de tenue de compte",
                compteBancaire,
                LocalDate.of(2024, JULY, 25),
                LocalDate.of(2100, JANUARY, 1),
                -20_000,
                25);

        var patrimoineZetyAu3juillet24 = new Patrimoine(
                "patrimoineZetyAu3juillet24",
                zety,
                au3juillet24,
                Set.of(ordinateur, vetements, especes, compteBancaire, fraisScolarite, fraisTenueCompte));

        var evolutionPatrimoine = new EvolutionPatrimoine(
                "Evolution de Zety",
                patrimoineZetyAu3juillet24,
                au3juillet24,
                au17septembre24);

        var valeurPatrimoineAu17Septembre = evolutionPatrimoine.getEvolutionJournaliere().get(au17septembre24).getValeurComptable();
        assertEquals(1_910_369, valeurPatrimoineAu17Septembre);
    }
}