package school.hei.patrimoine.modele.recouppement.decomposeur;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

class PieceJustificativeDecomposeurTest {
  PieceJustificativeDecomposeur subject = new PieceJustificativeDecomposeur(LocalDate.now());

  @Test
  void decompose_piece_justificative_d_une_seule_date() {
    var pieceJustificative =
        new PieceJustificative(
            "assuranceVoiture", LocalDate.now(), "http://example.com/facture-001.pdf");

    var actual = subject.apply(pieceJustificative);
    System.out.println(actual);

    assertEquals(1, actual.size());
    assertTrue(actual.getFirst().id().contains("assuranceVoiture"));
    assertEquals(LocalDate.now(), actual.getFirst().date());
  }

  @Test
  void decompose_piece_justificative_sur_trois_mois() {
    var pieceJustificative =
        new PieceJustificative(
            "loyerAppartement", LocalDate.of(2025, 10, 1), "http://example.com/facture-loyer.pdf");

    var actual = subject.apply(pieceJustificative);

    assertEquals(4, actual.size());
    assertTrue(actual.getFirst().id().contains("loyerAppartement__du_2025_10_01"));
    assertEquals(LocalDate.of(2025, 10, 1), actual.getFirst().date());
    assertTrue(actual.get(1).id().contains("loyerAppartement__du_2025_11_01"));
    assertEquals(LocalDate.of(2025, 11, 1), actual.get(1).date());
    assertTrue(actual.get(2).id().contains("loyerAppartement__du_2025_12_01"));
    assertEquals(LocalDate.of(2025, 12, 1), actual.get(2).date());
    assertTrue(actual.get(3).id().contains("loyerAppartement__du_2026_01_01"));
    assertEquals(LocalDate.of(2026, 1, 1), actual.get(3).date());
  }
}
