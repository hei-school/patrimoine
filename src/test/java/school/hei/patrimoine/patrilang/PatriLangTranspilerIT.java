package school.hei.patrimoine.patrilang;

import static java.time.Month.APRIL;
import static org.antlr.v4.runtime.CharStreams.fromString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Devise.MGA;

import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Creance;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.Possession;

public class PatriLangTranspilerIT {
  PatriLangTranspiler subject = new PatriLangTranspiler();
  private static final LocalDate AU_18_AVRIL_2025 = LocalDate.of(2025, APRIL, 18);

  @Test
  void patrimoine_without_possession_ok() {
    var expected = patrimoineWithoutPossessions();
    var input = fromString(SECTION_GENERAL);

    var actual = subject.apply(input);

    assertEquals(expected.getT(), actual.getT());
    assertEquals(expected.getNom(), actual.getNom());
    assertEquals(expected.getDevise(), actual.getDevise());
    assertEquals(expected.getValeurComptable(), actual.getValeurComptable());
  }

  @Test
  void patrimoine_with_trésorier_ok() {
    var expected = patrimoineWithTrésoriers();
    var input = fromString(SECTION_GENERAL + SECTION_TRESORIER);

    var actual = subject.apply(input);

    assertEquals(expected.getT(), actual.getT());
    assertEquals(expected.getNom(), actual.getNom());
    assertEquals(expected.getDevise(), actual.getDevise());
    assertEquals(expected.getValeurComptable(), actual.getValeurComptable());
    assertEquals(expected.getPossessions(), actual.getPossessions());
  }

  @Test
  void patrimoine_with_créance_ok() {
    var expected = patrimoineWithCréances();
    var input = fromString(SECTION_GENERAL + SECTION_CREANCE);

    var actual = subject.apply(input);

    assertEquals(expected.getT(), actual.getT());
    assertEquals(expected.getNom(), actual.getNom());
    assertEquals(expected.getDevise(), actual.getDevise());
    assertEquals(expected.getValeurComptable(), actual.getValeurComptable());
    assertEquals(expected.getPossessions(), actual.getPossessions());
  }

  @Test
  void patrimoine_with_dettes_ok() {
    var expected = patrimoineWithDettes();
    var input = fromString(SECTION_GENERAL + SECTION_DETTE);

    var actual = subject.apply(input);

    assertEquals(expected.getT(), actual.getT());
    assertEquals(expected.getNom(), actual.getNom());
    assertEquals(expected.getDevise(), actual.getDevise());
    assertEquals(expected.getValeurComptable(), actual.getValeurComptable());
    assertEquals(expected.getPossessions(), actual.getPossessions());
  }

  Patrimoine patrimoineWithoutPossessions() {
    return Patrimoine.of(
        "Patrimoine de Zety", MGA, AU_18_AVRIL_2025, new Personne("Zety"), Set.of());
  }

  Patrimoine patrimoineWithTrésoriers() {
    return Patrimoine.of(
        "Patrimoine de Zety", MGA, AU_18_AVRIL_2025, new Personne("Zety"), trésoriers());
  }

  Patrimoine patrimoineWithCréances() {
    return Patrimoine.of(
        "Patrimoine de Zety", MGA, AU_18_AVRIL_2025, new Personne("Zety"), créances());
  }

  Patrimoine patrimoineWithDettes() {
    return Patrimoine.of(
        "Patrimoine de Zety", MGA, AU_18_AVRIL_2025, new Personne("Zety"), dettes());
  }

  Set<Possession> trésoriers() {
    return Set.of(
        new Compte("BMOI", AU_18_AVRIL_2025, ariary(15_000)),
        new Compte("BNI", AU_18_AVRIL_2025, ariary(15_000)));
  }

  Set<Possession> créances() {
    return Set.of(
        new Creance("Myriade_Fr", AU_18_AVRIL_2025, ariary(5_000)),
        new Creance("FanoCréance", AU_18_AVRIL_2025, ariary(3_000)));
  }

  Set<Possession> dettes() {
    return Set.of(
        new Dette("DetteA", AU_18_AVRIL_2025, ariary(5_000).mult(-1)),
        new Dette("DetteB", AU_18_AVRIL_2025, ariary(3_000).mult(-1)));
  }

  private static final String SECTION_GENERAL =
      """
          # Général
          * Spécifié le 18 du 04-2025
          * Patrimoine de Zety
          * Devise en Ar
      """;

  private static final String SECTION_TRESORIER =
      """
          # Trésoreries
          * BMOI, valant 15000Ar le 18 du 04-2025
          * BNI, valant 15000Ar le 18 du 04-2025
      """;

  private static final String SECTION_CREANCE =
      """
          # Créances
          * Myriade_Fr, valant 5000Ar le 18 du 04-2025
          * FanoCréance, valant 3000Ar le 18 du 04-2025
      """;

  private static final String SECTION_DETTE =
      """
          # Dettes
          * DetteA, valant 5000Ar le 18 du 04-2025
          * DetteB, valant 3000Ar le 18 du 04-2025
      """;
}
