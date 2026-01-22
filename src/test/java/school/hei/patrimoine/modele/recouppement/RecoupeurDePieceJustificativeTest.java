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
    var pj = new PieceJustificative("salaire", date, "https://example.com/salaire.pdf");

    var subject = new RecoupeurDePieceJustificative(Set.of(pj), Set.of(compte, salaire));

    var actual = subject.getPossessionWithPj();
    assertEquals(2, actual.size());

    var association =
        actual.stream().filter(a -> a.possession().equals(salaire)).findFirst().orElseThrow();
    assertEquals("salaire", association.possession().nom());
    assertEquals("salaire", association.pieceJustificative().id());
  }

  @Test
  void detecte_possession_sans_piece_justificative() {
    var date = LocalDate.of(2025, JANUARY, 1);

    var compte = new Compte("comptePersonnel", date, ariary(0));
    var salaire = new FluxArgent("salaire", compte, date, ariary(200));
    var subject = new RecoupeurDePieceJustificative(Set.of(), Set.of(compte, salaire));

    var actual = subject.getPossessionWithPj();
    assertEquals(2, actual.size());
    assertTrue(actual.stream().allMatch(a -> a.pieceJustificative() == null));
  }

  @Test
  void recoupe_apres_decomposition_des_possessions() {
    var au01Janvier2025 = LocalDate.of(2025, JANUARY, 1);
    var au02Mars2025 = LocalDate.of(2025, MARCH, 2);
    var compte = new Compte("comptePersonnel", au01Janvier2025, ariary(0));
    var flux = new FluxArgent("salaire", compte, au01Janvier2025, au02Mars2025, 1, ariary(200));
    var pj = new PieceJustificative("salaire", au01Janvier2025, "http://example.com/salaire.pdf");

    var subject = new RecoupeurDePieceJustificative(Set.of(pj), Set.of(compte, flux));

    var actual = subject.getPossessionWithPj();
    assertFalse(actual.isEmpty());
    assertTrue(actual.stream().anyMatch(a -> a.possession().equals(flux)));
  }

  @Test
  void getPossessionWithPj_associe_correctement() {
    var date = LocalDate.of(2025, JANUARY, 1);

    var compte = new Compte("comptePersonnel", date, ariary(0));
    var salaire = new FluxArgent("salaire", compte, date, ariary(200));
    var pj1 = new PieceJustificative("salaire", date, "http://example.com/salaire.pdf");
    var pj2 = new PieceJustificative("comptePersonnel", date, "http://example.com/compte.pdf");

    var subject = new RecoupeurDePieceJustificative(Set.of(pj1, pj2), Set.of(compte, salaire));

    Set<PossessionWithPieceJustificative<? extends Possession>> actual =
        subject.getPossessionWithPj();

    assertEquals(2, actual.size());

    var salaireMatched =
        actual.stream()
            .anyMatch(
                a ->
                    a.possession().equals(salaire)
                        && a.pieceJustificative() != null
                        && a.pieceJustificative().id().equals("salaire"));
    var compteMatched =
        actual.stream()
            .anyMatch(
                a ->
                    a.possession().equals(compte)
                        && a.pieceJustificative() != null
                        && a.pieceJustificative().id().equals("comptePersonnel"));

    assertTrue(salaireMatched, "FluxArgent devrait être associé à pj1");
    assertTrue(compteMatched, "Compte devrait être associé à pj2");
  }

  @Test
  void find_piece_for_possession_via_association() {
    var date = LocalDate.of(2025, JANUARY, 1);

    var compte = new Compte("comptePersonnel", date, ariary(0));
    var salaire = new FluxArgent("salaire", compte, date, ariary(200));

    var pj = new PieceJustificative("salaire", date, "http://example.com/salaire.pdf");

    var subject = new RecoupeurDePieceJustificative(Set.of(pj), Set.of(compte, salaire));

    var association =
        subject.getPossessionWithPj().stream()
            .filter(a -> a.possession().equals(salaire))
            .findFirst()
            .orElseThrow();

    assertNotNull(association.pieceJustificative());
    assertTrue(association.pieceJustificative().id().contains(pj.id()));
  }

  @Test
  void absent_piece_justificative_est_detectee() {
    var date = LocalDate.of(2025, JANUARY, 1);

    var compte = new Compte("comptePersonnel", date, ariary(0));

    var subject = new RecoupeurDePieceJustificative(Set.of(), Set.of(compte));

    var actual = subject.getPossessionsWithoutPj();

    assertEquals(1, actual.size());
    assertTrue(actual.stream().anyMatch(p -> p.nom().equals("comptePersonnel")));
  }

  @Test
  void getPossessionsWithoutPj_retourne_uniquement_les_possessions_sans_pj() {
    var date = LocalDate.of(2025, JANUARY, 1);

    var compte = new Compte("comptePersonnel", date, ariary(0));
    var salaire = new FluxArgent("salaire", compte, date, ariary(200));

    var pj = new PieceJustificative("salaire", date, "http://example.com/salaire.pdf");

    var subject = new RecoupeurDePieceJustificative(Set.of(pj), Set.of(compte, salaire));

    var actual = subject.getPossessionsWithoutPj();

    assertTrue(actual.stream().anyMatch(p -> p.nom().equals("comptePersonnel")));
  }

  @Test
  void getPossessionWithPj_ne_associe_pas_si_nom_different() {
    var date = LocalDate.of(2025, JANUARY, 1);

    var compte = new Compte("comptePersonnel", date, ariary(0));

    var pj = new PieceJustificative("salaire", date, "http://example.com/autre.pdf");

    var subject = new RecoupeurDePieceJustificative(Set.of(pj), Set.of(compte));

    var actual = subject.getPossessionWithPj();

    assertEquals(1, actual.size());
    assertTrue(actual.stream().allMatch(a -> a.pieceJustificative() == null));
  }
}
