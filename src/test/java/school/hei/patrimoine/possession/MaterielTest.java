package school.hei.patrimoine.possession;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MaterielTest {

  @Test
  void mon_mac_s_apprecie_negativement_dans_le_futur() {
    var au26Oct21 = Instant.parse("2022-10-26T00:00:00.00Z");
    var mac = new Materiel(
        "MacBook Pro",
        au26Oct21,
        2_000_000,
        -0.10);
    var au26juin24 = Instant.parse("2024-06-26T00:00:00.00Z");
    assertTrue(
            mac.getValeurComptable() > mac.valeurComptableFuture(au26juin24));
  }
}