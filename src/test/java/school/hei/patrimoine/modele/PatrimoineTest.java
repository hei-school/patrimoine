package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.GroupePossession;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.possession.TransfertArgent;
import school.hei.patrimoine.modele.possession.Dette;

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

}