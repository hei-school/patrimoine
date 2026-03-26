package school.hei.patrimoine.modele.comptable.fec.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.comptable.fec.JournalCode.JN;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.comptable.fec.Journal;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.TransfertArgent;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

class EcritureComptableFactoryTest {
  private static final Compte COMPTE_EXPEDITEUR =
      new Compte("Compte expéditeur", LocalDate.of(2025, 1, 31), ariary(5_000_000));
  private static final Compte COMPTE_DESTINATAIRE =
      new Compte("Compte destinataire", LocalDate.of(2026, 1, 1), ariary(100_000));
  private static final TransfertArgent TRANSFERT =
      new TransfertArgent(
          "Transfert Argent BFV",
          COMPTE_EXPEDITEUR,
          COMPTE_DESTINATAIRE,
          LocalDate.of(2026, 5, 3),
          ariary(1_000_000));
  private static final OperationComptable OPERATION = new OperationComptable(TRANSFERT);
  private static final PieceJustificative PJ =
      new PieceJustificative("Transfert Argent BFV", LocalDate.of(2026, 4, 5), "lien-facture");
  private static final Journal JOURNAL = new Journal(JN, "Journal");

  @Test
  void should_create_complete_ecriture_comptable() {
    var ecriture = EcritureComptableFactory.make(JOURNAL, OPERATION, PJ);

    assertEquals(JOURNAL.getNextId(), ecriture.id());
    assertEquals(OPERATION.getPossession().t(), ecriture.date());
    assertEquals(OPERATION.getPossession().nom(), ecriture.libelle());
    assertEquals(PJ, ecriture.pj());
    assertEquals(PJ.date(), ecriture.dateValidation());
    assertEquals(2, ecriture.lignes().size());
  }

  @Test
  void should_create_ecriture_without_pj() {
    var ecriture = EcritureComptableFactory.make(JOURNAL, OPERATION, null);

    assertNull(ecriture.pj());
    assertNull(ecriture.dateValidation());
    assertEquals(2, ecriture.lignes().size());
  }
}
