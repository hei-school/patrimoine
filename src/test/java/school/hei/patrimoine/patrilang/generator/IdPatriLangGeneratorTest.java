package school.hei.patrimoine.patrilang.generator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class IdPatriLangGeneratorTest {
  private final IdPatriLangGenerator subject = new IdPatriLangGenerator();

  @Test
  void apply() {
    var result = subject.apply("  Mon Identifiant Test  ");
    assertEquals("Mon_Identifiant_Test", result);
  }

  @Test
  void apply_empty_string() {
    var result = subject.apply("     ");
    assertEquals("", result);
  }

  @Test
  void apply_with_special_characters_and_long_spaces() {
    var result = subject.apply("  Identifiant      @    2024!  ");
    assertEquals("Identifiant_@_2024!", result);
  }
}
