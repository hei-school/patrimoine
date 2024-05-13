package school.hei.patrimoine;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PatrimoineTest {

  @Test
  void patrimoine_vide_vaut_0() {
    var ilo = new Personne("Ilo");
    var patrimoineIlo = new Patrimoine(
        ilo, Instant.parse("2024-05-13T00:00:00.00Z"));

    Possession ordinateur = new Possession();
    patrimoineIlo.addPossession(ordinateur);

    assertEquals(0, patrimoineIlo.getValeurComptableActuelle());
  }
}