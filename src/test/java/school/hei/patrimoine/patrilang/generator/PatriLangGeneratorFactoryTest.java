package school.hei.patrimoine.patrilang.generator;

import static java.time.Month.JANUARY;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.patrilang.generator.possession.FluxArgentPatriLangGenerator;

class PatriLangGeneratorFactoryTest {
  private static final PatriLangGeneratorFactory subject = new PatriLangGeneratorFactory();
  private static final LocalDate date = LocalDate.of(2025, JANUARY, 1);
  private static final Argent argent = Argent.ariary(1000);
  private static final Compte compte = new Compte("comptePersonnel", date, argent);

  @Test
  void make() {
    var fluxArgent = new FluxArgent("id", compte, date, Argent.ariary(900));
    var generator = PatriLangGeneratorFactory.make(fluxArgent);
    assertInstanceOf(FluxArgentPatriLangGenerator.class, generator);
  }
}
