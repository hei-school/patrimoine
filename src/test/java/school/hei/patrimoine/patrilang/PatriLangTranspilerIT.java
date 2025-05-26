package school.hei.patrimoine.patrilang;

import static java.time.Month.*;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.transpileToutCas;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.cas.CasSet;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.patrilang.famille_rakoto_cas.FamilleRakotoCasSet;

class PatriLangTranspilerIT {

  private static final String RESOURCE_PATH = "/famille_rakoto_cas/FamilleRakoto.patri.md";
  private static final Path RESOURCE_FILE_PATH;
  private static final List<LocalDate> CHECK_DATES =
      List.of(
          LocalDate.of(2025, FEBRUARY, 7),
          LocalDate.of(2025, MARCH, 17),
          LocalDate.of(2025, APRIL, 27));

  static {
    try {
      var uri = requireNonNull(PatriLangTranspilerIT.class.getResource(RESOURCE_PATH)).toURI();
      RESOURCE_FILE_PATH = Paths.get(uri);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void transpile_casSet_should_match_expected() {
    var expected = new FamilleRakotoCasSet().get();
    var actual = transpileToutCas(RESOURCE_FILE_PATH.toString());

    assertEquals(expected.set().size(), actual.set().size(), "Cas set sizes do not match");
    assertEquals(expected.objectifFinal(), actual.objectifFinal(), "Objectif final mismatch");

    assertCasSetEquals(expected, actual);
  }

  private void assertCasSetEquals(CasSet expectedSet, CasSet actualSet) {
    for (var expected : expectedSet.set()) {
      var actual =
          actualSet.set().stream()
              .filter(c -> c.patrimoine().nom().equals(expected.patrimoine().nom()))
              .findFirst()
              .orElseThrow(
                  () -> new RuntimeException("Missing case for: " + expected.patrimoine().nom()));
      assertCasEquals(expected, actual);
    }
  }

  private void assertCasEquals(Cas expected, Cas actual) {
    assertEquals(expected.getAjd(), actual.getAjd(), "Ajd mismatch");
    assertEquals(expected.getFinSimulation(), actual.getFinSimulation(), "Fin simulation mismatch");
    assertEquals(
        expected.patrimoine().getValeurComptable(),
        actual.patrimoine().getValeurComptable(),
        "Valeur comptable mismatch");

    CHECK_DATES.forEach(
        date ->
            assertEquals(
                expected.patrimoine().projectionFuture(date).getValeurComptable(),
                actual.patrimoine().projectionFuture(date).getValeurComptable()));

    assertPossessionsEquals(expected.possessions(), actual.possessions());
  }

  private void assertPossessionsEquals(Set<Possession> expectedSet, Set<Possession> actualSet) {
    for (var expected : expectedSet) {
      var actual =
          actualSet.stream()
              .filter(p -> p.nom().equals(expected.nom()))
              .findFirst()
              .orElseThrow(() -> new RuntimeException("Missing possession: " + expected.nom()));
      assertPossessionEquals(expected, actual);
    }
  }

  private void assertPossessionEquals(Possession expected, Possession actual) {
    assertEquals(expected.nom(), actual.nom());
    assertEquals(expected.devise(), actual.devise());
    assertEquals(expected.typeAgregat(), actual.typeAgregat());
    assertEquals(expected.valeurComptable(), actual.valeurComptable());

    CHECK_DATES.forEach(
        date ->
            assertEquals(
                expected.projectionFuture(date).valeurComptable(),
                actual.projectionFuture(date).valeurComptable()));
  }
}
