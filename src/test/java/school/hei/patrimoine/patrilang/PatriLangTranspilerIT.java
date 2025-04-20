package school.hei.patrimoine.patrilang;

import static java.time.Month.DECEMBER;
import static java.time.Month.SEPTEMBER;
import static java.util.Comparator.comparing;
import static org.antlr.v4.runtime.CharStreams.fromString;
import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.patrilang.TestUtils.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.*;

class PatriLangTranspilerIT {
  private static final LocalDate AU_20_SEPTEMBER_2026 = LocalDate.of(2026, SEPTEMBER, 20);
  private static final LocalDate AU_31_DECEMBER_2025 = LocalDate.of(2025, DECEMBER, 31);
  PatriLangTranspiler subject = new PatriLangTranspiler();

  @Test
  void patrimoine_without_possession_ok() {
    var expected = patrimoineWithoutPossessions();
    var input = fromString(SECTION_GENERAL);

    var actual = subject.apply(input);

    assertPatrimoineEquals(expected, actual);
  }

  @Test
  void patrimoine_with_trésorier_ok() {
    var expected = patrimoineWithTrésoriers();
    var input = fromString(SECTION_GENERAL + SECTION_TRÉSORIER);

    var actual = subject.apply(input);

    assertPatrimoineEquals(expected, actual);
  }

  @Test
  void patrimoine_with_créance_ok() {
    var expected = patrimoineWithCréances();
    var input = fromString(SECTION_GENERAL + SECTION_CREANCE);

    var actual = subject.apply(input);

    assertPatrimoineEquals(expected, actual);
  }

  @Test
  void patrimoine_with_dettes_ok() {
    var expected = patrimoineWithDettes();
    var input = fromString(SECTION_GENERAL + SECTION_DETTE);

    var actual = subject.apply(input);

    assertPatrimoineEquals(expected, actual);
  }

  @Test
  void patrimoine_with_operations_ok() {
    var expected = patrimoineWithTrésorierEtOpérations();
    var input = fromString(SECTION_GENERAL + SECTION_TRÉSORIER + SECTION_OPERATION);

    var actual = subject.apply(input);

    assertPatrimoineEquals(expected, actual);
  }

  @Test
  void patrimoine_with_group_operations_ok() {
    var expected = patrimoineWithTrésorierEtGroupOpérations();
    var input =
        fromString(SECTION_GENERAL + SECTION_TRÉSORIER + SECTION_OPERATION_WITH_GROUP_POSSESSION);

    var actual = subject.apply(input);

    assertPatrimoineEquals(expected, actual);
  }

  List<Possession> sortPossessions(Set<Possession> possessions) {
    return possessions.stream().sorted(comparing(Possession::nom)).collect(Collectors.toList());
  }

  void assertPatrimoineEquals(Patrimoine expected, Patrimoine actual) {
    assertPatrimoineValueEquals(expected, actual);
    assertPatrimoineValueEquals(
        expected.projectionFuture(AU_20_SEPTEMBER_2026),
        expected.projectionFuture(AU_20_SEPTEMBER_2026));
    assertPatrimoineValueEquals(
        expected.projectionFuture(AU_31_DECEMBER_2025),
        expected.projectionFuture(AU_31_DECEMBER_2025));
  }

  void assertPatrimoineValueEquals(Patrimoine expected, Patrimoine actual) {
    assertEquals(expected.getT(), actual.getT());
    assertEquals(expected.getNom(), actual.getNom());
    assertEquals(expected.getDevise(), actual.getDevise());
    assertEquals(expected.getPossessions().size(), actual.getPossessions().size());
    assertEquals(expected.getValeurComptable(), actual.getValeurComptable());

    var expectedSortedPossession = sortPossessions(expected.getPossessions());
    var actualSortedPossession = sortPossessions(actual.getPossessions());

    for (int i = 0; i < expectedSortedPossession.size(); i++) {
      System.out.println(expectedSortedPossession.get(i) + " -> " + actualSortedPossession.get(i));
      assertEquals(expectedSortedPossession.get(i), actualSortedPossession.get(i));
      assertEquals(
          expectedSortedPossession.get(i).valeurComptable(),
          actualSortedPossession.get(i).valeurComptable());
    }
  }
}
