package school.hei.patrimoine;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.possession.Argent;
import school.hei.patrimoine.possession.Possession;

import java.time.Instant;
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
  void valeur_comptable_future(){
    var ilo = new Personne("Ilo");

    var au13mai24 = Instant.parse("2024-05-13T00:00:00.00Z");
    var au13mai29 = Instant.parse("2029-05-13T00:00:00.00Z");

    var argent = new Argent("Compte epargne", au13mai24, 200_000);

    assertEquals(200_000 * Math.pow(1 + 0.04 / 365, ChronoUnit.DAYS.between(au13mai24, au13mai29)),argent.valeurComptableFuture(au13mai29), 0.01);
  }
}