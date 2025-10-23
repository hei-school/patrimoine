package school.hei.patrimoine.patrilang.generator;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.patrilang.generator.possession.FluxArgentPatriLangGenerator;

class PatriLangGeneratorFactoryTest {
  private final PatriLangGeneratorFactory subject = new PatriLangGeneratorFactory();
  private final LocalDate date = LocalDate.of(2025, 1, 1);
  private static final Argent argent = Argent.ariary(1000);
  private final Compte compte = new Compte("comptePersonnel", date, argent);

  @Test
  void make() {
    var fluxArgent = new FluxArgent("id", compte, date, Argent.ariary(900));
    var generator = PatriLangGeneratorFactory.make(fluxArgent);
    assertInstanceOf(FluxArgentPatriLangGenerator.class, generator);
  }
}
