package school.hei.patrimoine.modele.fec.factory;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.fec.JournalCode.JN;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Possession;

class JournalFactoryTest {
  private List<OperationComptable> operations;
  private final Possession possession = new Compte("Épargne", now(), Argent.ariary(500_000));

  @BeforeEach
  void setUp() {
    operations = List.of(OperationComptable.make(possession));
  }

  @Test
  void journal_contains_ecriture_comptable() {
    var journal = JournalFactory.make(JN, "Journal", operations, Map.of());

    var actual = journal.ecritures().isEmpty();

    assertFalse(actual);
  }

  @Test
  void journal_contains_at_least_two_ligne_ecriture() {
    var journal = JournalFactory.make(JN, "Journal", operations, Map.of());

    var actual = journal.ecritures().getFirst().lignes().size();

    assertEquals(2, actual);
  }

  @Test
  void pjs_could_be_null() {
    var journal = JournalFactory.make(JN, "Journal", operations, null);

    var actual = journal.ecritures().getFirst().lignes().size();

    assertEquals(2, actual);
  }

  @Test
  void empty_ecritures_if_operations_is_null() {
    var journal = JournalFactory.make(JN, "Journal", null, Map.of());

    var actual = journal.ecritures().isEmpty();

    assertTrue(actual);
  }
}
