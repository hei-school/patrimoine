package school.hei.patrimoine.modele.comptable.fec.factory;

import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.comptable.fec.JournalCode.JN;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.TransfertArgent;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

class JournalFactoryTest {
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

  @Test
  void create_a_journal() {
    var journal =
        JournalFactory.make(JN, "Journal", List.of(OPERATION), Map.of("Transfert Argent BFV", PJ));

    assertEquals(JN, journal.code());
    assertEquals("Journal", journal.libelle());
    assertFalse(journal.ecritures().isEmpty());
  }

  @Test
  void a_journal_should_contain_at_least_two_ligne_ecriture() {
    var journal =
        JournalFactory.make(JN, "Journal", List.of(OPERATION), Map.of("Transfert Argent BFV", PJ));
    var lignes = journal.ecritures().getFirst().lignes();

    assertEquals(2, lignes.size());
  }
}
