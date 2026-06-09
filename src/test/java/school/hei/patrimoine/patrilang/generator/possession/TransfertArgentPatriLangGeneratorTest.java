package school.hei.patrimoine.patrilang.generator.possession;

import static java.time.Month.JANUARY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Argent.ariary;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.TransfertArgent;

class TransfertArgentPatriLangGeneratorTest {
  private static final TransfertArgentPatriLangGenerator subject =
      new TransfertArgentPatriLangGenerator();
  private static final LocalDate date = LocalDate.of(2025, JANUARY, 1);
  private static final Compte depuisCompte = new Compte("depuisCompte", date, ariary(5000));
  private static final Compte versCompte = new Compte("versCompte", date, ariary(5000));

  @Test
  void basique() {
    var transfert = new TransfertArgent("transfert1", depuisCompte, versCompte, date, ariary(1000));
    var actual = subject.apply(transfert);

    var expected =
        "* `transfert1`, le 1 janvier 2025 transférer 1_000Ar depuis Trésoreries:depuisCompte vers"
            + " Trésoreries:versCompte";
    assertEquals(expected, actual);
  }
}
