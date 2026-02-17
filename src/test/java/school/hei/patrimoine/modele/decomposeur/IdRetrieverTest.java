package school.hei.patrimoine.modele.decomposeur;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.decomposeur.IdRetriever.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class IdRetrieverTest {
  @Test
  void should_extract_baseId_when_decomposedId_is_valid() {
    var id = "salaire__du_2024_12_12";

    var actual = getBaseIdFromDecomposedId(id);

    assertEquals("salaire", actual);
  }

  @Test
  void should_return_same_id_when_id_has_no_separator() {
    var expected = "salaire_2024-05-10";
    var actual = getBaseIdFromDecomposedId(expected);

    assertEquals(expected, actual);
  }

  @Test
  void should_handle_baseId_containing_separator_pattern() {
    var expected = "revenu__du_special";

    var actual = getBaseIdFromDecomposedId(expected);

    assertEquals(expected, actual);
  }

  @Test
  void should_generate_decomposed_id() {
    var baseId = "fluxArgent";
    var date = LocalDate.of(2024, 6, 15);

    var result = getDecomposedId(baseId, date);

    assertEquals("fluxArgent__du_2024_06_15", result);
  }

  @Test
  void should_extract_planned_id_from_multiple_realisation() {
    var realisationId = "[plannedFlux]__somethingElse";

    var result = getPlannedIdFromRealisationId(realisationId);

    assertEquals("plannedFlux", result);
  }

  @Test
  void should_return_same_id_if_not_multiple_realisation() {
    var realisationId = "simpleFlux";

    var result = getPlannedIdFromRealisationId(realisationId);

    assertEquals("simpleFlux", result);
  }
}
