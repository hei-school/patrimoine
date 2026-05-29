package school.hei.patrimoine.modele.decomposeur;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.decomposeur.IdRetriever.getDecomposedId;
import static school.hei.patrimoine.utils.Comparator.isFluxArgentEquals;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;

class FluxArgentDecomposeurTest {
  private static final LocalDate DEBUT = LocalDate.of(2025, 1, 1);
  private static final LocalDate FIN = LocalDate.of(2025, 12, 31);
  private static final FluxArgentDecomposeur subject = new FluxArgentDecomposeur(DEBUT, FIN);
  private static final String FLUX_ARGENT_BASE_ID = "fluxArgent";
  private static final Argent FLUX_ARGENT_FLUX_MENSUEL = ariary(200);

  @Test
  void empty_if_operation_out_of_range() {
    var flux = fluxArgent(DEBUT.minusMonths(2), DEBUT.minusMonths(1), 3);
    assertTrue(subject.apply(flux).isEmpty());
  }

  @Test
  void full_decomposed_if_fit_inside_range() {
    var flux = fluxArgent(DEBUT.plusDays(1), LocalDate.of(2025, 3, 31), 2);
    var expected =
        List.of(
            expectedFluxArgent(LocalDate.of(2025, 1, 2)),
            expectedFluxArgent(LocalDate.of(2025, 2, 2)),
            expectedFluxArgent(LocalDate.of(2025, 3, 2)));

    var actual = subject.apply(flux);
    assertTrue(isFluxArgentEquals(expected, actual));
  }

  @Test
  void decomposed_until_fin() {
    var flux = fluxArgent(LocalDate.of(2025, 10, 31), FIN.plusMonths(12), 31);
    var expected =
        List.of(
            expectedFluxArgent(LocalDate.of(2025, 10, 31)),
            expectedFluxArgent(LocalDate.of(2025, 11, 30)),
            expectedFluxArgent(LocalDate.of(2025, 12, 31)));

    var actual = subject.apply(flux);
    assertTrue(isFluxArgentEquals(expected, actual));
  }

  @Test
  void decomposed_starting_from_debut() {
    var flux = fluxArgent(LocalDate.of(2024, 10, 31), LocalDate.of(2025, 3, 31), 31);
    var expected =
        List.of(
            expectedFluxArgent(LocalDate.of(2025, 1, 31)),
            expectedFluxArgent(LocalDate.of(2025, 2, 28)),
            expectedFluxArgent(LocalDate.of(2025, 3, 31)));

    var actual = subject.apply(flux);
    assertTrue(isFluxArgentEquals(expected, actual));
  }

  @Test
  void decomposed_starting_from_debut_and_end_on_fin() {
    var flux = fluxArgent(LocalDate.of(2024, 10, 31), LocalDate.of(2026, 3, 31), 31);
    var expected =
        List.of(
            expectedFluxArgent(LocalDate.of(2025, 1, 31)),
            expectedFluxArgent(LocalDate.of(2025, 2, 28)),
            expectedFluxArgent(LocalDate.of(2025, 3, 31)),
            expectedFluxArgent(LocalDate.of(2025, 4, 30)),
            expectedFluxArgent(LocalDate.of(2025, 5, 31)),
            expectedFluxArgent(LocalDate.of(2025, 6, 30)),
            expectedFluxArgent(LocalDate.of(2025, 7, 31)),
            expectedFluxArgent(LocalDate.of(2025, 8, 31)),
            expectedFluxArgent(LocalDate.of(2025, 9, 30)),
            expectedFluxArgent(LocalDate.of(2025, 10, 31)),
            expectedFluxArgent(LocalDate.of(2025, 11, 30)),
            expectedFluxArgent(LocalDate.of(2025, 12, 31)));

    var actual = subject.apply(flux);
    assertTrue(isFluxArgentEquals(expected, actual));
  }

  private static FluxArgent expectedFluxArgent(LocalDate date) {
    return new FluxArgent(
        getDecomposedId(FLUX_ARGENT_BASE_ID, date), compte(), date, FLUX_ARGENT_FLUX_MENSUEL);
  }

  private static FluxArgent fluxArgent(LocalDate debut, LocalDate fin, int dateOperation) {
    return new FluxArgent(
        FLUX_ARGENT_BASE_ID, compte(), debut, fin, dateOperation, FLUX_ARGENT_FLUX_MENSUEL);
  }

  private static Compte compte() {
    return new Compte("compte", DEBUT, ariary(10));
  }
}
