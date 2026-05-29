package school.hei.patrimoine.modele.normalizer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.normalizer.PossessionNomNormalizer.normalize;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class PossessionNomNormalizerTest {
  @Test
  void case_1() {
    var input = "Jean-Pierre";
    var expected = "Jean_Pierre";
    assertEquals(expected, normalize(input));
  }

  @Test
  void case_2() {
    var input = "  Jean-Pierre  " + LocalDate.of(2024, 12, 12);
    var expected = "Jean_Pierre_2024_12_12";
    assertEquals(expected, normalize(input));
  }

  @Test
  void case_3() {
    var input = "Jean--Pierre";
    var expected = "Jean_Pierre";
    assertEquals(expected, normalize(input));
  }
}
