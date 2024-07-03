package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
  void patrimoine_zety_au_17_septembre_2024() {
    var zety = new Personne("Zety");
    var dateDebut = LocalDate.of(2024, JULY, 3);

    var ordinateur = new Materiel("Ordinateur", dateDebut, 1_200_000, dateDebut, -0.10);
    var vetements = new Materiel("Vêtements", dateDebut, 1_500_000, dateDebut, -0.50);
    var argentEspeces = new Argent("Argent en espèces", dateDebut, 800_000);
    var compteBancaire = new Argent("Compte bancaire", dateDebut, 100_000);

    var fraisScolarite = new FluxArgent("Frais de scolarité", argentEspeces,
            LocalDate.of(2023, 11, 27), LocalDate.of(2024, 8, 27), -200_000, 27);
    var fraisTenueCompte = new FluxArgent("Frais de tenue de compte", compteBancaire,
            dateDebut, LocalDate.of(9999, 12, 31), -20_000, 25);

    var patrimoineZety = new Patrimoine(
            "PatrimoineZetyAu3Juillet2024",
            zety,
            dateDebut,
            Set.of(ordinateur, vetements, argentEspeces, compteBancaire, fraisScolarite, fraisTenueCompte));

    var evolutionPatrimoine = new EvolutionPatrimoine(
            "EvolutionPatrimoineZety",
            patrimoineZety,
            dateDebut,
            LocalDate.of(2024, SEPTEMBER, 17));

    var evolutionJournaliere = evolutionPatrimoine.getEvolutionJournaliere();

    double tauxDepreciationOrdinateur = -0.10 / 365;
    int joursEcoules = 76;
    double valeurFutureOrdinateur = 1_200_000 * Math.pow(1 + tauxDepreciationOrdinateur, joursEcoules);
    assertEquals(1175268.69, valeurFutureOrdinateur, 0.01);

    double tauxDepreciationVetements = -0.50 / 365;
    double valeurFutureVetements = 1_500_000 * Math.pow(1 + tauxDepreciationVetements, joursEcoules);
    assertEquals(1351593.33, valeurFutureVetements, 0.01);

    int paiementsFraisScolarite = 2;
    int valeurFutureArgentEspeces = 800_000 - paiementsFraisScolarite * 200_000;
    assertEquals(400_000, valeurFutureArgentEspeces);

    int paiementsFraisTenueCompte = 2;
    int valeurFutureCompteBancaire = 100_000 - paiementsFraisTenueCompte * 20_000;
    assertEquals(60_000, valeurFutureCompteBancaire);

    double valeurTotaleFuture = valeurFutureOrdinateur + valeurFutureVetements + valeurFutureArgentEspeces + valeurFutureCompteBancaire;
    assertEquals(2986862.01, valeurTotaleFuture, 0.01);

    var valeurPatrimoineAu17Septembre2024 = evolutionJournaliere.get(LocalDate.of(2024, SEPTEMBER, 17)).getValeurComptable();
    assertEquals(2978848, valeurPatrimoineAu17Septembre2024);
  }

  @Test
  void patrimoine_zety_entre_17_et_18_septembre_2024() {
    var zety = new Personne("Zety");
    var dateDebut = LocalDate.of(2024, JULY, 3);

    var ordinateur = new Materiel("Ordinateur", dateDebut, 1_200_000, dateDebut, -0.10);
    var vetements = new Materiel("Vêtements", dateDebut, 1_500_000, dateDebut, -0.50);
    var argentEspeces = new Argent("Argent en espèces", dateDebut, 800_000);
    var compteBancaire = new Argent("Compte bancaire", dateDebut, 100_000);

    var fraisScolarite = new FluxArgent("Frais de scolarité", argentEspeces,
            LocalDate.of(2023, 11, 27), LocalDate.of(2024, 8, 27), -200_000, 27);
    var fraisTenueCompte = new FluxArgent("Frais de tenue de compte", compteBancaire,
            dateDebut, LocalDate.of(9999, 12, 31), -20_000, 25);

    var dette = new Dette("Dette", LocalDate.of(2024, SEPTEMBER, 18), -11_000_000);

    var empruntBanque = new FluxArgent("Emprunt bancaire", compteBancaire,
            LocalDate.of(2024, SEPTEMBER, 18), LocalDate.of(2024, SEPTEMBER, 18), 10_000_000, 18);

    var patrimoineZety = new Patrimoine(
            "PatrimoineZetyAu3Juillet2024",
            zety,
            dateDebut,
            Set.of(ordinateur, vetements, argentEspeces, compteBancaire, fraisScolarite, fraisTenueCompte, empruntBanque, dette));

    var evolutionPatrimoine = new EvolutionPatrimoine(
            "EvolutionPatrimoineZety",
            patrimoineZety,
            dateDebut,
            LocalDate.of(2024, SEPTEMBER, 18));

    var evolutionJournaliere = evolutionPatrimoine.getEvolutionJournaliere();

    var valeurPatrimoineAu17Septembre2024 = evolutionJournaliere.get(LocalDate.of(2024, SEPTEMBER, 17)).getValeurComptable();

    var valeurPatrimoineAu18Septembre2024 = evolutionJournaliere.get(LocalDate.of(2024, SEPTEMBER, 18)).getValeurComptable();

    int diminutionPatrimoine = valeurPatrimoineAu17Septembre2024 - valeurPatrimoineAu18Septembre2024;

    assertEquals(-8021152, valeurPatrimoineAu17Septembre2024);
    assertEquals(1976464, valeurPatrimoineAu18Septembre2024);
    assertEquals(-9997616, diminutionPatrimoine);
  }

  @Test
  void date_fin_espèces_de_Zety() {
    var zety = new Personne("Zety");
    var dateDebut = LocalDate.of(2024, JULY, 3);

    var ordinateur = new Materiel("Ordinateur", dateDebut, 1_200_000, dateDebut, -0.10);
    var vetements = new Materiel("Vêtements", dateDebut, 1_500_000, dateDebut, -0.50);
    var argentEspeces = new Argent("Argent en espèces", dateDebut, 800_000);
    var compteBancaire = new Argent("Compte bancaire", dateDebut, 100_000);

    var fraisScolarite = new FluxArgent("Frais de scolarité", argentEspeces,
            LocalDate.of(2023, 11, 27), LocalDate.of(2024, 8, 27), -200_000, 27);
    var fraisTenueCompte = new FluxArgent("Frais de tenue de compte", compteBancaire,
            dateDebut, LocalDate.of(9999, 12, 31), -20_000, 25);

    var paiementFraisScolarite = new FluxArgent("Paiement frais scolarité 2024-2025", compteBancaire,
            LocalDate.of(2024, SEPTEMBER, 21), LocalDate.of(2024, SEPTEMBER, 21), -2_500_000, 21);
    var donMensuelParents = new FluxArgent("Don mensuel des parents", argentEspeces,
            LocalDate.of(2024, JANUARY, 15), LocalDate.of(9999, 12, 31), 100_000, 15);
    var trainDeVieZety = new FluxArgent("Train de vie de Zety", argentEspeces,
            LocalDate.of(2024, OCTOBER, 1), LocalDate.of(2025, FEBRUARY, 13), -250_000, 1);

    var patrimoineZety = new Patrimoine(
            "PatrimoineZetyAu3Juillet2024",
            zety,
            dateDebut,
            Set.of(ordinateur, vetements, argentEspeces, compteBancaire, fraisScolarite, fraisTenueCompte,
                    paiementFraisScolarite, donMensuelParents, trainDeVieZety));

    var evolutionPatrimoine = new EvolutionPatrimoine(
            "EvolutionPatrimoineZety",
            patrimoineZety,
            dateDebut,
            LocalDate.of(2025, FEBRUARY, 13));

    var evolutionJournaliere = evolutionPatrimoine.getEvolutionJournaliere();

    LocalDate dateFinEspèces = null;
    for (LocalDate date = dateDebut; date.isBefore(LocalDate.of(2025, FEBRUARY, 14)); date = date.plusDays(1)) {
      if (evolutionJournaliere.get(date).getValeurComptable() <= 0) {
        dateFinEspèces = date;
        break;
      }
    }

    assertEquals(LocalDate.of(2024, DECEMBER, 1), dateFinEspèces, "Date incorrecte");
  }

  @Test
  void valeur_patrimoine_14_février_2025() {
    var zety = new Personne("Zety");
    var dateDebut = LocalDate.of(2024, 7, 3);
    LocalDate dateFin = LocalDate.of(2025, 2, 14);

    long joursEntre = ChronoUnit.DAYS.between(dateDebut, dateFin);

    var ordinateur = new Materiel("Ordinateur", dateDebut, 1_200_000, dateDebut, -0.10);
    var vetements = new Materiel("Vêtements", dateDebut, 1_500_000, dateDebut, -0.50);
    var argentEspeces = new Argent("Argent en espèces", dateDebut, 800_000);
    var compteBancaire = new Argent("Compte bancaire", dateDebut, 100_000);

    var fraisScolarite = new FluxArgent("Frais de scolarité", argentEspeces,
            LocalDate.of(2023, 11, 27), LocalDate.of(2024, 8, 27), -200_000, 27);

    var fraisTenueCompte = new FluxArgent("Frais de tenue de compte", compteBancaire,
            dateDebut, LocalDate.of(9999, 12, 31), -20_000, 25);

    var paiementFraisScolarite = new FluxArgent("Paiement frais scolarité 2024-2025", compteBancaire,
            LocalDate.of(2024, 9, 21), LocalDate.of(2024, 9, 21), -2_500_000, 21);

    var donMensuelParents = new FluxArgent("Don mensuel des parents", argentEspeces,
            LocalDate.of(2024, 1, 15), LocalDate.of(9999, 12, 31), 100_000, 15);

    var trainDeVieZety = new FluxArgent("Train de vie de Zety", argentEspeces,
            LocalDate.of(2024, 10, 1), LocalDate.of(2025, 2, 13), -250_000, 1);

    var patrimoineZety = new Patrimoine(
            "PatrimoineZetyAu3Juillet2024",
            zety,
            dateDebut,
            Set.of(ordinateur, vetements, argentEspeces, compteBancaire, fraisScolarite, fraisTenueCompte,
                    paiementFraisScolarite, donMensuelParents, trainDeVieZety));

    var evolutionPatrimoine = new EvolutionPatrimoine(
            "EvolutionPatrimoineZety",
            patrimoineZety,
            dateDebut,
            LocalDate.of(2025, 2, 14));

    var serieValeursComptables = evolutionPatrimoine.serieValeursComptablesPatrimoine();


    assertEquals(800_000, serieValeursComptables.getLast());
    assertEquals(100_000, serieValeursComptables.get(serieValeursComptables.size() - 1));

  }
}