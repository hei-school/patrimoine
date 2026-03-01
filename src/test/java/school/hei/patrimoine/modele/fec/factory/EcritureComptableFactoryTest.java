package school.hei.patrimoine.modele.fec.factory;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.fec.JournalCode.JN;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.fec.Journal;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;

class EcritureComptableFactoryTest {
  private FluxArgent flux;
  private OperationComptable operation;
  private Journal journal;

  @BeforeEach
  void setUp() {
    var compte = new Compte("Compte principal", now(), ariary(0));
    flux = new FluxArgent("Achats", compte, now(), now(), 1, ariary(4000));
    operation = OperationComptable.make(flux);
    journal = new Journal(JN, "Journal");
  }

  @Test
  void ecriture_comptable_should_contain_at_least_two_ligne_ecriture() {
    var actual = EcritureComptableFactory.make(journal, operation, null);

    assertEquals(2, actual.lignes().size());
  }

  @Test
  void should_match_with_the_possession_infos() {
    var actual = EcritureComptableFactory.make(journal, operation, null);

    assertEquals("JN000", actual.id());
    assertEquals(flux.nom(), actual.libelle());
    assertEquals(flux.t(), actual.date());
  }
}
