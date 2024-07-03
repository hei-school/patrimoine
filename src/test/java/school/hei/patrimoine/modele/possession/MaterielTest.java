package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static java.time.Month.*;
import static java.time.Month.SEPTEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MaterielTest {

  @Test
  void mon_mac_s_apprecie_negativement_dans_le_futur() {
    var au26Oct21 = LocalDate.of(2021, OCTOBER, 26);
    var mac = new Materiel(
        "MacBook Pro",
        au26Oct21,
        2_000_000,
        au26Oct21.minusDays(2),
        -0.10);

    var au26juin24 = LocalDate.of(2024, JUNE, 26);
    assertEquals(1_466_301, mac.valeurComptableFuture(au26juin24));
  }

  @Test
  void possession_de_zety_s_apprecie_negativement_dans_le_futur() {
    var au3Juillet24 = LocalDate.of(2024, JULY, 03);
    var ordinateur = new Materiel(
            "ordinateur",
            au3Juillet24,
            1_200_000,
            au3Juillet24.minusDays(1),
            -0.10
    );

    var vetements = new Materiel(
            "vêtements",
            au3Juillet24,
            1_500_000,
            au3Juillet24.minusDays(1),
            -0.50
    );

    var au17Sept24 = LocalDate.of(2024, SEPTEMBER, 17);
    assertEquals(1175013, ordinateur.valeurComptableFuture(au17Sept24));
    assertEquals(1343835, vetements.valeurComptableFuture(au17Sept24));
  }
}