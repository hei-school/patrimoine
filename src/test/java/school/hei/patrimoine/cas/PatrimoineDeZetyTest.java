package school.hei.patrimoine.cas;

import static java.time.Month.DECEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Argent.ariary;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.cas.CasSet;
import school.hei.patrimoine.cas.CasSetSupplier;
import school.hei.patrimoine.cas.examen2.bako.BakoCas;


class PatrimoineDeBakoTest {

  private final CasSetSupplier casSetSupplier = new CasSetSupplier();

  private CasSet casSet() {
    return casSetSupplier.get();
  }

  private BakoCas bakoCas() {
    return (BakoCas) casSet().getCasSet().stream().filter(c -> c instanceof BakoCas).findFirst().get();
  }

  @Test
  void bako_patrimoine_au_31_decembre_2025() {
    BakoCas bakoCas = bakoCas();
    Patrimoine patrimoineDeBako = bakoCas.patrimoine();

    var patrimoineProjete = patrimoineDeBako.projectionFuture(LocalDate.of(2025, DECEMBER, 31));

    assertEquals(ariary(13_711_657), patrimoineProjete.getValeurComptable());
  }
}
