package school.hei.patrimoine.patrilang;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.transpileToutCas;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.cas.CasSet;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.patrilang.famille_rakoto_cas.FamilleRakotoCasSet;

class PatriLangTranspilerIT {

  private static final String RESOURCE_PATH = "/famille_rakoto_cas/FamilleRakoto.patri.md";
  private static final Path RESOURCE_FILE_PATH;

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
    assertPossesseursEquals(
        expected.patrimoine().getPossesseurs(), actual.patrimoine().getPossesseurs());
    assertPossessionsEquals(expected.possessions(), actual.possessions());
  }

  private void assertPossesseursEquals(
      Map<Personne, Double> expectedMap, Map<Personne, Double> actualMap) {
    for (var expected : expectedMap.entrySet()) {
      var actual =
          actualMap.entrySet().stream()
              .filter(p -> p.getKey().nom().equals(expected.getKey().nom()))
              .findFirst()
              .orElseThrow(
                  () -> new RuntimeException("Missing possesseurs: " + expected.getKey().nom()));

      assertEquals(expected.getValue(), actual.getValue());
      assertEquals(expected.getKey().nom(), actual.getKey().nom());
    }
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
  }
}
