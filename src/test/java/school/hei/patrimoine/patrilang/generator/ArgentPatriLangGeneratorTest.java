package school.hei.patrimoine.patrilang.generator;

import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Devise.*;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;

class ArgentPatriLangGeneratorTest {
  private static final ArgentPatriLangGenerator subject = new ArgentPatriLangGenerator();

  @Test
  void apply_MGA_devise() {
    var argent = new Argent(125000, MGA);

    var result = subject.apply(argent);

    assertEquals("125_000Ar", result);
  }

  @Test
  void apply_EUR_devise() {
    var argent = new Argent(12500, EUR);
    var result = subject.apply(argent);

    assertEquals("12_500€", result);
  }

  @Test
  void apply_dollar_devise() {
    var argent = new Argent(12500, CAD);
    var result = subject.apply(argent);

    assertEquals("12_500$", result);
  }

  @Test
  void apply_must_throw_without_devise() {
    var argent = new Argent(12500, null);

    assertThrows(NullPointerException.class, () -> subject.apply(argent));
  }

  @Test
  void apply_with_decimals() {
    var argent = new Argent(12345.67, EUR);
    var result = subject.apply(argent);

    assertEquals("12_345.67€", result);
  }

  @Test
  void montant() {
    var argent = new Argent(9876543, EUR);
    var result = ArgentPatriLangGenerator.montant(argent);

    assertEquals("9_876_543", result);
  }
}
