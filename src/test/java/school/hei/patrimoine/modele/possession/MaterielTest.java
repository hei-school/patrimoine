package school.hei.patrimoine.modele.possession;

import static java.time.Month.JUNE;
import static java.time.Month.OCTOBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Argent.ariary;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class MaterielTest {

  @Test
  void mon_mac_s_apprecie_negativement_dans_le_futur() {
    var au26Oct21 = LocalDate.of(2021, OCTOBER, 26);
    var mac =
        new Materiel("MacBook Pro", au26Oct21.minusDays(2), au26Oct21, ariary(2_000_000), -0.10);

    var au26juin24 = LocalDate.of(2024, JUNE, 26);
    assertEquals(ariary(1_466_301), mac.valeurComptableFuture(au26juin24));
  }
}
