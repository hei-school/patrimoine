package school.hei.patrimoine.visualisation.swing.ihm.google.generator;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.visualisation.swing.ihm.google.generator.possession.ExecutionGenerator;
import school.hei.patrimoine.visualisation.swing.ihm.google.generator.possession.FluxArgentExecutionGenerator;

class PossessionGeneratorFactoryTest {

  @Test
  void make_returns_FluxArgentExecutionGenerator_when_type_is_FluxArgent() {
    var compte = new Compte("CompteEpargne", LocalDate.of(2025, 5, 10), Argent.ariary(1000000));
    var flux = new FluxArgent("Salaire", compte, LocalDate.of(2025, 7, 31), Argent.ariary(50000));

    ExecutionGenerator<FluxArgent> actual = PossessionGeneratorFactory.make(flux);

    assertNotNull(actual);
    assertInstanceOf(FluxArgentExecutionGenerator.class, actual);
  }

  @Test
  void make_throws_exception_when_type_unsupported_type() {
    var unsupported =
        new Compte("CompteEpargne", LocalDate.of(2025, 5, 10), Argent.ariary(1000000));

    var exception =
        assertThrows(
            IllegalArgumentException.class, () -> PossessionGeneratorFactory.make(unsupported));

    assertTrue(exception.getMessage().contains("Generating instance of clazz="));
  }
}
