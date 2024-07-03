package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EvolutionPatrimoineTest {

    @Test
    void patrimoine_evolue() {
        var ilo = new Personne("Ilo");
        var au13mai24 = LocalDate.of(2024, MAY, 13);
        var financeur = new Argent("Espèces", au13mai24, 600_000);
        var trainDeVie = new FluxArgent(
                "Vie courante",
                financeur, au13mai24.minusDays(100), au13mai24.plusDays(100), -100_000,
                15);
        var patrimoineIloAu13mai24 = new Patrimoine(
                "patrimoineIloAu13mai24",
                ilo,
                au13mai24,
                Set.of(financeur, trainDeVie));

        var evolutionPatrimoine = new EvolutionPatrimoine(
                "Nom",
                patrimoineIloAu13mai24,
                LocalDate.of(2024, MAY, 12),
                LocalDate.of(2024, MAY, 17));

        var evolutionJournaliere = evolutionPatrimoine.getEvolutionJournaliere();
        assertEquals(0, evolutionJournaliere.get(LocalDate.of(2024, MAY, 12)).getValeurComptable());
        assertEquals(600_000, evolutionJournaliere.get(LocalDate.of(2024, MAY, 13)).getValeurComptable());
        assertEquals(600_000, evolutionJournaliere.get(LocalDate.of(2024, MAY, 14)).getValeurComptable());
        assertEquals(500_000, evolutionJournaliere.get(LocalDate.of(2024, MAY, 15)).getValeurComptable());
        assertEquals(500_000, evolutionJournaliere.get(LocalDate.of(2024, MAY, 16)).getValeurComptable());
        assertEquals(500_000, evolutionJournaliere.get(LocalDate.of(2024, MAY, 17)).getValeurComptable());
    }

    @Test
    void valeur_patrimoine_de_Zety_au_17_septembre_2024() {
        var zety = new Personne("Zety");
        var dateDebut = LocalDate.of(2024, JULY, 3);

        var ordinateur = new Materiel("Ordinateur", dateDebut, 1_200_000, dateDebut, -0.10);
        var vetement = new Materiel("Vetement", dateDebut, 1_500_000, dateDebut, -0.50);
        var argentEnEspece = new Argent("espece zety", dateDebut, 800_000);
        var fraisEcolage = new FluxArgent("ecolage", argentEnEspece, LocalDate.of(2023,
                NOVEMBER, 27),
                LocalDate.of(2024, AUGUST, 27),
                -200_000, 27);
        var compteBancaire = new Argent("compte bancaire", dateDebut, 100_000);
        var fraisCompteBancaire = new FluxArgent("frais de compte",
                compteBancaire,
                dateDebut,
                LocalDate.of(2025, MAY, 5),
                -20_000, 25);

        var patrimoineDeZety = new Patrimoine("patrimoine de Zety", zety, dateDebut,
                Set.of(ordinateur, vetement, argentEnEspece, fraisEcolage, compteBancaire, fraisCompteBancaire));

        var dateDePrediction = LocalDate.of(2024, SEPTEMBER, 17);
        var patrimoineProjete = patrimoineDeZety.projectionFuture(dateDePrediction);

        var valeurComptable = patrimoineProjete.getValeurComptable();
        assertEquals(2_978_848, valeurComptable);
    }

    @Test
    void zety_s_endette() {
        var zety = new Personne("Zety");
        var au03Juillet2024 = LocalDate.of(2024, JULY, 3);

        var ordinateur = new Materiel("Ordinateur", au03Juillet2024, 1_200_000, au03Juillet2024, -0.1);
        var vetements = new Materiel("Garde-Robe", au03Juillet2024, 1_500_000, au03Juillet2024, -0.5);

        var espece = new Argent("Espèces", au03Juillet2024, 800_000);
        var debutFraisScolarite = LocalDate.of(2023, NOVEMBER, 01);
        var finFraisDeScolarite = LocalDate.of(2024, AUGUST, 28);

        var compteBancaire = new Argent("Compte Bancaire", au03Juillet2024, 100_000);

        var fraisDeTenueDeCompte =
                new FluxArgent(
                        "Frais de tenue de compte",
                        compteBancaire,
                        au03Juillet2024,
                        LocalDate.MAX,
                        -20_000,
                        25);

        var au18Septembre2024 = LocalDate.of(2024, SEPTEMBER, 18);
        var empruntFraisDeScolarite =
                new FluxArgent(
                        "Emprunt frais de scolarité",
                        compteBancaire,
                        au18Septembre2024,
                        au18Septembre2024,
                        10_000_000,
                        au18Septembre2024.getDayOfMonth());

        var dette = new Dette("Dette emprunt frais de scolarités", au18Septembre2024, -11_000_000);

        var patrimoineZetyAu03Juillet2024 =
                new Patrimoine(
                        "patrimoineZetyAu03Juillet2024",
                        zety,
                        au03Juillet2024,
                        Set.of(ordinateur, vetements, espece, compteBancaire, dette));

        var au17Septembre = LocalDate.of(2024, SEPTEMBER, 17);
        var patrimoineZetyAu17Septembre = patrimoineZetyAu03Juillet2024.projectionFuture(au17Septembre);

        var patrimoineZetyAu18Septembre = patrimoineZetyAu03Juillet2024.projectionFuture(au18Septembre2024);

        var diminutionPatrimoineAttendue = 1002384;
        var diminutionPatrimoineActuelle = patrimoineZetyAu17Septembre.getValeurComptable() - patrimoineZetyAu18Septembre.getValeurComptable();

        assertEquals(diminutionPatrimoineAttendue, diminutionPatrimoineActuelle);
    }

    @Test
    void zety_n_a_plus_d_especes() {
        var zety = new Personne("Zety");
        var dateDebut = LocalDate.of(2024, JULY, 3);

        var ordinateur = new Materiel("Ordinateur", dateDebut, 1_200_000, dateDebut, -0.10);
        var vetement = new Materiel("Vetement", dateDebut, 1_500_000, dateDebut, -0.50);
        var argentEnEspece = new Argent("espece zety", dateDebut, 800_000);
        var fraisEcolage = new FluxArgent("ecolage", argentEnEspece, LocalDate.of(2023, NOVEMBER, 27), LocalDate.of(2024, AUGUST, 27), -200_000, 27);
        var compteBancaire = new Argent("compte bancaire", dateDebut, 100_000);
        var fraisCompteBancaire = new FluxArgent("frais de compte", compteBancaire, dateDebut, LocalDate.of(2025, MAY, 5), -20_000, 25);

        var fraisScolarite = new FluxArgent("frais scolarité", compteBancaire, LocalDate.of(2024, SEPTEMBER, 21), LocalDate.of(2024, SEPTEMBER, 21), -2_500_000, 0);

        var donMensuel = new FluxArgent("don parents", argentEnEspece, LocalDate.of(2024, JANUARY, 15), LocalDate.of(2024, DECEMBER, 15), 100_000, 30);

        var trainDeVie = new FluxArgent("train de vie", argentEnEspece, LocalDate.of(2024, OCTOBER, 1), LocalDate.of(2025, FEBRUARY, 13), -250_000, 30);

        var patrimoineDeZety = new Patrimoine("patrimoine de Zety", zety, dateDebut,
                Set.of(ordinateur, vetement, argentEnEspece, fraisEcolage, compteBancaire, fraisCompteBancaire, fraisScolarite, donMensuel, trainDeVie));

        LocalDate dateDePrediction = LocalDate.of(2024, SEPTEMBER, 21);
        Patrimoine patrimoineProjete = patrimoineDeZety.projectionFuture(dateDePrediction);

        LocalDate dateFin = LocalDate.of(2025, FEBRUARY, 13);
        LocalDate dateActuelle = dateDePrediction;
        while (dateActuelle.isBefore(dateFin) || dateActuelle.isEqual(dateFin)) {
            patrimoineProjete = patrimoineProjete.projectionFuture(dateActuelle);
            var espece = (Argent) patrimoineProjete.possessionParNom("espece zety");
            if (espece.getValeurComptable() <= 0) {
                System.out.println("Zety n'a plus d'espèces à partir du : " + dateActuelle);
                assertTrue(dateActuelle.isAfter(dateDePrediction));
                return;
            }
            dateActuelle = dateActuelle.plusDays(1);
        }
    }
}
