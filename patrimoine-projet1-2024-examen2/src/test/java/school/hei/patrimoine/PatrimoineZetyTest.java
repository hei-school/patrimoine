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
  void testDiminutionPatrimoine() {
    LocalDate dateReference = LocalDate.of(2024, 7, 3);
    LocalDate dateEvaluation = LocalDate.of(2024, 9, 17);
    LocalDate dateEndettement = LocalDate.of(2024, 9, 18);

    Materiel ordinateur = new Materiel("Ordinateur", dateReference, 1200000, dateReference, -0.10);
    Materiel vetements = new Materiel("Vêtements", dateReference, 1500000, dateReference, -0.50);
    Argent argentEspèces = new Argent("Espèces", dateReference, 800000);
    Argent compteBancaire = new Argent("Compte Bancaire", dateReference, 100000);

    Creance creanceClient = new Creance("Crédit Client", dateReference, 500000);
    Dette detteFournisseur = new Dette("Dette Fournisseur", dateReference, -300000);

    // Calcul des valeurs actualisées au 17 septembre 2024
    double valeurOrdinateur = ordinateur.getValeurComptable() * Math.pow(1 - 0.10, getYearsBetween(dateReference, dateEvaluation));
    double valeurVetements = vetements.getValeurComptable() * Math.pow(1 - 0.50, getYearsBetween(dateReference, dateEvaluation));

    // Frais de scolarité
    LocalDate debutScolarite = LocalDate.of(2023, 11, 27);
    LocalDate finScolarite = LocalDate.of(2024, 8, 27);
    long moisScolarite = getMonthsBetween(debutScolarite, finScolarite);
    double totalFraisScolarite = moisScolarite * 200000;

    // Frais de compte bancaire
    LocalDate dateDebutFraisCompte = LocalDate.of(2023, 7, 25);
    long moisCompteBancaire = getMonthsBetween(dateDebutFraisCompte, dateEvaluation);
    double totalFraisCompteBancaire = moisCompteBancaire * 20000;

    // Valeur du patrimoine au 17 septembre 2024
    double valeurPatrimoine17Septembre = valeurOrdinateur + valeurVetements + argentEspèces.getValeurComptable() + compteBancaire.getValeurComptable()
        + creanceClient.getValeurComptable() + detteFournisseur.getValeurComptable()
        - totalFraisScolarite - totalFraisCompteBancaire;

    // Création de la dette
    double montantEmprunt = 10000000;
    double coutPret = 1000000;
    double dette = montantEmprunt + coutPret;

    double valeurPatrimoine18Septembre = valeurPatrimoine17Septembre + montantEmprunt - dette;

    double diminution = valeurPatrimoine17Septembre - valeurPatrimoine18Septembre;

    // Vérification de la diminution
    double diminutionAttendue = 1623536;
    assertEquals(diminutionAttendue, diminution, 0.01);
  }

  private double getYearsBetween(LocalDate start, LocalDate end) {
    return (double) (end.toEpochDay() - start.toEpochDay()) / 365.0;
  }

  private long getMonthsBetween(LocalDate start, LocalDate end) {
    return (long) (end.toEpochDay() - start.toEpochDay()) / 30;
  }

}
