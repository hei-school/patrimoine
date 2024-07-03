package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
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
        assertTrue(valeurComptable>2_900_000 && valeurComptable<3_000_000);
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
        var zety = new Personne("zety");
        var au3juillet24 = LocalDate.of(2024, JULY, 3);

        var ordinateur = new Materiel(
                "Ordinateur",
                au3juillet24,
                1_200_000,
                au3juillet24,
                -0.10);

        var vetements = new Materiel(
                "Vêtements",
                au3juillet24,
                1_500_000,
                au3juillet24,
                -0.50);

        var espece = new Argent("espèce", au3juillet24, 800_000);

        var debutScolarite = LocalDate.of(2023, NOVEMBER, 1);
        var finScolarite = LocalDate.of(2024, AUGUST, 30);
        var fraisDeScolarite2324 = new FluxArgent(
                "Frais de Scolarité 2023-2024",
                espece,
                debutScolarite,
                finScolarite,
                -200_000,
                27);


        var compteBancaire = new Argent("CompteBancaire", au3juillet24, 100_000);
        var fraisDuCompte = new FluxArgent(
                "Frais de compte",
                compteBancaire,
                au3juillet24,
                LocalDate.MAX,
                -20_000,
                25);

        var dateEmprunt = LocalDate.of(2024, SEPTEMBER, 18);
        var dateRemb = dateEmprunt.plusYears(1);
        var dette = new Dette("Dette Scolarité", au3juillet24,0);

        var pret = new FluxArgent("Frais De Scolarité Prêt", compteBancaire, dateEmprunt, dateEmprunt, 10_000_000, dateEmprunt.getDayOfMonth());
        var detteAjout = new FluxArgent("Frais De Scolarité Dette", dette, dateEmprunt, dateEmprunt, -11_000_000, dateEmprunt.getDayOfMonth());
        var remboursement = new FluxArgent("Frais De Scolarité Rem", compteBancaire, dateRemb, dateRemb, -11_000_000, dateRemb.getDayOfMonth());
        var detteAnnulation = new FluxArgent("Frais De Scolarité annulation", dette, dateRemb, dateRemb, 11_000_000, dateRemb.getDayOfMonth());

        var au21septembre24 = LocalDate.of(2024, SEPTEMBER, 21);
        var fraisDeScolarite2425 = new FluxArgent(
                "Frais de Scolarité 2024-2025",
                compteBancaire,
                au21septembre24,
                au21septembre24,
                -2_500_000,
                au21septembre24.getDayOfMonth()
        );

        var transfertParent = new FluxArgent(
                "Transfert Parent",
                espece,
                LocalDate.of(2024, JANUARY, 1),
                LocalDate.MAX,
                100_000,
                15
        );

        var trainDeVie = new FluxArgent(
                "Train de vie",
                espece,
                LocalDate.of(2024, OCTOBER, 1),
                LocalDate.of(2025, FEBRUARY, 13),
                -250_000,
                1
        );

        var patrimoineZetyAu3juillet24 = new Patrimoine(
                "patrimoineZetyAu3juillet24",
                zety,
                au3juillet24,
                Set.of(ordinateur,
                        vetements,
                        espece,
                        compteBancaire,
                        fraisDuCompte,
                        dette,
                        new GroupePossession("Dette sur le compte bancaire", au3juillet24, Set.of(pret, detteAjout, remboursement, detteAnnulation)),
                        new GroupePossession("Espece Flux", au3juillet24, Set.of(fraisDeScolarite2324,fraisDeScolarite2425, transfertParent, trainDeVie))
                )
        );

        var especeValeurComptable = patrimoineZetyAu3juillet24.possessionParNom("espèce").getValeurComptable();
        var nombreDeJour = 0;
        for (int i = 1; especeValeurComptable > 0 ; i++) {
            especeValeurComptable = patrimoineZetyAu3juillet24.possessionParNom("espèce").projectionFuture(au3juillet24.plusDays(i)).getValeurComptable();
            nombreDeJour = i;
        }

        assertTrue(patrimoineZetyAu3juillet24.possessionParNom("espèce").projectionFuture(au3juillet24.plusDays(nombreDeJour - 1)).getValeurComptable() > 0);
        assertTrue(patrimoineZetyAu3juillet24.possessionParNom("espèce").projectionFuture(au3juillet24.plusDays(nombreDeJour)).getValeurComptable() <= 0);
        assertEquals(LocalDate.of(2025, JANUARY, 1), au3juillet24.plusDays(nombreDeJour));
    }

    
}

