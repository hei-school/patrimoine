package school.hei.patrimoine.visualisation.swing.ihm.google.validator;

import static java.lang.Double.parseDouble;

public class NombreValidator {
  public static void validate(String input) {
    try {
      if (input == null) {
        throw new IllegalArgumentException();
      }

      parseDouble(input.trim().replaceAll(" ", "").replaceAll("_", ""));
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(
          String.format("Nombre invalide '%s' (syntaxe incorrecte)", input));
    }
  }
}
