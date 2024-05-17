package school.hei.patrimoine;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.possession.Argent;
import school.hei.patrimoine.possession.Materiel;
import school.hei.patrimoine.possession.TrainDeVie;

import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PatrimoineTest {

  @Test
  void patrimoine_vide_vaut_0() {
    var ilo = new Personne("Ilo");

    var patrimoineIloAu13mai24 = new Patrimoine(
        ilo,
        Instant.parse("2024-05-13T00:00:00.00Z"),
        Set.of());

    assertEquals(0, patrimoineIloAu13mai24.getValeurComptable());
  }

  @Test
  void patrimoine_a_de_l_argent() {
    var ilo = new Personne("Ilo");

    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var patrimoineIloAu13mai24 = new Patrimoine(
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

    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var financeur = new Argent("Espèces", au13mai24, 400_000);

    var trainDeVie = new TrainDeVie(null, 0, null, null, financeur, 0);

    var patrimoineIloAu13mai24 = new Patrimoine(
            ilo,
            au13mai24,
            Set.of(financeur, trainDeVie));
    assertEquals(400_000, patrimoineIloAu13mai24.getValeurComptable());
  }

  @Test
  void combien_vaut_le_patrimoine_de_Ilo_le_26_juin_2024() {
    Personne ilo = new Personne("Ilo");
    Instant au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    Argent espece = new Argent("Espèces", au13mai24, 400_000);
    Argent compteEpargne = new Argent("Compte Epargne", au13mai24, 200_000);
    Argent compteCourant = new Argent("Compte Courant", au13mai24, 600_000);
    Instant debutTrainDeVie = Instant.parse("2024-01-01T00:00:00.00Z");
    Instant finTrainDeVie = Instant.parse("2024-12-31T00:00:00.00Z");
    TrainDeVie trainDeVie = new TrainDeVie("Train de Vie", 500_000, debutTrainDeVie, finTrainDeVie, compteCourant, 1);
    Instant dateAchatOrdinateur = Instant.parse("2021-10-26T00:00:00.00Z");
    Materiel ordinateur = new Materiel("Ordinateur", dateAchatOrdinateur, 2_000_000, -0.10);
    Instant dateEvaluationVetements = Instant.parse("2024-01-01T00:00:00.00Z");
    Materiel vetements = new Materiel("Vetements", dateEvaluationVetements, 1_000_000, -0.20);
    Patrimoine patrimoineIloAu13mai24 = new Patrimoine(ilo, au13mai24, Set.of(espece, compteEpargne, compteCourant, trainDeVie, ordinateur, vetements));
    Instant au26juin24 = Instant.parse("2024-06-26T00:00:00.00Z");
    Patrimoine projectionPatrimoine = patrimoineIloAu13mai24.projectionFuture(au26juin24);
    assertEquals(3400000, projectionPatrimoine.getValeurComptable());
  }

  @Test
  void combien_vaut_le_patrimoine_de_Ilo_le_14_juillet_2024() {
    Personne ilo = new Personne("Ilo");

    Instant date14juillet24 = Instant.parse("2024-07-14T00:00:00.00Z");

    Argent espece = new Argent("Espèces", date14juillet24, 400_000);
    Argent compteEpargne = new Argent("Compte Epargne", date14juillet24, 200_000);
    Argent compteCourant = new Argent("Compte Courant", date14juillet24, 600_000);

    Instant debutTrainDeVie = Instant.parse("2024-01-01T00:00:00.00Z");
    Instant finTrainDeVie = Instant.parse("2024-12-31T00:00:00.00Z");
    TrainDeVie trainDeVie = new TrainDeVie("Train de Vie", 500_000, debutTrainDeVie, finTrainDeVie, compteCourant, 1);

    Instant dateAchatOrdinateur = Instant.parse("2021-10-26T00:00:00.00Z");
    Materiel ordinateur = new Materiel("Ordinateur", dateAchatOrdinateur, 2_000_000, -0.10);

    Instant dateEvaluationVetements = Instant.parse("2024-01-01T00:00:00.00Z");
    Materiel vetements = new Materiel("Vetements", dateEvaluationVetements, 1_000_000, -0.20);

    Patrimoine patrimoineIloAu14juillet24 = new Patrimoine(
            ilo,
            date14juillet24,
            Set.of(espece, compteEpargne, compteCourant, trainDeVie, ordinateur, vetements)
    );

    int valeurPatrimoine = patrimoineIloAu14juillet24.getValeurComptable();
    assertEquals(valeurPatrimoine, patrimoineIloAu14juillet24.getValeurComptable());

    int montantCompteCourant = patrimoineIloAu14juillet24.getMontantCompteCourant();
    assertEquals(montantCompteCourant, compteCourant.getValeurComptable());
  }
}