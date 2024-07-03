package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.MAY;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
  void patrimoine_de_Zety() {
    LocalDate au03juillet24 = LocalDate.of(2024, 7, 3);
    LocalDate dateEvaluation = LocalDate.of(2024, 9, 17);

    // Création des possessions
    Materiel ordinateur = new Materiel("Ordinateur", au03juillet24, 1_200_000, au03juillet24, -0.10);
    Materiel vetements = new Materiel("Vêtements", au03juillet24, 1_500_000, au03juillet24, -0.50);
    Argent argentEspeces = new Argent("Argent en espèces", au03juillet24, 800_000);
    Argent compteBancaire = new Argent("Compte bancaire", au03juillet24, 100_000);
    FluxArgent fraisScolarite = new FluxArgent("Frais de scolarité", argentEspeces, LocalDate.of(2023, 11, 27), LocalDate.of(2024, 8, 27), -200_000, 27);
    FluxArgent fraisCompte = new FluxArgent("Frais de compte", compteBancaire, LocalDate.of(2024, 7, 25), LocalDate.MAX, -20_000, 25);

    // valeur futurde l'ordinateur est egale à 1175013
    //valeur futur des vetement est egale à 1343835
    //l'argent restant apres les paiment de frais de scolarite est egale à 400000
    //l'argent restant apres les paiments de frais de compte est egale à 60000

    //donc 1175013 + 1343835 + 400000 + 60000 = 2978848



    // Création du patrimoine
    Set<Possession> possessionsDeZety = Set.of(ordinateur, vetements, argentEspeces, compteBancaire, fraisScolarite, fraisCompte);
    Personne possesseur = new Personne("Zety");
    Patrimoine patrimoineDeZety = new Patrimoine("Patrimoine de Zety", possesseur, au03juillet24, possessionsDeZety);

    // Projection du patrimoine au 17 septembre 2024
    Patrimoine patrimoineFutureDeZety = patrimoineDeZety.projectionFuture(dateEvaluation);

    // Assertion
    assertEquals(2978848, patrimoineFutureDeZety.getValeurComptable());
  }

  @Test
  void patrimoine_diminue_apres_emprunt() {
    LocalDate au03juillet24 = LocalDate.of(2024, 7, 3);
    LocalDate dateEvaluation = LocalDate.of(2024, 9, 17);
    LocalDate dateEmprunt = LocalDate.of(2024, 9, 18);

    Materiel ordinateur = new Materiel("Ordinateur", au03juillet24, 1_200_000, au03juillet24, -0.10);
    Materiel vetements = new Materiel("Vêtements", au03juillet24, 1_500_000, au03juillet24, -0.50);
    Argent argentEspeces = new Argent("Argent en espèces", au03juillet24, 800_000);
    Argent compteBancaire = new Argent("Compte bancaire", au03juillet24, 100_000);
    FluxArgent fraisScolarite = new FluxArgent("Frais de scolarité", argentEspeces, LocalDate.of(2023, 11, 27), LocalDate.of(2024, 8, 27), -200_000, 27);
    FluxArgent fraisCompte = new FluxArgent("Frais de compte", compteBancaire, LocalDate.of(2024, 7, 25), LocalDate.MAX, -20_000, 25);

    Set<Possession> possessions = Set.of(ordinateur, vetements, argentEspeces, compteBancaire, fraisScolarite, fraisCompte);
    Personne possesseur = new Personne("Zety");
    Patrimoine patrimoineDeZety = new Patrimoine("Patrimoine de Zety", possesseur, au03juillet24, possessions);

    Patrimoine patrimoineFutureDeZetyAu17Septembre = patrimoineDeZety.projectionFuture(dateEvaluation);

    Argent argentApresEmprunt = new Argent("Compte bancaire après emprunt", dateEmprunt, patrimoineFutureDeZetyAu17Septembre.possessionParNom("Compte bancaire").getValeurComptable() + 10_000_000);
    Dette dette = new Dette("Dette envers la banque", dateEmprunt, -11_000_000);

    Set<Possession> possessionsApresEmprunt = Set.of(
            patrimoineFutureDeZetyAu17Septembre.possessionParNom("Ordinateur"),
            patrimoineFutureDeZetyAu17Septembre.possessionParNom("Vêtements"),
            patrimoineFutureDeZetyAu17Septembre.possessionParNom("Argent en espèces"),
            argentApresEmprunt,
            fraisScolarite.projectionFuture(dateEmprunt),
            fraisCompte.projectionFuture(dateEmprunt),
            dette
    );

    Patrimoine patrimoineDeZetyApresEmprunt = new Patrimoine("Patrimoine de Zety après emprunt", possesseur, dateEmprunt, possessionsApresEmprunt);

    int valeurPatrimoineAu17Septembre = patrimoineFutureDeZetyAu17Septembre.getValeurComptable();
    int valeurPatrimoineApresEmprunt = patrimoineDeZetyApresEmprunt.getValeurComptable();
    int diminutionPatrimoine = valeurPatrimoineAu17Septembre - valeurPatrimoineApresEmprunt;

    assertEquals(1_000_000, diminutionPatrimoine);
  }

  @Test
  void zety_n_a_plus_d_especes_a_partir_de() {
    LocalDate au03juillet24 = LocalDate.of(2024, 7, 3);
    LocalDate dateEvaluation = LocalDate.of(2024, 9, 17);
    LocalDate dateEmprunt = LocalDate.of(2024, 9, 18);

    Materiel ordinateur = new Materiel("Ordinateur", au03juillet24, 1_200_000, au03juillet24, -0.10);
    Materiel vetements = new Materiel("Vêtements", au03juillet24, 1_500_000, au03juillet24, -0.50);
    Argent argentEspeces = new Argent("Argent en espèces", au03juillet24, 800_000);
    Argent compteBancaire = new Argent("Compte bancaire", au03juillet24, 100_000);
    FluxArgent fraisScolarite = new FluxArgent("Frais de scolarité", argentEspeces, LocalDate.of(2023, 11, 27), LocalDate.of(2024, 8, 27), -200_000, 27);
    FluxArgent fraisCompte = new FluxArgent("Frais de compte", compteBancaire, LocalDate.of(2024, 7, 25), LocalDate.MAX, -20_000, 25);

    Set<Possession> possessions = Set.of(ordinateur, vetements, argentEspeces, compteBancaire, fraisScolarite, fraisCompte);
    Personne possesseur = new Personne("Zety");
    Patrimoine patrimoineDeZety = new Patrimoine("Patrimoine de Zety", possesseur, au03juillet24, possessions);

    FluxArgent fraisScolarite2425 = new FluxArgent("Frais de scolarité 2024-2025", compteBancaire, LocalDate.of(2024, 9, 21), LocalDate.of(2024, 9, 21), -2_500_000, 21);
    FluxArgent donParents = new FluxArgent("Don des parents", argentEspeces, LocalDate.of(2024, 1, 15), LocalDate.MAX, 100_000, 15);
    FluxArgent trainDeVie = new FluxArgent("Train de vie", argentEspeces, LocalDate.of(2024, 10, 1), LocalDate.of(2025, 2, 13), -250_000, 1);

    Set<Possession> possessionsAvecNouvellesTransactions = Set.of(
            ordinateur, vetements, argentEspeces, compteBancaire, fraisScolarite, fraisCompte, fraisScolarite2425, donParents, trainDeVie
    );

    Patrimoine patrimoineDeZetyAvecNouvellesTransactions = new Patrimoine("Patrimoine de Zety avec nouvelles transactions", possesseur, au03juillet24, possessionsAvecNouvellesTransactions);

    LocalDate dateTest = LocalDate.of(2024, 10, 1);
    while (patrimoineDeZetyAvecNouvellesTransactions.projectionFuture(dateTest).possessionParNom("Argent en espèces").getValeurComptable() > 0) {
      dateTest = dateTest.plusDays(1);
    }

    assertEquals(LocalDate.of(2024, 11, 15), dateTest.minusDays(1));
  }
  @Test
  void testPatrimoineAu14Fevrier2025() {
    LocalDate au03juillet24 = LocalDate.of(2024, 7, 3);
    LocalDate dateEvaluation = LocalDate.of(2025, 2, 14);

    Materiel ordinateur = new Materiel("Ordinateur", au03juillet24, 1_200_000, au03juillet24, -0.10);
    Materiel vetements = new Materiel("Vêtements", au03juillet24, 1_500_000, au03juillet24, -0.50);
    Argent argentEspeces = new Argent("Argent en espèces", au03juillet24, 800_000);
    Argent compteBancaire = new Argent("Compte bancaire", au03juillet24, 100_000);
    FluxArgent fraisScolarite = new FluxArgent("Frais de scolarité", argentEspeces, LocalDate.of(2023, 11, 27), LocalDate.of(2024, 8, 27), -200_000, 27);
    FluxArgent fraisCompte = new FluxArgent("Frais de compte", compteBancaire, LocalDate.of(2024, 7, 25), LocalDate.MAX, -20_000, 25);

    Set<Possession> possessions = Set.of(ordinateur, vetements, argentEspeces, compteBancaire, fraisScolarite, fraisCompte);
    Personne possesseur = new Personne("Zety");
    Patrimoine patrimoineDeZety = new Patrimoine("Patrimoine de Zety", possesseur, au03juillet24, possessions);

    Patrimoine patrimoineFutureDeZety = patrimoineDeZety.projectionFuture(dateEvaluation);

    double valeurComptable = patrimoineFutureDeZety.getValeurComptable();

    assertEquals(2521314.0, valeurComptable);
  }


}