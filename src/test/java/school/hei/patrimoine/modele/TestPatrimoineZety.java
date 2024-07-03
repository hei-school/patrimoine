package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static java.util.Calendar.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPatrimoineZety {

    @Test
    void patrimoine_evolue_au_17_septembre_2024() {
        var zety = new Personne("Zety");
        var au3juillet24 = LocalDate.of(2024, JULY, 3);
        var au17sept24 = LocalDate.of(2024, SEPTEMBER, 17);
        var ordinateur = new Materiel("Ordinateur", au3juillet24, 1_200_000, au3juillet24, -0.10);
        var vetements = new Materiel("Vêtements", au3juillet24, 1_500_000, au3juillet24, -0.50);
        var argentEspeces = new Argent("Espèces", au3juillet24, 800_000);
        var compteBancaire = new Argent("Compte bancaire", au3juillet24, 100_000);
        var fraisScolarite = new FluxArgent(
                "Frais de scolarité",
                argentEspeces,
                LocalDate.of(2023, NOVEMBER, 27),
                LocalDate.of(2024, AUGUST, 27),
                -200_000,
                30);
        var fraisTenueCompte = new FluxArgent(
                "Frais de tenue de compte",
                compteBancaire,
                au3juillet24,
                au17sept24.plusDays(1),
                -20_000,
                30);

        var patrimoineZety = new Patrimoine(
                "patrimoineZetyAu17sept24",
                zety,
                au3juillet24,
                Set.of(ordinateur, vetements, argentEspeces, compteBancaire, fraisScolarite, fraisTenueCompte));

        var evolutionPatrimoine = new EvolutionPatrimoine(
                "EvolutionPatrimoineZety",
                patrimoineZety,
                au3juillet24,
                au17sept24);

        var evolutionJournaliere = evolutionPatrimoine.getEvolutionJournaliere();
        var valeurPatrimoineAu17sept24 = evolutionJournaliere.get(au17sept24).getValeurComptable();

        assertEquals(3_181_232, valeurPatrimoineAu17sept24);
    }


    @Test
    void patrimoine_diminue_avec_dette_au_18_septembre_2024() {
        var zety = new Personne("Zety");

        var au3juillet24 = LocalDate.of(2024, JULY, 3);
        var au17sept24 = LocalDate.of(2024, SEPTEMBER, 17);
        var au18sept24 = LocalDate.of(2024, SEPTEMBER, 18);

        var ordinateur = new Materiel("Ordinateur", au3juillet24, 1_200_000, au3juillet24, -0.10);
        var vetements = new Materiel("Vêtements", au3juillet24, 1_500_000, au3juillet24, -0.50);
        var argentEspeces = new Argent("Espèces", au3juillet24, 800_000);
        var compteBancaire = new Argent("Compte bancaire", au3juillet24, 100_000);

        var fraisScolarite = new FluxArgent(
                "Frais de scolarité",
                argentEspeces,
                LocalDate.of(2023, NOVEMBER, 27),
                LocalDate.of(2024, AUGUST, 27),
                -200_000,
                30);

        var fraisTenueCompte = new FluxArgent(
                "Frais de tenue de compte",
                compteBancaire,
                au3juillet24,
                au17sept24.plusDays(1),
                -20_000,
                30);

        var emprunt = new FluxArgent(
                "Emprunt bancaire",
                compteBancaire,
                au18sept24,
                au18sept24,
                10_000_000,
                1);

        var dette = new FluxArgent(
                "Dette bancaire",
                compteBancaire,
                au18sept24,
                LocalDate.of(2025, SEPTEMBER, 18),
                -11_000_000,
                365);

        var patrimoineZety = new Patrimoine(
                "patrimoineZetyAu18sept24",
                zety,
                au3juillet24,
                Set.of(ordinateur, vetements, argentEspeces, compteBancaire, fraisScolarite, fraisTenueCompte, emprunt, dette));

        var evolutionPatrimoine = new EvolutionPatrimoine(
                "EvolutionPatrimoineZety",
                patrimoineZety,
                au3juillet24,
                au18sept24);

        var evolutionJournaliere = evolutionPatrimoine.getEvolutionJournaliere();
        var valeurPatrimoineAu17sept24 = evolutionJournaliere.get(au17sept24).getValeurComptable();
        var valeurPatrimoineAu18sept24 = evolutionJournaliere.get(au18sept24).getValeurComptable();

        var diminutionPatrimoine = valeurPatrimoineAu17sept24 - valeurPatrimoineAu18sept24;

        assertEquals(2384, diminutionPatrimoine);
    }


    @Test
    void zety_sans_especes_apres_le_1er_février_2025() {

        LocalDate debut2024 = LocalDate.of(2024, 1, 1);
        LocalDate debutOct2024 = LocalDate.of(2024, 10, 1);

        int soldeEspeces = 0;

        LocalDate dateDonMensuel = debut2024.withDayOfMonth(15);
        while (dateDonMensuel.isBefore(debutOct2024)) {
            soldeEspeces += 100000;
            dateDonMensuel = dateDonMensuel.plusMonths(1);
        }

        LocalDate dateTrainVie = debutOct2024.withDayOfMonth(1);
        while (soldeEspeces >= 250000 && dateTrainVie.isBefore(LocalDate.of(2025, 2, 14))) {
            soldeEspeces -= 250000;
            dateTrainVie = dateTrainVie.plusMonths(1);
        }

        LocalDate datePaiementFrais = LocalDate.of(2024, 9, 21);
        soldeEspeces -= 2500000;

        LocalDate dateEpuisementEspeces = datePaiementFrais;
        if (soldeEspeces >= 0) {
            dateEpuisementEspeces = dateTrainVie;
        }

        assertEquals(LocalDate.of(2025, 2, 1), dateEpuisementEspeces);
        assertEquals(0, soldeEspeces);
    }

    @Test
    void testPatrimoineAu14Fevrier2025() {
        Personne zety = new Personne("Zety");
        LocalDate dateReference = LocalDate.of(2024, 7, 3);

        Argent especes = new Argent("Espèces", dateReference, 800000);
        Argent compteBancaire = new Argent("Compte bancaire", dateReference, 100000);
        FluxArgent fraisScolarite = new FluxArgent("Frais de scolarité", especes, LocalDate.of(2023, 11, 27), LocalDate.of(2024, 8, 27), -200000, 30);

        Argent compteDeutscheBank = new Argent("Compte Deutsche Bank", dateReference, 0);
        TransfertArgent detteAllemagne = new TransfertArgent("Dette Allemagne", especes, compteDeutscheBank, LocalDate.of(2025, 2, 15), LocalDate.of(2025, 2, 15), 7000, 15);

        Set<Possession> possessions = new HashSet<>();
        possessions.add(especes);
        possessions.add(compteBancaire);
        possessions.add(fraisScolarite);
        possessions.add(detteAllemagne);

        Patrimoine patrimoineZety = new Patrimoine("Patrimoine de Zety", zety, dateReference, possessions);

        LocalDate date14Fevrier2025 = LocalDate.of(2025, 2, 14);
        int valeurPatrimoineAu14Fevrier2025 = patrimoineZety.projectionFuture(date14Fevrier2025).getValeurComptable();

        assertEquals(693000, valeurPatrimoineAu14Fevrier2025);
    }
}
