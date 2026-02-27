package school.hei.patrimoine.modele.fec;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.fec.JournalCode.JN;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;

class JournalTest {
  private Journal subject;

  @BeforeEach
  void setUp() {
    subject = new Journal(JN, "Journal");
  }

  @Test
  void ecriture_of_journal_should_contain_at_least_two_ligne_ecriture() {
    var compte = new Compte("Compte principal", now(), Argent.ariary(0));
    var possession = new FluxArgent("Achats", compte, now(), now(), 1, Argent.ariary(4000));
    var operation = OperationComptable.make(possession);

    subject.addEcriture(operation, null);
    var actual = subject.ecritures().getFirst().lignes().size();

    assertEquals(2, actual);
  }

  @Test
  void ecriture_ids_should_be_sequential() {
    var compte = new Compte("Compte principal", now(), Argent.ariary(0));
    var possession = new FluxArgent("Achats", compte, now(), now(), 1, Argent.ariary(4000));
    var operation = OperationComptable.make(possession);

    subject.addEcriture(operation, null);
    subject.addEcriture(operation, null);
    subject.addEcriture(operation, null);
    var actual = subject.ecritures();

    assertEquals(3, actual.size());
    assertEquals("JN000", actual.getFirst().id());
    assertEquals("JN001", actual.get(1).id());
    assertEquals("JN002", actual.get(2).id());
  }
}
