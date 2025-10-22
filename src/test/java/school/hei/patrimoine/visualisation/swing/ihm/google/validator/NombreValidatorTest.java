package school.hei.patrimoine.visualisation.swing.ihm.google.validator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class NombreValidatorTest {

  @Test
  void validate_number() {
    assertDoesNotThrow(() -> NombreValidator.validate("123"));
    assertDoesNotThrow(() -> NombreValidator.validate("  456  "));
    assertDoesNotThrow(() -> NombreValidator.validate("7_890"));
    assertDoesNotThrow(() -> NombreValidator.validate("1 234.56"));
    assertDoesNotThrow(() -> NombreValidator.validate("-987.65"));
    assertDoesNotThrow(() -> NombreValidator.validate("0"));
  }

  @Test
  void validate_invalid_number() {
    Exception exception1 =
        assertThrows(IllegalArgumentException.class, () -> NombreValidator.validate("abc"));
    assertEquals("Nombre invalide 'abc' (syntaxe incorrecte)", exception1.getMessage());

    Exception exception2 =
        assertThrows(IllegalArgumentException.class, () -> NombreValidator.validate("12.34.56"));
    assertEquals("Nombre invalide '12.34.56' (syntaxe incorrecte)", exception2.getMessage());

    Exception exception3 =
        assertThrows(IllegalArgumentException.class, () -> NombreValidator.validate(null));
    assertEquals("Nombre invalide 'null' (syntaxe incorrecte)", exception3.getMessage());

    Exception exception4 =
        assertThrows(IllegalArgumentException.class, () -> NombreValidator.validate("12a34"));
    assertEquals("Nombre invalide '12a34' (syntaxe incorrecte)", exception4.getMessage());
  }
}
