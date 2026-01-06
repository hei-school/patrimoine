package school.hei.patrimoine.modele.recouppement.decomposeur;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Argent.ariary;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

public class DecomposeurOperationPieceJustificativeTest {
  private final LocalDate finSimulation = LocalDate.of(2025, 12, 31);
  private final Compte compte =
      new Compte("compte courant", LocalDate.of(2020, 1, 1), ariary(1000));

  @Test
  void decompose_flux_argent_avec_piece_justificative() {
    var subject = new DecomposeurOperationPieceJustificative(finSimulation);

    PieceJustificative pj =
        new PieceJustificative("loyer", LocalDate.of(2025, 10, 1), "http://example.com/loyer.pdf");

    FluxArgent flux = new FluxArgent("loyer maison", compte, LocalDate.now(), ariary(500000), pj);

    List<Possession> possessions = List.of(flux);

    OperationDecomposee actual = subject.decomposePossession(possessions);

    assertEquals(1, actual.possessions().size());
    assertEquals(3, actual.piecesJustificatives().size());
    assertEquals("loyer__du_2025_10_01", actual.piecesJustificatives().getFirst().id());
  }

  @Test
  void ne_duplique_pas_la_date_dans_l_id() {
    var subject = new DecomposeurOperationPieceJustificative(finSimulation);

    PieceJustificative pj =
        new PieceJustificative(
            "loyer__du_2025_10_01", LocalDate.of(2025, 10, 1), "http://example.com/loyer.pdf");

    FluxArgent flux = new FluxArgent("loyer maison", compte, LocalDate.now(), ariary(500000), pj);

    OperationDecomposee actual = subject.decomposePossession(List.of(flux));

    assertEquals("loyer__du_2025_10_01", actual.piecesJustificatives().getFirst().id());
  }

  @Test
  void genere_une_piece_justificative_par_mois_jusqua_fin_simulation() {
    var subject = new DecomposeurOperationPieceJustificative(finSimulation);

    PieceJustificative pj =
        new PieceJustificative(
            "abonnement", LocalDate.of(2025, 10, 1), "http://example.com/abo.pdf");

    FluxArgent flux = new FluxArgent("abonnement", compte, LocalDate.now(), ariary(100000), pj);

    var actual = subject.decomposePossession(List.of(flux));

    assertEquals(3, actual.piecesJustificatives().size());

    List<LocalDate> dates =
        actual.piecesJustificatives().stream().map(PieceJustificative::date).toList();

    assertEquals(
        List.of(LocalDate.of(2025, 10, 1), LocalDate.of(2025, 11, 1), LocalDate.of(2025, 12, 1)),
        dates);
  }

  @Test
  void ignore_les_flux_sans_piece_justificative() {
    var subject = new DecomposeurOperationPieceJustificative(finSimulation);

    FluxArgent flux = new FluxArgent("don", compte, LocalDate.now(), ariary(500000), null);

    OperationDecomposee actual = subject.decomposePossession(List.of(flux));

    assertEquals(0, actual.piecesJustificatives().size());
  }
}
