package school.hei.patrimoine.modele;

import static java.time.Month.SEPTEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Argent.euro;
import static school.hei.patrimoine.modele.Devise.EUR;
import static school.hei.patrimoine.modele.Devise.MGA;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ArgentTest {
  @Test
  void eur_to_mga() {
    var t = LocalDate.of(2024, SEPTEMBER, 1);

    assertEquals(ariary(4_844), euro(1).convertir(MGA, t));
    assertEquals(ariary(4_989), euro(1).convertir(MGA, t.plusYears(1)));
    assertEquals(ariary(9_688), euro(2).convertir(MGA, t));
    assertEquals(ariary(10_268), euro(2).convertir(MGA, t.plusYears(2)));
    assertEquals(ariary(9_110), euro(2).convertir(MGA, t.minusYears(2)));
  }

  @Test
  void mga_to_eur() {
    var t = LocalDate.of(2024, SEPTEMBER, 1);

    assertEquals(euro(1), ariary(4_741).convertir(EUR, t));
    assertEquals(euro(2), ariary(9_482).convertir(EUR, t));
    assertEquals(euro(2), ariary(7_554).convertir(EUR, t.plusYears(2)));
    assertEquals(euro(2), ariary(11_414).convertir(EUR, t.minusYears(2)));
  }
}
