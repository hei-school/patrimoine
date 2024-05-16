package school.hei.patrimoine;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.possession.Argent;
import school.hei.patrimoine.possession.Materiel;
import school.hei.patrimoine.possession.Possession;
import school.hei.patrimoine.possession.TrainDeVie;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
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
  void testValeurComptableFutureMateriel() {
    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var au13mai29 = Instant.parse("2029-05-13T00:00:00.00Z");

    var materiel = new Materiel("ordinateur", au13mai24, 400_000, 0.1);

    long joursEntre = ChronoUnit.DAYS.between(au13mai24, au13mai29);
    double tauxAmortissementQuotidien = Math.pow(1 - 0.1, 1.0 / 365);
    double valeurAttendueMateriel = Math.round(400_000 * Math.pow(tauxAmortissementQuotidien, joursEntre));

    double valeurComptableFutureMateriel = materiel.valeurComptableFuture(au13mai29);
    assertEquals(valeurAttendueMateriel, valeurComptableFutureMateriel, 0.01);
  }

  // Vérifie que la valeur comptable future avant le début reste égale à la valeur comptable initiale
  @Test
  void valeurComptableFuture_AvantDebut_RetourneValeurInitiale() {
    Instant debut = Instant.parse("2024-01-01T00:00:00Z");
    TrainDeVie trainDeVie = new TrainDeVie("Test", 1000, debut,
            Instant.parse("2024-12-31T23:59:59Z"), new Argent("Finance", Instant.now(), 12000), 5);

    int valeurComptable = trainDeVie.valeurComptableFuture(debut.minusSeconds(1));

    assertEquals(trainDeVie.getValeurComptable(), valeurComptable);
  }

  // Vérifie que la valeur comptable future après le début retourne la valeur correcte après un certain délai
  @Test
  void valeurComptableFuture_ApresDebut_RetourneValeurCorrecte() {
    LocalDate debut = Instant.parse("2024-01-01T00:00:00Z").atZone(ZoneOffset.UTC).toLocalDate();
    TrainDeVie trainDeVie = new TrainDeVie("Test", 1000,
            Instant.parse("2024-01-01T00:00:00Z"),
            Instant.parse("2024-12-31T23:59:59Z"),
            new Argent("Finance", Instant.now(), 12000),
            5);

    // Ajoute 3 mois à la date de début pour calculer la valeur comptable future
    LocalDate futureDate = debut.plus(3, ChronoUnit.MONTHS);
    int valeurComptable = trainDeVie.valeurComptableFuture(futureDate.atStartOfDay(ZoneOffset.UTC).toInstant());

    assertEquals(9000, valeurComptable);
  }

  // Vérifie que la projection future retourne une nouvelle instance de TrainDeVie avec les mêmes attributs
  @Test
  void projectionFuture_RetourneNouvelleInstanceTrainDeVie() {
    Instant debut = Instant.parse("2024-01-01T00:00:00Z");
    TrainDeVie trainDeVie = new TrainDeVie("Test", 1000, debut,
            Instant.parse("2024-12-31T23:59:59Z"), new Argent("Finance", Instant.now(), 12000), 5);

    Instant futureDate = debut.plus(6, ChronoUnit.MONTHS);
    Possession futureProjection = trainDeVie.projectionFuture(futureDate);

    assertEquals(trainDeVie.getNom(), futureProjection.getNom());
    assertEquals(trainDeVie.getDepensesMensuelle(), ((TrainDeVie) futureProjection).getDepensesMensuelle());
    assertEquals(trainDeVie.getDebut(), ((TrainDeVie) futureProjection).getDebut());
    assertEquals(trainDeVie.getFin(), ((TrainDeVie) futureProjection).getFin());
    assertEquals(trainDeVie.getFinancePar(), ((TrainDeVie) futureProjection).getFinancePar());
    assertEquals(trainDeVie.getDateDePonction(), ((TrainDeVie) futureProjection).getDateDePonction());
  }
}