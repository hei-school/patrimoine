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
  private Compte compte;
  private Journal subject;
  private PieceJustificative pj;

  @BeforeEach
  void setup() {
    subject = new Journal(JN, "Journal");
    pj = new PieceJustificative("FAC251229", LocalDate.of(2025, 12, 29), "link1");
    compte = new Compte("Epargne", LocalDate.of(2024, 6, 9), ariary(200_000));
  }

  @Test
  void journal_initial_state_is_correct() {
    assertTrue(subject.ecritures().isEmpty(), "Journal should be empty when created");
    assertEquals(JN, subject.code());
    assertEquals("Journal", subject.libelle());
  }

  @Test
  void add_ecriture_to_a_journal() {
    var flux =
        new FluxArgent(
            "Entrée",
            compte,
            LocalDate.of(2026, 1, 20),
            LocalDate.of(2026, 1, 20),
            6,
            ariary(30_000));
    var optionalOperation = OperationComptable.of(flux);
    var operation = optionalOperation.orElseThrow();

    subject.addEcriture(operation, pj);
    subject.addEcriture(operation, pj);
    subject.addEcriture(operation, pj);

    assertEquals(3, subject.ecritures().size());
  }

  @Test
  void ecriture_ids_should_be_sequential() {
    var flux =
        new FluxArgent(
            "Entrée",
            compte,
            LocalDate.of(2026, 1, 20),
            LocalDate.of(2026, 1, 20),
            6,
            ariary(30_000));
    var optionalOperation = OperationComptable.of(flux);
    var operation = optionalOperation.orElseThrow();

    subject.addEcriture(operation, pj);
    subject.addEcriture(operation, pj);
    subject.addEcriture(operation, pj);
    var actual = subject.ecritures();

    assertEquals("JN000", actual.get(0).id());
    assertEquals("JN001", actual.get(1).id());
    assertEquals("JN002", actual.get(2).id());
  }

  @Test
  void ecriture_should_contain_at_least_two_ligne_ecriture() {
    var flux =
        new FluxArgent(
            "Entrée",
            compte,
            LocalDate.of(2026, 1, 20),
            LocalDate.of(2026, 1, 20),
            6,
            ariary(30_000));
    var optionalOperation = OperationComptable.of(flux);
    var operation = optionalOperation.orElseThrow();

    subject.addEcriture(operation, pj);
    var actual = subject.ecritures().getFirst().lignes().size();

    assertEquals(2, actual);
  }
}
