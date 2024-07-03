package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.GroupePossession;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.possession.TransfertArgent;
import school.hei.patrimoine.modele.possession.Dette;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static java.time.Month.MAY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PatrimoineTest {

  @Test
  void patrimoine_vide_vaut_0() {
    var ilo = new Personne("Ilo");

    var patrimoineIloAu13mai24 = new Patrimoine(
        "patrimoineIloAu13mai24",
        ilo,
        LocalDate.of(2024, MAY, 13),
        Set.of());

    assertEquals(0, patrimoineIloAu13mai24.getValeurComptable());
  }

  @Test
  void patrimoine_a_de_l_argent() {
    var ilo = new Personne("Ilo");

    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var patrimoineIloAu13mai24 = new Patrimoine(
        "patrimoineIloAu13mai24",
        ilo,
        au13mai24,
        Set.of(
            new Argent("Espèces", au13mai24, 400_000),
            new Argent("Compte epargne", au13mai24, 200_000),
            new Argent("Compte courant", au13mai24, 600_000)));

    assertEquals(1_200_000, patrimoineIloAu13mai24.getValeurComptable());
  }

  @Test
  void patrimoine_possede_un_train_de_vie_financé_par_argent() {
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

    assertEquals(500_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(10)).getValeurComptable());
    assertEquals(200_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(100)).getValeurComptable());
    assertEquals(200_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(1_000)).getValeurComptable());
  }

  @Test
  void patrimoine_possede_groupe_de_train_de_vie_et_d_argent() {
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
        Set.of(new GroupePossession("Le groupe", au13mai24, Set.of(financeur, trainDeVie))));

    assertEquals(500_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(10)).getValeurComptable());
    assertEquals(200_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(100)).getValeurComptable());
    assertEquals(200_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(1_000)).getValeurComptable());
  }

  @Test
  void testPatrimoineZety() {
      LocalDate dateInitiale = LocalDate.of(2024, 7, 3);
      LocalDate dateFuture = LocalDate.of(2024, 9, 17);

      Materiel ordinateur = new Materiel("Ordinateur", dateInitiale, 1200000, dateInitiale, -0.10);
      Materiel vetements = new Materiel("Vêtements", dateInitiale, 1500000, dateInitiale, -0.50);
      Argent especes = new Argent("Espèces", dateInitiale, 800000);
      Argent compteBancaire = new Argent("Compte bancaire", dateInitiale, 100000);

      LocalDate debutScolarite = LocalDate.of(2023, 11, 27);
      LocalDate finScolarite = LocalDate.of(2024, 8, 27);
      FluxArgent fraisScolarite = new FluxArgent("Frais de scolarité", especes, debutScolarite, finScolarite, -200000, 27);

      FluxArgent fraisBancaires = new FluxArgent("Frais bancaires", compteBancaire, dateInitiale, LocalDate.of(9999, 12, 31), -20000, 25);

      Set<Possession> possessions = new HashSet<>();
      possessions.add(ordinateur);
      possessions.add(vetements);
      possessions.add(especes);
      possessions.add(compteBancaire);
      possessions.add(fraisScolarite);
      possessions.add(fraisBancaires);

      Personne zety = new Personne("Zety");
      Patrimoine patrimoineZety = new Patrimoine("Patrimoine de Zety", zety, dateInitiale, possessions);

      Patrimoine patrimoineFutur = patrimoineZety.projectionFuture(dateFuture);

      int valeurTotale = patrimoineFutur.getValeurComptable();
    int valeurAttendue = 2978848;
    int marge = 100;
    System.out.println("Test du patrimoine de Zety:");
    System.out.println("Date initiale: " + dateInitiale);
    System.out.println("Date future: " + dateFuture);
    System.out.println("Valeur totale calculée: " + valeurTotale + " Ar");
    System.out.println("Valeur attendue: " + valeurAttendue + " Ar");
    System.out.println("Marge d'erreur: ±" + marge + " Ar");

    try {
        assertEquals(valeurAttendue, valeurTotale, marge,
                () -> String.format("La valeur du patrimoine de Zety au 17 septembre 2024 devrait être proche de %d Ar (±%d Ar). Valeur obtenue : %d Ar", valeurAttendue, marge, valeurTotale));
        System.out.println("Test RÉUSSI: La valeur calculée est dans la marge d'erreur acceptable.");
    } catch (AssertionError e) {
        System.out.println("Test ÉCHOUÉ: " + e.getMessage());
        throw e;
    }
  }

  @Test
  void testDiminutionPatrimoineZetyApresEmprunt() {
    LocalDate dateInitiale = LocalDate.of(2024, 9, 17);
    LocalDate dateEmprunt = LocalDate.of(2024, 9, 18);

    Materiel ordinateur = new Materiel("Ordinateur", dateInitiale, 1200000, dateInitiale, -0.10);
    Materiel vetements = new Materiel("Vêtements", dateInitiale, 1500000, dateInitiale, -0.50);
    Argent especes = new Argent("Espèces", dateInitiale, 800000);
    Argent compteBancaire = new Argent("Compte bancaire", dateInitiale, 100000);

    LocalDate debutScolarite = LocalDate.of(2023, 11, 27);
    LocalDate finScolarite = LocalDate.of(2024, 8, 27);
    FluxArgent fraisScolarite = new FluxArgent("Frais de scolarité", especes, debutScolarite, finScolarite, -200000, 27);

    FluxArgent fraisBancaires = new FluxArgent("Frais bancaires", compteBancaire, dateInitiale, LocalDate.of(9999, 12, 31), -20000, 25);

    Set<Possession> possessionsInitiales = new HashSet<>();
    possessionsInitiales.add(ordinateur);
    possessionsInitiales.add(vetements);
    possessionsInitiales.add(especes);
    possessionsInitiales.add(compteBancaire);
    possessionsInitiales.add(fraisScolarite);
    possessionsInitiales.add(fraisBancaires);

    Personne zety = new Personne("Zety");
    Patrimoine patrimoineInitial = new Patrimoine("Patrimoine de Zety", zety, dateInitiale, possessionsInitiales);

    int valeurPatrimoineInitial = patrimoineInitial.getValeurComptable();

    Set<Possession> possessionsApresEmprunt = new HashSet<>(possessionsInitiales);

    FluxArgent emprunt = new FluxArgent("Emprunt bancaire", compteBancaire, dateEmprunt, dateEmprunt, 10000000, 18);
    possessionsApresEmprunt.add(emprunt);

    Dette detteBancaire = new Dette("Dette bancaire", dateEmprunt, -11000000);
    possessionsApresEmprunt.add(detteBancaire);

    Patrimoine patrimoineApresEmprunt = new Patrimoine("Patrimoine de Zety après emprunt", zety, dateEmprunt, possessionsApresEmprunt);

    int valeurPatrimoineApresEmprunt = patrimoineApresEmprunt.getValeurComptable();

    int diminutionPatrimoine = valeurPatrimoineInitial - valeurPatrimoineApresEmprunt;

    System.out.println("Valeur du patrimoine au 17 septembre 2024 : " + valeurPatrimoineInitial + " Ar");
    System.out.println("Valeur du patrimoine au 18 septembre 2024 : " + valeurPatrimoineApresEmprunt + " Ar");
    System.out.println("Diminution du patrimoine : " + diminutionPatrimoine + " Ar");

    int diminutionAttendue = 11000000;

    assertEquals(diminutionAttendue, diminutionPatrimoine, 100,"La diminution du patrimoine de Zety devrait être d'environ 11 000 000 Ar");
  }

  @Test
  public void testDateSansEspeces() {
    LocalDate dateDebut = LocalDate.of(2024, 1, 1);
    LocalDate dateFin = LocalDate.of(2025, 12, 31);

    Argent compteBancaire = new Argent("Compte bancaire", dateDebut, 3600000);
    Argent especes = new Argent("Espèces", dateDebut, 0);

    LocalDate dateEmprunt = LocalDate.of(2024, 9, 18);
    FluxArgent emprunt = new FluxArgent("Emprunt", compteBancaire, dateEmprunt, dateEmprunt, 10000000, dateEmprunt.getDayOfMonth());

    LocalDate datePaiementScolarite = LocalDate.of(2024, 9, 21);
    FluxArgent paiementScolarite = new FluxArgent("Frais de scolarité", compteBancaire, datePaiementScolarite, datePaiementScolarite, -2500000, datePaiementScolarite.getDayOfMonth());

    TransfertArgent donParents = new TransfertArgent("Don parents", compteBancaire, especes, dateDebut, dateFin, 100000, 15);

    LocalDate dateDebutDepenses = LocalDate.of(2024, 1, 1);  // Commence dès le début
    LocalDate dateFinDepenses = LocalDate.of(2025, 12, 31);  // Jusqu'à la fin
    FluxArgent depensesMensuelles = new FluxArgent("Dépenses mensuelles", especes, dateDebutDepenses, dateFinDepenses, -300000, 1);  // Augmentation du montant

    GroupePossession patrimoine = new GroupePossession("Patrimoine Zety", dateDebut, Set.of(compteBancaire, especes, emprunt, paiementScolarite, donParents, depensesMensuelles));

    LocalDate dateSansEspeces = null;
    for (LocalDate date = dateDebut; date.isBefore(dateFin) || date.isEqual(dateFin); date = date.plusDays(1)) {
        Possession patrimoineProjection = patrimoine.projectionFuture(date);
        if (patrimoineProjection instanceof GroupePossession) {
            GroupePossession groupeProjection = (GroupePossession) patrimoineProjection;
            Argent especesProjection = (Argent) groupeProjection.getPossessions().stream()
                    .filter(p -> p.getNom().equals("Espèces"))
                    .findFirst()
                    .orElseThrow();

            System.out.println("Date: " + date + ", Solde espèces: " + especesProjection.getValeurComptable());

            if (especesProjection.getValeurComptable() < 0) {
                dateSansEspeces = date;
                break;
            }
        }
    }

    assertNotNull(dateSansEspeces, "Zety devrait se retrouver sans espèces à un moment donné");
    System.out.println("Zety n'a plus d'espèces à partir du : " + dateSansEspeces);
  }

  @Test
  public void testPatrimoineAvantDepart() {
    LocalDate dateDebut = LocalDate.of(2024, 1, 1);
    LocalDate dateDepart = LocalDate.of(2025, 2, 14);

    Argent compteBancaire = new Argent("Compte bancaire", dateDebut, 3600000);
    Argent especes = new Argent("Espèces", dateDebut, 0);

    LocalDate datePaiementScolarite = LocalDate.of(2024, 9, 21);
    FluxArgent paiementScolarite = new FluxArgent("Frais de scolarité", compteBancaire, datePaiementScolarite, datePaiementScolarite, -2500000, datePaiementScolarite.getDayOfMonth());

    TransfertArgent donParents = new TransfertArgent("Don parents", compteBancaire, especes, dateDebut, dateDepart, 100000, 15);

    LocalDate dateDebutDepenses = LocalDate.of(2024, 10, 1);
    LocalDate dateFinDepenses = LocalDate.of(2025, 2, 13);
    FluxArgent depensesMensuelles = new FluxArgent("Dépenses mensuelles", especes, dateDebutDepenses, dateFinDepenses, -250000, 1);

    GroupePossession patrimoine = new GroupePossession("Patrimoine Zety", dateDebut, Set.of(compteBancaire, especes, paiementScolarite, donParents, depensesMensuelles));

    Possession patrimoineProjection = patrimoine.projectionFuture(dateDepart);

    assertTrue(patrimoineProjection instanceof GroupePossession, "La projection devrait être un GroupePossession");
    GroupePossession groupeProjection = (GroupePossession) patrimoineProjection;

    double valeurTotale = groupeProjection.getPossessions().stream()
            .mapToDouble(Possession::getValeurComptable)
            .sum();

    System.out.println("Valeur du patrimoine de Zety le " + dateDepart + " : " + valeurTotale + " Ar");
  }

    @Test
    void testValeurPatrimoineZetyEnEuros() {
        BigDecimal valeurPatrimoineAR = BigDecimal.valueOf(-7000);

        LocalDate dateInitiale = LocalDate.of(2024, 7, 3);
        LocalDate dateEvaluation = LocalDate.of(2025, 10, 26);
        double tauxInitial = 1.0 / 4821;
        double tauxAppreciationAnnuel = -0.10;

        long joursDifference = dateEvaluation.toEpochDay() - dateInitiale.toEpochDay();

        double tauxAppreciationJournalier = Math.pow(1 + tauxAppreciationAnnuel, 1.0 / 365) - 1;
        double tauxChange = tauxInitial * Math.pow(1 + tauxAppreciationJournalier, joursDifference);

        BigDecimal valeurEUR = valeurPatrimoineAR.multiply(BigDecimal.valueOf(tauxChange)).setScale(2);

        BigDecimal expectedValueEUR = BigDecimal.valueOf(-1452.81).setScale(2);
        assertEquals(expectedValueEUR, valeurEUR);

        System.out.println("Valeur du patrimoine de Zety au " + dateEvaluation + " : " + valeurEUR + " EUR");
    }
}