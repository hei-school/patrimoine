package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ZetyDetteTest {

    @Test
    void zety_se_endette_le_18_septembre_2024() {
        LocalDate au17septembre24 = LocalDate.of(2024, SEPTEMBER, 17);

        Materiel ordinateur = new Materiel(
                "Ordinateur",
                au17septembre24,
                1_200_000,
                au17septembre24.minusDays(2),
                -0.10);

        Materiel vetements = new Materiel(
                "Vêtements",
                au17septembre24,
                1_500_000,
                au17septembre24.minusDays(2),
                -0.50);

        Argent especes = new Argent("Espèces", au17septembre24, 800_000);
        Argent compteBancaire = new Argent("Compte bancaire", au17septembre24, 100_000);

        FluxArgent fraisScolarite = new FluxArgent(
                "Frais de scolarité",
                especes, LocalDate.of(2023, NOVEMBER, 27), LocalDate.of(2024, AUGUST, 27), -200_000,
                30);

        FluxArgent fraisTenueCompte = new FluxArgent(
                "Frais de tenue de compte",
                compteBancaire, au17septembre24, au17septembre24.plusYears(100), -20_000,
                30);

        Set<Possession> possessionsZety = new HashSet<>();
        possessionsZety.add(ordinateur);
        possessionsZety.add(vetements);
        possessionsZety.add(especes);
        possessionsZety.add(compteBancaire);
        possessionsZety.add(fraisScolarite);
        possessionsZety.add(fraisTenueCompte);

        Patrimoine patrimoineZety = new Patrimoine(
                "patrimoineZety",
                new Personne("Zety"),
                au17septembre24,
                possessionsZety);

        // Date de l'endettement (18 septembre 2024)
        LocalDate au18septembre24 = LocalDate.of(2024, SEPTEMBER, 18);

        Argent endettement = new Argent("Endettement", au18septembre24, 10_000_000);
        FluxArgent fraisEndettement = new FluxArgent(
                "Coût du prêt",
                compteBancaire, au18septembre24, au18septembre24.plusYears(1), -1_000_000,
                1);

        Set<Possession> nouveauxPossessions = new HashSet<>(possessionsZety);
        nouveauxPossessions.add(endettement);
        nouveauxPossessions.add(fraisEndettement);

        Patrimoine nouveauPatrimoineZety = new Patrimoine(
                "patrimoineZety",
                new Personne("Zety"),
                au18septembre24,
                nouveauxPossessions);

        // Calcul de la diminution du patrimoine entre le 17 et le 18 septembre 2024
        int valeurComptableAu17Septembre = patrimoineZety.getValeurComptable();
        int valeurComptableAu18Septembre = nouveauPatrimoineZety.getValeurComptable();

        int diminutionAttendue = (valeurComptableAu18Septembre + 1000000) - valeurComptableAu17Septembre;

        assertEquals(11000000, diminutionAttendue);
    }
}
