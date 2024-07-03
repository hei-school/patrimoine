package school.hei.patrimoine.Zety;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.EvolutionPatrimoine;
import school.hei.patrimoine.modele.Monnaie;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.*;
import school.hei.patrimoine.visualisation.xchart.GrapheurEvolutionPatrimoine;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EtudeTest {
    private final GrapheurEvolutionPatrimoine grapheurEvolutionPatrimoine = new GrapheurEvolutionPatrimoine();

    @Test
    void etude_2023_2024() {
        var zety = new Personne("Zety");
        var au3juillet2024 = LocalDate.of(2024, JULY, 3);
        var au28Aout2024 = LocalDate.of(2024, AUGUST, 28);
        var au17Sept2024 = LocalDate.of(2024, SEPTEMBER, 17);

        var arriary = new Monnaie("arriary", 4_821, au3juillet2024, -.1);

        var ordinateur = new Materiel(
                "Ordinateur",
                au3juillet2024,
                1_200_000,
                au3juillet2024,
                -.1, arriary);
        var vetements = new Materiel(
                "Vetements",
                au3juillet2024,
                1_500_000,
                au3juillet2024,
                -.5, arriary);
        var argentEspece = new Argent("espece", au3juillet2024, 800_000, arriary);
        var fraisScolarite = new FluxArgent(
                "Frais 2023-2024",
                argentEspece,
                au3juillet2024,
                au28Aout2024,
                -200_000,
                27, arriary);
        var compteBancaire = new Argent("Compte bancaire", au3juillet2024, 100_000, arriary);
        var fraisDeTenueDeCompte = new FluxArgent(
                "frais de tenue de compte",
                compteBancaire,
                au3juillet2024,
                au17Sept2024,
                -100_000,
                25, arriary);

        var patrimoineDeZety = new Patrimoine(
                "Patrimoine de Zety",
                zety,
                au3juillet2024,
                Set.of(ordinateur, vetements, argentEspece, fraisScolarite,
                        compteBancaire, fraisDeTenueDeCompte));

        var patrimoineAuJusquau17Sep = new EvolutionPatrimoine(
                "Test",
                patrimoineDeZety,
                au3juillet2024,
                au17Sept2024
        );
        // var viz = grapheurEvolutionPatrimoine.apply(patrimoineAuJusquau17Sep);

        assertEquals(2_818_848, patrimoineDeZety.projectionFuture(au17Sept2024).getValeurComptable());
    }

    @Test
    void etude_s_endette() {
        var zety = new Personne("Zety");
        var au3juillet2024 = LocalDate.of(2024, JULY, 3);
        var au17Sept2024 = LocalDate.of(2024, SEPTEMBER, 17);
        var au18Sept2024 = LocalDate.of(2024, SEPTEMBER, 18);
        var au18Sept2025 = LocalDate.of(2025, SEPTEMBER, 18);

        var arriary = new Monnaie("arriary", 4_821, au3juillet2024, -.1);

        var ordinateur = new Materiel(
                "Ordinateur",
                au3juillet2024,
                1_200_000,
                au3juillet2024,
                -.1, arriary);
        var vetements = new Materiel(
                "Vetements",
                au3juillet2024,
                1_500_000,
                au3juillet2024,
                -.5, arriary);
        var argentEspece = new Argent("espece", au3juillet2024, 800_000, arriary);
        var fraisScolarite = new FluxArgent(
                "Frais 2023-2024",
                argentEspece,
                au3juillet2024,
                au18Sept2024,
                -200_000,
                27, arriary);
        var compteBancaire = new Argent("Compte bancaire", au3juillet2024, 100_000, arriary);
        var fraisDeTenueDeCompte = new FluxArgent(
                "frais de tenue de compte",
                compteBancaire,
                au3juillet2024,
                au18Sept2025,
                -100_000,
                25, arriary);
        var detteDansCompte = new FluxArgent(
                "Ajouter dette dans compte",
                compteBancaire,
                au17Sept2024,
                au18Sept2024,
                10_000_000,
                18, arriary);
        var remboursementDedetteDansCompte = new FluxArgent(
                "Remboursement dette futur dans compte",
                compteBancaire,
                au18Sept2025.minusDays(1),
                au18Sept2025,
                -11_000_000,
                18, arriary);
        var dette = new Dette("Dette aupres une banque", au18Sept2024, -11_000_000, arriary);

        var patrimoineDeZety = new Patrimoine(
                "Patrimoine de Zety",
                zety,
                au3juillet2024,
                Set.of(ordinateur, vetements, argentEspece, fraisScolarite,
                        compteBancaire, fraisDeTenueDeCompte, dette, detteDansCompte, remboursementDedetteDansCompte));

        var patrimoineAuJusquau18Sep = new EvolutionPatrimoine(
                "Test",
                patrimoineDeZety,
                au3juillet2024,
                au18Sept2024
        );
        // var viz = grapheurEvolutionPatrimoine.apply(patrimoineAuJusquau18Sep);

        var differenceEntre17et18 = patrimoineDeZety.projectionFuture(au18Sept2024)
                .getValeurComptable() - patrimoineDeZety.projectionFuture(au17Sept2024)
                .getValeurComptable();
        assertEquals(-1_002_384, differenceEntre17et18);
    }

    @Test
    void etude_de_zety_2024_2025() {
        var zety = new Personne("Zety");
        var au3juillet2024 = LocalDate.of(2024, JULY, 3);
        var au17Sept2024 = LocalDate.of(2024, SEPTEMBER, 17);
        var au18Sept2024 = LocalDate.of(2024, SEPTEMBER, 18);
        var au18Sept2025 = LocalDate.of(2025, SEPTEMBER, 18);

        var arriary = new Monnaie("arriary", 4_821, au3juillet2024, -.1);

        // Annees scolaire 2024-2025
        var ordinateur = new Materiel(
                "Ordinateur",
                au3juillet2024,
                1_200_000,
                au3juillet2024,
                -.1, arriary);
        var vetements = new Materiel(
                "Vetements",
                au3juillet2024,
                1_500_000,
                au3juillet2024,
                -.5, arriary);
        var argentEspece = new Argent("espece", au3juillet2024, 800_000, arriary);
        var fraisScolarite = new FluxArgent(
                "Frais 2023-2024",
                argentEspece,
                au3juillet2024,
                au18Sept2024,
                -200_000,
                27, arriary);
        var compteBancaire = new Argent("Compte bancaire", au3juillet2024, 100_000, arriary);
        var fraisDeTenueDeCompte = new FluxArgent(
                "frais de tenue de compte",
                compteBancaire,
                au3juillet2024,
                au18Sept2025,
                -100_000,
                25, arriary);
        var detteDansCompte = new FluxArgent(
                "Ajouter dette dans compte",
                compteBancaire,
                au17Sept2024,
                au18Sept2024,
                10_000_000,
                18, arriary);
        var remboursementDedetteDansCompte = new FluxArgent(
                "Remboursement dette futur dans compte",
                compteBancaire,
                au18Sept2025.minusDays(1),
                au18Sept2025,
                -11_000_000,
                18, arriary);
        var creance = new Creance("Remboursement de la dette", au18Sept2025, 11_000_000, arriary);
        var dette = new Dette("Dette aupres une banque", au18Sept2024, -11_000_000, arriary);

        // Annees scolaire 2024-2025
        var au21Sept2024 = LocalDate.of(2024, SEPTEMBER, 21);
        var fraisDeScolarite2024_2025 = new FluxArgent("Scolarite 2024 2025",
                compteBancaire,
                au21Sept2024.minusDays(1),
                au21Sept2024,
                2_500_000,
                21, arriary);
        var donDesParents = new FluxArgent("Don des parents mensuel",
                argentEspece,
                LocalDate.of(2024, JANUARY, 1),
                au18Sept2025,
                100_000,
                15, arriary);
        var trainDeVieMensuel = new FluxArgent("Train de Vie",
                argentEspece,
                LocalDate.of(2024, OCTOBER, 1),
                LocalDate.of(2025, FEBRUARY, 13),
                -250_000,
                1, arriary);

        // Patrimoine total
        var patrimoineDeZety = new Patrimoine(
                "Patrimoine de Zety",
                zety,
                au3juillet2024,
                Set.of(ordinateur, vetements, argentEspece, fraisScolarite,
                        compteBancaire, fraisDeTenueDeCompte, dette, detteDansCompte,
                        remboursementDedetteDansCompte, creance,
                        fraisDeScolarite2024_2025, donDesParents, trainDeVieMensuel));

        var patrimoineAuJusquau18Sep = new EvolutionPatrimoine(
                "Test",
                patrimoineDeZety,
                au3juillet2024,
                au18Sept2025
        ).getEvolutionJournaliere();
        // var viz = grapheurEvolutionPatrimoine.apply(patrimoineAuJusquau18Sep);

        assertEquals(0, patrimoineAuJusquau18Sep
                .get(LocalDate.of(2025, JANUARY, 1))
                .possessionParNom(argentEspece.getNom()).getValeurComptable());
        assertEquals(250_000, patrimoineAuJusquau18Sep
                .get(LocalDate.of(2024, DECEMBER, 31))
                .possessionParNom(argentEspece.getNom()).getValeurComptable());
    }

    @Test
    void zety_abandonne_et_part_pour_l_allemagne() {
        var zety = new Personne("Zety");
        var au3juillet2024 = LocalDate.of(2024, JULY, 3);
        var au17Sept2024 = LocalDate.of(2024, SEPTEMBER, 17);
        var au18Sept2024 = LocalDate.of(2024, SEPTEMBER, 18);
        var au18Sept2025 = LocalDate.of(2025, SEPTEMBER, 18);
        var au14Fev2025 = LocalDate.of(2025, FEBRUARY, 14);

        var arriary = new Monnaie("arriary", 4_821, au3juillet2024, .1);

        // Annees scolaire 2024-2025
        var ordinateur = new Materiel(
                "Ordinateur",
                au3juillet2024,
                1_200_000,
                au3juillet2024,
                -.1, arriary);
        var vetements = new Materiel(
                "Vetements",
                au3juillet2024,
                1_500_000,
                au3juillet2024,
                -.5, arriary);
        var argentEspece = new Argent("espece", au3juillet2024, 800_000, arriary);
        var fraisScolarite = new FluxArgent(
                "Frais 2023-2024",
                argentEspece,
                au3juillet2024,
                au18Sept2024,
                -200_000,
                27, arriary);
        var compteBancaire = new Argent("Compte bancaire", au3juillet2024, 100_000, arriary);
        var fraisDeTenueDeCompte = new FluxArgent(
                "frais de tenue de compte",
                compteBancaire,
                au3juillet2024,
                au18Sept2025,
                -100_000,
                25, arriary);
        var detteDansCompte = new FluxArgent(
                "Ajouter dette dans compte",
                compteBancaire,
                au17Sept2024,
                au18Sept2024,
                10_000_000,
                18, arriary);
        var remboursementDedetteDansCompte = new FluxArgent(
                "Remboursement dette futur dans compte",
                compteBancaire,
                au18Sept2025.minusDays(1),
                au18Sept2025,
                -11_000_000,
                18, arriary);
        var creance = new Creance("Remboursement de la dette", au18Sept2025, 11_000_000, arriary);
        var dette = new Dette("Dette aupres une banque", au18Sept2024, -11_000_000, arriary);

        // Annees scolaire 2024-2025
        var au21Sept2024 = LocalDate.of(2024, SEPTEMBER, 21);
        var fraisDeScolarite2024_2025 = new FluxArgent("Scolarite 2024 2025",
                compteBancaire,
                au21Sept2024.minusDays(1),
                au21Sept2024,
                2_500_000,
                21, arriary);
        var donDesParents = new FluxArgent("Don des parents mensuel",
                argentEspece,
                LocalDate.of(2024, JANUARY, 1),
                au18Sept2025,
                100_000,
                15, arriary);
        var trainDeVieMensuel = new FluxArgent("Train de Vie",
                argentEspece,
                LocalDate.of(2024, OCTOBER, 1),
                LocalDate.of(2025, FEBRUARY, 13),
                -250_000,
                1, arriary);

        // Depart pour l'allemagne
        var au15fevrier2025 = LocalDate.of(2025, FEBRUARY, 15);
        var euro = new Monnaie("euro", 1, au15fevrier2025, 0);

        var compteEnAllemagne = new Argent("Compte en Allemagne", au15fevrier2025, 0, euro);

        var detteEnAllemagne = new Dette("Dette en Allemagne", au15fevrier2025, -7_000, euro);
        var detteDansCompteEnAllemagne = new FluxArgent(
                "Ajouter dette dans compte en allemagne",
                compteEnAllemagne,
                au15fevrier2025.minusDays(1),
                au15fevrier2025,
                7_000,
                15, euro);

        // Patrimoine total
        var patrimoineDeZety = new Patrimoine(
                "Patrimoine de Zety",
                zety,
                au3juillet2024,
                Set.of(ordinateur, vetements, argentEspece, fraisScolarite,
                        compteBancaire, fraisDeTenueDeCompte, dette, detteDansCompte,
                        remboursementDedetteDansCompte, creance,
                        fraisDeScolarite2024_2025, donDesParents, trainDeVieMensuel,
                        detteEnAllemagne, detteDansCompteEnAllemagne));

        var patrimoineAuJusquau18Sep = new EvolutionPatrimoine(
                "Test",
                patrimoineDeZety,
                au3juillet2024,
                au18Sept2025
        );
        // var viz = grapheurEvolutionPatrimoine.apply(patrimoineAuJusquau18Sep);

        var au26Oct2025 = LocalDate.of(2025, OCTOBER, 26);
        assertEquals(2_911_314, patrimoineDeZety.projectionFuture(au14Fev2025).getValeurComptable());
        assertEquals(439, arriary.projectionFutur(au26Oct2025).convert(patrimoineDeZety.projectionFuture(au26Oct2025).getValeurComptable(), euro));
    }
}