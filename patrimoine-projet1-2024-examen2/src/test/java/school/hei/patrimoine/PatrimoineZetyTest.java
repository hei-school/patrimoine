package school.hei.patrimoine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.Creance;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;

public class PatrimoineZetyTest {

  @Test
  void testValeurPatrimoineZety17_septembre_2024() {
    LocalDate dateReference = LocalDate.of(2024, 7, 3);
    LocalDate dateFuture = LocalDate.of(2024, 9, 17);

    Materiel ordinateur = new Materiel("Ordinateur", dateReference, 1200000, dateReference, -0.10);
    Materiel vetements = new Materiel("Vêtements", dateReference, 1500000, dateReference, -0.50);
    Argent argentEspèces = new Argent("Espèces", dateReference, 800000);

    LocalDate debutScolarite = LocalDate.of(2023, 11, 27);
    LocalDate finScolarite = LocalDate.of(2024, 8, 27);

    LocalDate dateScolarite = debutScolarite;
    while (!dateScolarite.isAfter(finScolarite)) {
      FluxArgent fraisScolarite = new FluxArgent("Frais Scolarité", argentEspèces, dateScolarite, dateScolarite, -200000, 27);
      argentEspèces.addFinancés(fraisScolarite);
      dateScolarite = dateScolarite.plusMonths(1);
    }

    Argent compteBancaire = new Argent("Compte Bancaire", dateReference, 100000);
    FluxArgent fraisCompteBancaire = new FluxArgent("Frais Compte Bancaire", compteBancaire, dateReference, dateFuture, -20000, 25);

    compteBancaire.addFinancés(fraisCompteBancaire);

    Set<Possession> possessions = new HashSet<>();
    possessions.add(ordinateur);
    possessions.add(vetements);
    possessions.add(argentEspèces);
    possessions.add(compteBancaire);

    Patrimoine patrimoine = new Patrimoine("Patrimoine de Zety", null, dateReference, possessions);

    Patrimoine patrimoineFutur = patrimoine.projectionFuture(dateFuture);

    int valeurAttendue = 0;
    valeurAttendue += patrimoineFutur.possessionParNom("Ordinateur").valeurComptableFuture(dateFuture);
    valeurAttendue += patrimoineFutur.possessionParNom("Vêtements").valeurComptableFuture(dateFuture);
    valeurAttendue += patrimoineFutur.possessionParNom("Espèces").valeurComptableFuture(dateFuture);
    valeurAttendue += patrimoineFutur.possessionParNom("Compte Bancaire").valeurComptableFuture(dateFuture);

    assertEquals(valeurAttendue, patrimoineFutur.getValeurComptable());
  }


  @Test
  void testDiminutionPatrimoineZetyEntre_17_18_spt_2024() {
    LocalDate dateReference = LocalDate.of(2024, 7, 3);
    LocalDate dateEvaluation = LocalDate.of(2024, 9, 17);
    LocalDate dateEndettement = LocalDate.of(2024, 9, 18);

    Materiel ordinateur = new Materiel("Ordinateur", dateReference, 1200000, dateReference, -0.10);
    Materiel vetements = new Materiel("Vêtements", dateReference, 1500000, dateReference, -0.50);
    Argent argentEspèces = new Argent("Espèces", dateReference, 800000);
    Argent compteBancaire = new Argent("Compte Bancaire", dateReference, 100000);

    Creance creanceClient = new Creance("Crédit Client", dateReference, 500000);
    Dette detteFournisseur = new Dette("Dette Fournisseur", dateReference, -300000);

    double valeurOrdinateur = ordinateur.getValeurComptable() * Math.pow(1 - 0.10, getYearsBetween(dateReference, dateEvaluation));
    double valeurVetements = vetements.getValeurComptable() * Math.pow(1 - 0.50, getYearsBetween(dateReference, dateEvaluation));

    LocalDate debutScolarite = LocalDate.of(2023, 11, 27);
    LocalDate finScolarite = LocalDate.of(2024, 8, 27);
    long moisScolarite = getMonthsBetween(debutScolarite, finScolarite);
    double totalFraisScolarite = moisScolarite * 200000;

    LocalDate dateDebutFraisCompte = LocalDate.of(2023, 7, 25);
    long moisCompteBancaire = getMonthsBetween(dateDebutFraisCompte, dateEvaluation);
    double totalFraisCompteBancaire = moisCompteBancaire * 20000;

    double valeurPatrimoine17Septembre = valeurOrdinateur + valeurVetements + argentEspèces.getValeurComptable() + compteBancaire.getValeurComptable()
        + creanceClient.getValeurComptable() + detteFournisseur.getValeurComptable()
        - totalFraisScolarite - totalFraisCompteBancaire;

    double montantEmprunt = 10000000;
    double coutPret = 1000000;
    double dette = montantEmprunt + coutPret;

    double valeurPatrimoine18Septembre = valeurPatrimoine17Septembre - dette;

    double diminution = valeurPatrimoine17Septembre - valeurPatrimoine18Septembre;

    double diminutionAttendue = 1000000;
    assertEquals(diminutionAttendue, diminution, 0.01);
  }

  private double getYearsBetween(LocalDate start, LocalDate end) {
    return (double) (end.toEpochDay() - start.toEpochDay()) / 365.0;
  }

  private long getMonthsBetween(LocalDate start, LocalDate end) {
    return (long) (end.toEpochDay() - start.toEpochDay()) / 30;
  }


  @Test
  void testDateZetyPlusDEspeces() {

    LocalDate dateReference = LocalDate.of(2024, 7, 3);
    LocalDate dateEvaluation = LocalDate.of(2024, 9, 17);
    LocalDate dateEndettement = LocalDate.of(2024, 9, 18);
    LocalDate datePaiementScolarite = LocalDate.of(2024, 9, 21);
    LocalDate dateDebutTransferts = LocalDate.of(2024, 1, 15);
    LocalDate dateDebutDepenses = LocalDate.of(2024, 10, 1);

    // Création des possessions initiales
    Materiel ordinateur = new Materiel("Ordinateur", dateReference, 1200000, dateReference, -0.10);
    Materiel vetements = new Materiel("Vêtements", dateReference, 1500000, dateReference, -0.50);
    Argent argentEspeces = new Argent("Espèces", dateReference, 800000);
    Argent compteBancaire = new Argent("Compte Bancaire", dateReference, 100000);

    Dette emprunt = new Dette("Emprunt", dateEndettement, -10000000);
    Argent argentEmprunt = new Argent("Argent emprunté", dateEndettement, 10000000);
    compteBancaire = new Argent("Compte Bancaire", dateEndettement, compteBancaire.getValeurComptable() + argentEmprunt.getValeurComptable());

    // Paiement des frais de scolarité depuis le compte bancaire le 21 septembre 2024
    compteBancaire = new Argent("Compte Bancaire", datePaiementScolarite, compteBancaire.getValeurComptable() - 2500000);

    LocalDate currentDate = dateDebutTransferts;
    while (!currentDate.isAfter(dateEvaluation)) {
      argentEspeces = new Argent("Espèces", currentDate, argentEspeces.getValeurComptable() + 100000);
      currentDate = currentDate.plusMonths(1);
    }

    currentDate = dateDebutDepenses;
    LocalDate dateSansEspeces = null;
    while (argentEspeces.getValeurComptable() > 0) {
      argentEspeces = new Argent("Espèces", currentDate, argentEspeces.getValeurComptable() - 250000);
      if (argentEspeces.getValeurComptable() <= 0) {
        dateSansEspeces = currentDate;
        break;
      }
      currentDate = currentDate.plusMonths(1);
    }

    LocalDate dateAttendue = LocalDate.of(2025, 4, 1);
    assertEquals(dateAttendue, dateSansEspeces);
  }
}
