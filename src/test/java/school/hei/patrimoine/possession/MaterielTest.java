package school.hei.patrimoine.possession;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MaterielTest {

  @Test
  void mon_mac_s_apprecie_negativement_dans_le_futur() {
    var au26Oct21 = Instant.parse("2021-10-26T00:00:00.00Z");
    var mac = new Materiel(
        "MacBook Pro",
        au26Oct21,
        2_000_000,
        -0.10);

    var au26juin24 = Instant.parse("2024-06-26T00:00:00.00Z");
    assertTrue(
        mac.getValeurComptable() > mac.valeurComptableFuture(au26juin24));
  }

  @Test
  void testProjectionFuture() {
    String nom = "Ordinateur portable";
    Instant t = Instant.parse("2022-01-01T00:00:00Z");
    int valeurComptable = 1000;
    double tauxDAppreciationAnnuelle = 0.05; // 5%
    Materiel materiel = new Materiel(nom, t, valeurComptable, tauxDAppreciationAnnuelle);

    Instant tFutur = Instant.parse("2023-01-01T00:00:00Z");

    Materiel materielProjete = materiel.projectionFuture(tFutur);

    double valeurComptableAttendue = valeurComptable * Math.pow(1 + tauxDAppreciationAnnuelle, 1); // Pour une année de projection

    assertEquals((int)Math.round(valeurComptableAttendue), materielProjete.getValeurComptable());
  }
}