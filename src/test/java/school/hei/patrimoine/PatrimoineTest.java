package school.hei.patrimoine;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.possession.Argent;
import school.hei.patrimoine.possession.Materiel;
import school.hei.patrimoine.possession.Possession;
import school.hei.patrimoine.possession.TrainDeVie;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PatrimoineTest {

  @Test
  void patrimoine_vide_vaut_0() {
    var ilo = new Personne("Ilo", List.of());

    var patrimoineIloAu13mai24 = new Patrimoine(
        ilo,
        Instant.parse("2024-05-13T00:00:00.00Z"),
        Set.of());

    assertEquals(0, patrimoineIloAu13mai24.getValeurComptable());
  }

  @Test
  void patrimoine_a_de_l_argent() {
    var ilo = new Personne("Ilo", List.of());

    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var patrimoineIloAu13mai24 = new Patrimoine(
        ilo,
        au13mai24,
        Set.of(
            new Argent("Espèces", au13mai24, 400_000, 0.04),
            new Argent("Compte epargne", au13mai24, 200_000, 0.04),
            new Argent("Compte courant", au13mai24, 600_000, 0.04)));

    assertEquals(1_200_000, patrimoineIloAu13mai24.getValeurComptable());
  }
  @Test
  void testValeurComptableFutureArgent() {
    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var au13mai29 = Instant.parse("2029-05-13T00:00:00.00Z");

    var argent = new Argent("Compte epargne", au13mai24, 200_000, 0.04);

    long joursEntre = ChronoUnit.DAYS.between(au13mai24, au13mai29);
    double tauxInteretQuotidien = 0.04 / 365;
    double valeurAttendueArgent = Math.round(200_000 * Math.pow(1 + tauxInteretQuotidien, joursEntre));

    double valeurComptableFutureArgent = argent.valeurComptableFuture(au13mai29);
    assertEquals(valeurAttendueArgent, valeurComptableFutureArgent, 0.01);
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

}