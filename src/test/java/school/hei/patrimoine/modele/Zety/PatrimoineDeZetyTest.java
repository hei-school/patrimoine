package school.hei.patrimoine.modele.Zety;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatrimoineDeZetyTest {
    @Test
    void patrimoine_vaut_0() {
        var zety = new Personne("Zety");
        var patrimoineZetyAu17sept24 = new Patrimoine("patrimoineDeZetyAu17sept24",
                zety,
                LocalDate.of(2024, SEPTEMBER,17),
                Set.of());
        assertEquals(0, patrimoineZetyAu17sept24.getValeurComptable());
    }
    @Test
    void patrimoine_de_Zety_le_17_septembre_2024() {
        LocalDate au3jul24 = LocalDate.of(2024, JULY, 3);

        Materiel ordinateur = new Materiel(
                "Ordinateur",
                au3jul24,
                1_200_000,
                au3jul24,
                -0.10);

        Materiel vetements = new Materiel(
                "Vêtements",
                au3jul24,
                1_500_000,
                au3jul24,
                -0.50);

        Argent argentEspeces = new Argent(
                "Espèces",
                au3jul24,
                800_000);

        LocalDate debutFraisScolarite = LocalDate.of(2023, NOVEMBER, 27);
        LocalDate finFraisScolarite = LocalDate.of(2024, AUGUST, 27);
        FluxArgent fraisScolarite = new FluxArgent(
                "Frais de scolarité",
                argentEspeces,
                debutFraisScolarite,
                finFraisScolarite,
                200_000,
                27);

        Argent compteBancaire = new Argent(
                "Compte Bancaire",
                au3jul24,
                100_000);

        LocalDate debutCompteBancaire = LocalDate.of(2024, JULY, 25);
        FluxArgent fraisCompteBancaire = new FluxArgent(
                "Frais de tenue de compte",
                compteBancaire,
                debutCompteBancaire,
                LocalDate.MAX,
                -20_000,
                25);

        argentEspeces.addFinancés(fraisScolarite);
        compteBancaire.addFinancés(fraisCompteBancaire);

        Patrimoine patrimoineZety = new Patrimoine(
                "patrimoineZetyLe17Sept24",
                new Personne("Zety"),
                au3jul24,
                Set.of(ordinateur, vetements, argentEspeces, fraisScolarite, compteBancaire, fraisCompteBancaire));

        patrimoineZety = patrimoineZety.projectionFuture(LocalDate.of(2024, SEPTEMBER, 17));

        double valeurComptableAttendue =
                ordinateur.projectionFuture(LocalDate.of(2024, SEPTEMBER, 17)).getValeurComptable() +
                        vetements.projectionFuture(LocalDate.of(2024, SEPTEMBER, 17)).getValeurComptable() +
                        argentEspeces.projectionFuture(LocalDate.of(2024, SEPTEMBER, 17)).getValeurComptable() +
                        fraisScolarite.projectionFuture(LocalDate.of(2024, SEPTEMBER, 17)).getValeurComptable() +
                        compteBancaire.projectionFuture(LocalDate.of(2024, SEPTEMBER, 17)).getValeurComptable() +
                        fraisCompteBancaire.projectionFuture(LocalDate.of(2024, SEPTEMBER, 17)).getValeurComptable();

        assertEquals(2978848.0, valeurComptableAttendue);
    }
    @Test
    void diminution_Patrimoine_Entre17_Et_18Septembre2024() {
        var zety = new Personne("Zety");
        var au3jul24 = LocalDate.of(2024, JULY, 3);

        var ordinateur = new Materiel(
                "Ordinateur",
                au3jul24,
                1_200_000,
                au3jul24,
                -0.10);

        var vetements = new Materiel(
                "Vêtements",
                au3jul24,
                1_500_000,
                au3jul24,
                -0.50);

        var argentEspeces = new Argent(
                "Espèces",
                au3jul24,
                800_000);

        var debutFraisScolarite = LocalDate.of(2023, NOVEMBER, 27);
        var finFraisScolarite = LocalDate.of(2024, AUGUST, 27);
        var fraisScolarite = new FluxArgent(
                "Frais de scolarité",
                argentEspeces,
                debutFraisScolarite,
                finFraisScolarite,
                200_000,
                27);

        var compteBancaire = new Argent(
                "Compte Bancaire",
                au3jul24,
                100_000);

        var debutCompteBancaire = LocalDate.of(2024, JULY, 25);
        var fraisCompteBancaire = new FluxArgent(
                "Frais de tenue de compte",
                compteBancaire,
                debutCompteBancaire,
                LocalDate.MAX,
                -20_000,
                25);

        argentEspeces.addFinancés(fraisScolarite);
        compteBancaire.addFinancés(fraisCompteBancaire);

        var patrimoineZetyAu17sept24 = new Patrimoine(
                "patrimoineZetyAu17sept24",
                zety,
                au3jul24,
                Set.of(
                        ordinateur,
                        vetements,
                        argentEspeces,
                        fraisScolarite,
                        compteBancaire,
                        fraisCompteBancaire
                )
        );

        var endettement = new Dette(
                "Dette bancaire",
                LocalDate.of(2024, SEPTEMBER, 18),
                -11_000_000);

        var patrimoineZetyAu18sept24 = patrimoineZetyAu17sept24.ajouterPossession(endettement);

        var diminutionPatrimoine = patrimoineZetyAu18sept24.getValeurComptable() - patrimoineZetyAu17sept24.getValeurComptable();

        assertEquals(1_000_000, diminutionPatrimoine);
    }

    @Test
    void date_Epuisement_Espèces() {

        var au3jul24 = LocalDate.of(2024, JULY, 3);

        var argentEspeces = new Argent(
                "Espèces",
                au3jul24,
                800_000);


        var debutDonMensuel = LocalDate.of(2024, JANUARY, 15);
        var finDonMensuel = LocalDate.of(2025, FEBRUARY, 15);
        var donMensuel = new FluxArgent(
                "Don mensuel des parents",
                argentEspeces,
                debutDonMensuel,
                finDonMensuel,
                100_000,
                30);


        var debutTrainDeVie = LocalDate.of(2024, OCTOBER, 1);
        var finTrainDeVie = LocalDate.of(2025, FEBRUARY, 13);
        var trainDeVie = new FluxArgent(
                "Train de vie mensuel",
                argentEspeces,
                debutTrainDeVie,
                finTrainDeVie,
                -250_000,
                0);

        var fraisScolarite = new FluxArgent(
                "Frais de scolarité",
                argentEspeces,
                LocalDate.of(2024, SEPTEMBER, 21),
                LocalDate.of(2024, SEPTEMBER, 21),
                -2_500_000,
                21);

        argentEspeces.addFinancés(fraisScolarite);


        LocalDate dateEpuisement = au3jul24;
        int soldeEspèces = argentEspeces.getValeurComptable();

        while (soldeEspèces >= 0) {
            dateEpuisement = dateEpuisement.plusDays(1);
            soldeEspèces = argentEspeces.projectionFuture(dateEpuisement).getValeurComptable();
        }

        assertEquals(LocalDate.of(2024, SEPTEMBER, 21), dateEpuisement);
    }
    @Test
    void valeur_Patrimoine_Au_14Fevrier2025() {
        Personne zety = new Personne("Zety");
        LocalDate au3jul24 = LocalDate.of(2024, 7, 3);

        Materiel ordinateur = new Materiel(
                "Ordinateur",
                au3jul24,
                1_200_000,
                au3jul24,
                -0.10);

        Materiel vetements = new Materiel(
                "Vêtements",
                au3jul24,
                1_500_000,
                au3jul24,
                -0.50);

        Argent argentEspeces = new Argent(
                "Espèces",
                au3jul24,
                800_000);

        LocalDate debutFraisScolarite = LocalDate.of(2023, 11, 27);
        LocalDate finFraisScolarite = LocalDate.of(2024, 8, 27);
        FluxArgent fraisScolarite = new FluxArgent(
                "Frais de scolarité",
                argentEspeces,
                debutFraisScolarite,
                finFraisScolarite,
                200_000,
                27);

        Argent compteBancaire = new Argent(
                "Compte Bancaire",
                au3jul24,
                100_000);

        LocalDate debutCompteBancaire = LocalDate.of(2024, 7, 25);
        FluxArgent fraisCompteBancaire = new FluxArgent(
                "Frais de tenue de compte",
                compteBancaire,
                debutCompteBancaire,
                LocalDate.MAX,
                -20_000,
                25);

        argentEspeces.addFinancés(fraisScolarite);
        compteBancaire.addFinancés(fraisCompteBancaire);

        Patrimoine patrimoineAu14Fevrier2025 = new Patrimoine(
                "Patrimoine de Zety au 14 février 2025",
                new Personne("Zety"),
                LocalDate.of(2025, 2, 14),
                Set.of(ordinateur, vetements, argentEspeces, fraisScolarite, compteBancaire, fraisCompteBancaire)
        );

        Dette detteAllemagne = new Dette("Dette en Allemagne", LocalDate.of(2025, 2, 15), -7000);

        patrimoineAu14Fevrier2025 = patrimoineAu14Fevrier2025.ajouterPossession(detteAllemagne);

        assertEquals(3593000.0, patrimoineAu14Fevrier2025.getValeurComptable());
    }
}
