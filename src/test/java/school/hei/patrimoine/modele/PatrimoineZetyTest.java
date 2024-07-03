package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.JULY;
import static java.time.Month.SEPTEMBER;
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
}
