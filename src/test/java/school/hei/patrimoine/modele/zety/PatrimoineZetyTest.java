package school.hei.patrimoine.modele.zety;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PatrimoineZetyTest {

    @Test
    void testPatrimoineZetyLe17Septembre2024() {
        // Date de début des possessions de Zety
        LocalDate dateDebut = LocalDate.of(2024, JULY, 3);

        // Création des possessions de Zety le 3 juillet 2024
        Materiel ordinateur = new Materiel("Ordinateur", dateDebut, 1200000, dateDebut, -0.1 / 365);
        Materiel vetements = new Materiel("Vêtements", dateDebut, 1500000, dateDebut, -0.5 / 365);
        Argent argentEspeces = new Argent("Espèces", dateDebut, 800000);

        // Flux d'argent pour les frais de scolarité
        FluxArgent fraisScolarite = new FluxArgent(
                "Frais de scolarité",
                argentEspeces,
                LocalDate.of(2023, 11, 27).minusDays(1),
                LocalDate.of(2024, 8, 27),
                -200000,
                27);

        // Compte bancaire avec frais de tenue de compte
        Argent compteBancaire = new Argent("Compte bancaire", dateDebut, 100000);
        FluxArgent fraisTenueCompte = new FluxArgent(
                "Frais de tenue de compte",
                compteBancaire,
                LocalDate.of(2024, 7, 25).minusDays(1),
                LocalDate.of(2024, 9, 17),
                -20000,
                25);

        // Ajout des possessions au patrimoine de Zety
        Set<Possession> possessions = Set.of(ordinateur, vetements, argentEspeces, fraisScolarite, compteBancaire, fraisTenueCompte);

        // Création du patrimoine de Zety au 3 juillet 2024
        Patrimoine zetyPatrimoine = new Patrimoine("Zety", new Personne("Zety"), dateDebut, possessions);

        // Projection au 17 septembre 2024
        Patrimoine patrimoineFutur = zetyPatrimoine.projectionFuture(LocalDate.of(2024, 9, 17));

        // Calcul de la valeur comptable du patrimoine futur
        int valeurComptableFutur = patrimoineFutur.getValeurComptable();

        // calcul Valeur attendue

        int valeurAttendue = ordinateur.projectionFuture(LocalDate.of(2024, 9, 17)).getValeurComptable() +
                vetements.projectionFuture(LocalDate.of(2024, 9, 17)).getValeurComptable() +
                argentEspeces.projectionFuture(LocalDate.of(2024, 9, 17)).getValeurComptable() +
                fraisScolarite.projectionFuture(LocalDate.of(2024, 9, 17)).getValeurComptable() +
                compteBancaire.projectionFuture(LocalDate.of(2024, 9, 17)).getValeurComptable() +
                fraisTenueCompte.projectionFuture(LocalDate.of(2024, 9, 17)).getValeurComptable();

        // Assertion
        assertEquals(valeurAttendue, valeurComptableFutur);
    }
    @Test
    void zety_patrimoine_diminue_apres_emprunt() {
        var zety = new Personne("Zety");
        var au17sept24 = LocalDate.of(2024, 9, 17);
        var au18sept24 = LocalDate.of(2024, 9, 18);

        // Initialisation du patrimoine de Zety au 17 septembre 2024
        var patrimoineZetyAu17sept24 = new Patrimoine(
                "patrimoineZetyAu17sept24",
                zety,
                au17sept24,
                Set.of(new Argent("Compte bancaire", au17sept24, 100_000)));

        // Simulation de l'emprunt de Zety le 18 septembre 2024
        var compteBancaireApresEmprunt = new Argent("Compte bancaire", au18sept24, 10_100_000); // Montant sur le compte bancaire après emprunt
        var dette = new Dette("Dette bancaire", au18sept24, -11_000_000); // Dette contractée

        var patrimoineZetyAu18sept24 = new Patrimoine(
                "patrimoineZetyAu18sept24",
                zety,
                au18sept24,
                Set.of(compteBancaireApresEmprunt, dette));

        // Calcul de la diminution du patrimoine
        int diminutionPatrimoine = patrimoineZetyAu18sept24.getValeurComptable() - patrimoineZetyAu17sept24.getValeurComptable();

        assertEquals(-1_000_000, diminutionPatrimoine, "Le patrimoine de Zety devrait diminuer de 1 000 000 Ar après l'emprunt.");
    }
    @Test
    void zety_n_a_plus_d_especes_apres_1er_mars_2025() {
        var zety = new Personne("Zety");
        var au3juillet24 = LocalDate.of(2024, JULY, 3);

        // Initialisation du patrimoine de Zety au 3 juillet 2024
        var especes = new Argent("Especes", au3juillet24, 800_000);
        Set<Possession> initialPossessions = new HashSet<>(Set.of(especes));

        // Ajout des dons mensuels des parents à partir du 15 janvier 2024
        for (int month = 1; month <= 9; month++) {
            var donParent = new Argent(
                    "Don des parents",
                    LocalDate.of(2024, month, 15),
                    100_000);
            especes.addFinancés(new FluxArgent(
                    "FluxArgent",
                    donParent,
                    LocalDate.of(2024, month, 15),
                    LocalDate.of(2024, month, 15), // Fin doit être ajusté selon vos besoins
                    100_000, // fluxMensuel doit être ajusté selon vos besoins
                    15)); // dateOperation doit être ajusté selon vos besoins
            initialPossessions.add(donParent);
        }

        // Ajout des dépenses pour le train de vie à partir du 1 octobre 2024
        for (int month = 10; month <= 12; month++) {
            var trainDeVie = new Argent(
                    "Train de vie",
                    LocalDate.of(2024, month, 1),
                    -250_000);
            especes.addFinancés(new FluxArgent(
                    "FluxArgent",
                    trainDeVie,
                    LocalDate.of(2024, month, 1),
                    LocalDate.of(2025, FEBRUARY, 13),
                    -250_000,
                    1));
            initialPossessions.add(trainDeVie);
        }

        var patrimoineZetyAu3juillet24 = new Patrimoine(
                "patrimoineZetyAu3juillet24",
                zety,
                au3juillet24,
                initialPossessions);

        // Calcul du patrimoine au 1 mars 2025
        var patrimoineZetyAu1mars25 = patrimoineZetyAu3juillet24.projectionFuture(LocalDate.of(2025, MARCH, 1));

        var valeurEspeces = patrimoineZetyAu1mars25.possessions().stream()
                .filter(possession -> possession instanceof Argent && "Especes".equals(possession.getNom()))
                .mapToInt(possession -> {
                    if (possession instanceof Argent) {
                        return ((Argent) possession).getValeurComptable();
                    } else {
                        return 0;
                    }
                })
                .sum();

        assertEquals(0, valeurEspeces, "Zety n'a plus d'espèces à partir du 1er mars 2025.");
    }
    @Test
    void zety_patrimoine_valeur_en_euro_le_26_octobre_2025() {
        var zety = new Personne("Zety");
        var au3juillet24 = LocalDate.of(2024, 7, 3);

        // Initialisation du patrimoine de Zety au 3 juillet 2024
        var especes = new Argent("Espèces", au3juillet24, 800_000);
        var initialPossessions = new HashMap<>(Map.of(especes, "€"));

        // Ajout des dons mensuels des parents à partir du 15 janvier 2024
        for (int month = 1; month <= 9; month++) {
            var donParent = new FluxArgent(
                    "Don des parents",
                    especes,
                    LocalDate.of(2024, month, 15),
                    LocalDate.of(2024, month, 15),
                    100_000,
                    15);
            Argent.addFluxArgent(especes, donParent);
            initialPossessions.put(donParent, "€");
        }

        // Ajout des dépenses pour le train de vie à partir du 1 octobre 2024
        for (int month = 10; month <= 2; month++) {
            var trainDeVie = new FluxArgent(
                    "Train de vie",
                    especes,
                    LocalDate.of(2024, month, 1),
                    LocalDate.of(2025, 2, 13),
                    -250_000,
                    1);
            Argent.addFluxArgent(especes, trainDeVie);
            initialPossessions.put(trainDeVie, "€");
        }

        var patrimoineZetyAu3juillet24 = new Patrimoine(
                "patrimoineZetyAu3juillet24",
                zety,
                au3juillet24,
                initialPossessions.entrySet().stream().map(e -> new Possession(e.getKey().getNom(), e.getKey().getValeurComptable(), e.getKey().getCURRENCY())).collect(toSet()));

        // S'endetter de 7 000 € le 15 février 2025
        var dette = new Argent("Dette Deutsche Bank", LocalDate.of(2025, 2, 15), 7000);
        patrimoineZetyAu3juillet24.getPossessions().add(dette);

        // Calcul de la valeur du patrimoine le 26 octobre 2025
        Map<String, Double> exchangeRates = new HashMap<>();
        exchangeRates.put("€", 1.0);
        exchangeRates.put("Ar", 4821.0);
        double annualAppreciationRate = -10;
        double patrimoineValueInEuro = patrimoineZetyAu3juillet24.convertToEuro(LocalDate.of(2025, 10, 26), exchangeRates, annualAppreciationRate);

        assertEquals(0, patrimoineValueInEuro, "La valeur du patrimoine de Zety doit être de 0 € le 26 octobre 2025.");
    }
}
