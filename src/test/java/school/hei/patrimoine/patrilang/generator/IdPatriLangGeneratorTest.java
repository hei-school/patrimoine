package school.hei.patrimoine.patrilang.generator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class IdPatriLangGeneratorTest {
  private static final IdPatriLangGenerator subject = new IdPatriLangGenerator();

  @Test
  void apply() {
    var actual = subject.apply("  Mon Identifiant Test  ");
    assertEquals("Mon_Identifiant_Test", actual);
  }

  @Test
  void apply_empty_string() {
    var actual = subject.apply("     ");
    assertEquals("", actual);
  }

  @Test
  void apply_with_special_characters_and_long_spaces() {
    var actual = subject.apply("  Identifiant      @    2024!  ");
    assertEquals("Identifiant_@_2024!", actual);
  }
}
