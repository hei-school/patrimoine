package school.hei.patrimoine.modele.decomposeur;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.decomposeur.IdRetriever.getDecomposedId;
import static school.hei.patrimoine.utils.Comparator.isTransfertArgentEquals;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.TransfertArgent;

class TransfertArgentDecomposeurTest {
  private static final LocalDate DEBUT = LocalDate.of(2025, 1, 1);
  private static final LocalDate FIN = LocalDate.of(2025, 12, 31);
  private static final String TRANSFERT_ARGENT_BASE_ID = "transfertArgent";
  private static final Argent TRANSFERT_ARGENT_TRANSFERT_MENSUEL = ariary(200);

  private static final TransfertArgentDecomposeur subject =
      new TransfertArgentDecomposeur(DEBUT, FIN);

  @Test
  void empty_if_operation_out_of_range() {
    var transfert = transfertArgent(DEBUT.minusMonths(2), DEBUT.minusMonths(1), 3);
    assertTrue(subject.apply(transfert).isEmpty());
  }

  @Test
  void full_decomposed_if_fit_inside_range() {
    var transfert = transfertArgent(DEBUT.plusDays(1), LocalDate.of(2025, 3, 31), 2);
    var expected =
        List.of(
            expected(LocalDate.of(2025, 1, 2)),
            expected(LocalDate.of(2025, 2, 2)),
            expected(LocalDate.of(2025, 3, 2)));

    var actual = subject.apply(transfert);
    assertTrue(isTransfertArgentEquals(expected, actual));
  }

  @Test
  void decomposed_until_fin() {
    var transfert = transfertArgent(LocalDate.of(2025, 10, 31), FIN.plusMonths(12), 31);
    var expected =
        List.of(
            expected(LocalDate.of(2025, 10, 31)),
            expected(LocalDate.of(2025, 11, 30)),
            expected(LocalDate.of(2025, 12, 31)));

    var actual = subject.apply(transfert);
    assertTrue(isTransfertArgentEquals(expected, actual));
  }

  @Test
  void decomposed_starting_from_debut() {
    var transfert = transfertArgent(LocalDate.of(2024, 10, 31), LocalDate.of(2025, 3, 31), 31);
    var expected =
        List.of(
            expected(LocalDate.of(2025, 1, 31)),
            expected(LocalDate.of(2025, 2, 28)),
            expected(LocalDate.of(2025, 3, 31)));

    var actual = subject.apply(transfert);
    assertTrue(isTransfertArgentEquals(expected, actual));
  }

  @Test
  void decomposed_starting_from_debut_and_end_on_fin() {
    var transfert = transfertArgent(LocalDate.of(2024, 10, 31), LocalDate.of(2026, 3, 31), 31);
    var expected =
        List.of(
            expected(LocalDate.of(2025, 1, 31)),
            expected(LocalDate.of(2025, 2, 28)),
            expected(LocalDate.of(2025, 3, 31)),
            expected(LocalDate.of(2025, 4, 30)),
            expected(LocalDate.of(2025, 5, 31)),
            expected(LocalDate.of(2025, 6, 30)),
            expected(LocalDate.of(2025, 7, 31)),
            expected(LocalDate.of(2025, 8, 31)),
            expected(LocalDate.of(2025, 9, 30)),
            expected(LocalDate.of(2025, 10, 31)),
            expected(LocalDate.of(2025, 11, 30)),
            expected(LocalDate.of(2025, 12, 31)));

    var actual = subject.apply(transfert);
    assertTrue(isTransfertArgentEquals(expected, actual));
  }

  private static TransfertArgent expected(LocalDate date) {
    return new TransfertArgent(
        getDecomposedId(TRANSFERT_ARGENT_BASE_ID, date),
        depuisCompte(),
        versCompte(),
        date,
        TRANSFERT_ARGENT_TRANSFERT_MENSUEL);
  }

  private static TransfertArgent transfertArgent(
      LocalDate debut, LocalDate fin, int dateOperation) {
    return new TransfertArgent(
        TRANSFERT_ARGENT_BASE_ID,
        depuisCompte(),
        versCompte(),
        debut,
        fin,
        dateOperation,
        TRANSFERT_ARGENT_TRANSFERT_MENSUEL);
  }

  private static Compte depuisCompte() {
    return new Compte("depuisCompte", DEBUT, ariary(10));
  }

  private static Compte versCompte() {
    return new Compte("versCompte", DEBUT, ariary(10));
  }
}
