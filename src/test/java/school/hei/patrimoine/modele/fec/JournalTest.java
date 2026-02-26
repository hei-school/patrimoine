package school.hei.patrimoine.modele.fec;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.fec.JournalCode.JN;

import java.util.Map;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;

class JournalTest {
  private final Journal subject = new Journal(JN, "Journal");

  @Test
  void ecriture_of_journal_should_content_at_least_two_ligne_ecriture() {
    var compte = new Compte("Compte principal", now(), Argent.ariary(0));
    var possession = new FluxArgent("Achats", compte, now(), now(), 1, Argent.ariary(4000));
    var operation = OperationComptable.make(possession);

    var actual = subject.addEcriture(operation, Map.of());

    assertEquals(2, actual.ecritures().getFirst().lignes().size());
  }
}
