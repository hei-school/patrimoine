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
  void journal_content_at_least_two_ligne_ecriture() {
    var journal = JournalFactory.make(JN, "Journal", operations, Map.of());

    var actual = journal.ecritures().getFirst().lignes().size();

    assertEquals(2, actual);
  }
}
