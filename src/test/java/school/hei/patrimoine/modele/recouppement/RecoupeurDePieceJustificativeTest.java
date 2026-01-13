package school.hei.patrimoine.modele.recouppement;

import static java.time.Month.JANUARY;
import static java.time.Month.MARCH;
import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;

import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

class RecoupeurDePieceJustificativeTest {

  @Test
  void trouve_piece_justificative_pour_possession() {
    var date = LocalDate.of(2025, JANUARY, 1);

    var compte = new Compte("comptePersonnel", date, ariary(0));
    var salaire = new FluxArgent("salaire", compte, date, ariary(200));

    var pj = new PieceJustificative("salaire", date, "http://example.com/salaire.pdf");

    var subject =
        new RecoupeurDePieceJustificative(
            Set.of(pj), Set.of(compte, salaire), LocalDate.of(2026, JANUARY, 9));

    var actual = subject.getPossessionWithPj();

    assertEquals(13, actual.size());

    var association = actual.iterator().next();
    assertEquals("salaire", association.possession().nom());
    assertEquals("salaire__du_2025_12_01", association.pieceJustificative().id());
  }

  @Test
  void detecte_possession_sans_piece_justificative() {
    var date = LocalDate.of(2025, JANUARY, 1);

    var compte = new Compte("comptePersonnel", date, ariary(0));
    var salaire = new FluxArgent("salaire", compte, date, ariary(200));

    var subject =
        new RecoupeurDePieceJustificative(Set.of(), Set.of(compte, salaire), LocalDate.now());

    var actual = subject.getPossessionWithPj();

    assertTrue(actual.isEmpty());
  }

  @Test
  void recoupe_apres_decomposition_des_possessions() {
    var au01Janvier2025 = LocalDate.of(2025, JANUARY, 1);
    var au02Mars2025 = LocalDate.of(2025, MARCH, 2);

    var compte = new Compte("comptePersonnel", au01Janvier2025, ariary(0));

    var flux = new FluxArgent("salaire", compte, au01Janvier2025, au02Mars2025, 1, ariary(200));

    var pj = new PieceJustificative("salaire", au01Janvier2025, "http://example.com/salaire.pdf");

    var subject =
        new RecoupeurDePieceJustificative(Set.of(pj), Set.of(compte, flux), LocalDate.now());

    var actual = subject.getPossessionWithPj();

    assertFalse(actual.isEmpty());
  }

  @Test
  void getPossessionWithPj_associe_correctement() {
    var date = LocalDate.of(2025, JANUARY, 1);

    var compte = new Compte("comptePersonnel", date, ariary(0));
    var salaire = new FluxArgent("salaire", compte, date, ariary(200));

    var pj1 = new PieceJustificative("salaire", date, "http://example.com/salaire.pdf");

    var pj2 = new PieceJustificative("comptePersonnel", date, "http://example.com/compte.pdf");

    var subject =
        new RecoupeurDePieceJustificative(
            Set.of(pj1, pj2), Set.of(compte, salaire), LocalDate.of(2026, JANUARY, 9));

    Set<PossessionWithPieceJustificative<? extends Possession>> actual =
        subject.getPossessionWithPj();

    assertEquals(26, actual.size());

    var salaireMatched =
        actual.stream()
            .anyMatch(
                a ->
                    a.possession().equals(salaire)
                        && a.pieceJustificative().id().startsWith("salaire__du_"));
    var compteMatched =
        actual.stream()
            .anyMatch(
                a ->
                    a.possession().equals(compte)
                        && a.pieceJustificative().id().startsWith("comptePersonnel__du_"));

    assertTrue(salaireMatched, "FluxArgent devrait être associé à pj1");
    assertTrue(compteMatched, "Compte devrait être associé à pj2");
  }

  @Test
  void getPieceJustificativeFor_retourne_correctement() {
    var date = LocalDate.of(2025, JANUARY, 1);

    var compte = new Compte("comptePersonnel", date, ariary(0));
    var salaire = new FluxArgent("salaire", compte, date, ariary(200));

    var pj = new PieceJustificative("salaire", date, "http://example.com/salaire.pdf");

    var subject =
        new RecoupeurDePieceJustificative(Set.of(pj), Set.of(compte, salaire), LocalDate.now());

    var actual = subject.getPieceJustificativeFor(salaire);
    assertTrue(actual.id().contains(pj.id()));
  }

  @Test
  void getPieceJustificativeFor_lance_exception_si_absent() {
    var date = LocalDate.of(2025, JANUARY, 1);

    var compte = new Compte("comptePersonnel", date, ariary(0));

    var subject = new RecoupeurDePieceJustificative(Set.of(), Set.of(compte), LocalDate.now());

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class, () -> subject.getPieceJustificativeFor(compte));

    assertTrue(exception.getMessage().contains("No PieceJustificative found for possession"));
  }
}
