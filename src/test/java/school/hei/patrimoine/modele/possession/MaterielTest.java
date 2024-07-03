package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static java.time.Month.*;
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
  void zetyOrdinateur(){
    var au3Juil24 = LocalDate.of(2024, JULY, 3);
    var pc = new Materiel(
            "pc de Zety",
            au3Juil24,
            1_200_000,
            au3Juil24.minusDays(2),
            -0.10);

    var au17Sept24 = LocalDate.of(2024, SEPTEMBER, 17);
    assertEquals( 1_175_013, pc.valeurComptableFuture(au17Sept24));
  }
}