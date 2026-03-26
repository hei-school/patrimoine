package school.hei.patrimoine.modele.comptable.fec;

import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.comptable.fec.JournalCode.JN;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

class JournalTest {
  private Journal subject;
  private static final Compte COMPTE =
      new Compte("Epargne", LocalDate.of(2024, 6, 9), ariary(200_000));
  private static final FluxArgent FLUX =
      new FluxArgent(
          "Entrée",
          COMPTE,
          LocalDate.of(2026, 1, 20),
          LocalDate.of(2026, 1, 20),
          6,
          ariary(30_000));
  private static final OperationComptable OPERATION = new OperationComptable(FLUX);
  private static final PieceJustificative PJ =
      new PieceJustificative("FAC251229", LocalDate.of(2025, 12, 29), "link1");

  @BeforeEach
  void setup() {
    subject = new Journal(JN, "Journal");
  }

  @Test
  void journal_initial_state_is_correct() {
    assertTrue(subject.ecritures().isEmpty(), "Journal should be empty when created");
    assertEquals(JN, subject.code());
    assertEquals("Journal", subject.libelle());
  }

  @Test
  void add_ecriture_to_a_journal() {
    subject.addEcriture(OPERATION, PJ);
    subject.addEcriture(OPERATION, PJ);
    subject.addEcriture(OPERATION, PJ);

    assertEquals(3, subject.ecritures().size());
  }

  @Test
  void ecriture_ids_should_be_sequential() {
    subject.addEcriture(OPERATION, PJ);
    subject.addEcriture(OPERATION, PJ);
    subject.addEcriture(OPERATION, PJ);
    var actual = subject.ecritures();

    assertEquals("JN000", actual.get(0).id());
    assertEquals("JN001", actual.get(1).id());
    assertEquals("JN002", actual.get(2).id());
  }

  @Test
  void ecriture_of_journal_should_contain_at_least_two_ligne_ecriture() {
    subject.addEcriture(OPERATION, PJ);
    var actual = subject.ecritures().getFirst().lignes().size();

    assertEquals(2, actual);
  }
}
