package school.hei.patrimoine.modele.fec.factory;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.fec.JournalCode.JN;

import java.util.Map;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.fec.Journal;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;

class EcritureComptableFactoryTest {

  @Test
  void ecriture_comptable_should_content_at_least_two_ligne_ecriture() {
    var compte = new Compte("Compte principal", now(), Argent.ariary(0));
    var possession = new FluxArgent("Achats", compte, now(), now(), 1, Argent.ariary(4000));
    var operation = OperationComptable.make(possession);
    var journal = new Journal(JN, "Journal");

    var actual = EcritureComptableFactory.make(journal, operation, Map.of(), 1);

    assertEquals(2, actual.lignes().size());
  }

  @Test
  void should_match() {
    var compte = new Compte("Compte principal", now(), Argent.ariary(0));
    var possession = new FluxArgent("Achats", compte, now(), now(), 1, Argent.ariary(4000));
    var operation = OperationComptable.make(possession);
    var journal = new Journal(JN, "Journal");

    var actual = EcritureComptableFactory.make(journal, operation, Map.of(), 1);

    assertEquals("JN001", actual.id());
    assertEquals(possession.nom(), actual.libelle());
    assertEquals(possession.t(), actual.date());
    assertNull(actual.dateValidation()); // not implemented yet
  }
}
