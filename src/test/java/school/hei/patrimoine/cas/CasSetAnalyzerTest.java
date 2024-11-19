package school.hei.patrimoine.cas;

import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.conf.TestUtils.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CasSetAnalyzerTest {

  private CasSetAnalyzer analyzer;

  @BeforeEach
  void set_up() {
    analyzer = new CasSetAnalyzer();
  }

  @Test
  void cas_set_objective_achieved_ok() {
    assertDoesNotThrow(() -> analyzer.accept(casSet1()));
  }

  @Test
  void cas_set_objective_achieved_ko() {
    RuntimeException exception =
        assertThrows(RuntimeException.class, () -> analyzer.accept(casSet2()));
    assertTrue(exception.getMessage().contains("Objectifs non atteints"));
  }
}
