package school.hei.patrimoine.modele.possession;

import static java.time.Month.AUGUST;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static school.hei.patrimoine.modele.Argent.ariary;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class CompteTest {
  @Test
  void test_compte_getTypeFEC() {
    var le19Août25 = LocalDate.of(2025, AUGUST, 19);
    var compte = new Compte("compte", le19Août25, ariary(100_000));

    assertNotNull(compte.getTypeFEC());
  }
}
