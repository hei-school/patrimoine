package school.hei.patrimoine.modele.comptable.fec.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.comptable.fec.mapper.FECLineMapper.toFECLine;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.comptable.fec.JournalCode;
import school.hei.patrimoine.modele.comptable.fec.factory.JournalFactory;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.TransfertArgent;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

class FECLineMapperTest {
  private static final Compte COMPTE_EXPEDITEUR =
      new Compte("Compte expéditeur", LocalDate.of(2025, 1, 31), ariary(5_000_000));
  private static final Compte COMPTE_DESTINATAIRE =
      new Compte("Compte destinataire", LocalDate.of(2026, 1, 1), ariary(100_000));
  private static final TransfertArgent TRANSFERT =
      new TransfertArgent(
          "Transfert Argent BNI",
          COMPTE_EXPEDITEUR,
          COMPTE_DESTINATAIRE,
          LocalDate.of(2026, 5, 3),
          ariary(1_000_000));
  private static final OperationComptable OPERATION = new OperationComptable(TRANSFERT);
  private static final PieceJustificative PJ =
      new PieceJustificative("Transfert Argent BNI", LocalDate.of(2026, 4, 5), "lien-facture");

  @Test
  void values_should_match() {
    var journal =
        JournalFactory.make(
            JournalCode.JN, "Journal", Set.of(OPERATION), Map.of("Transfert Argent BNI", PJ));
    var ecriture = journal.ecritures().getFirst();
    var ligne = ecriture.lignes().getFirst();

    var fecLine = toFECLine(journal, ecriture, ligne);
    var values = fecLine.values();

    assertEquals("JN", values[0], "JournalCode");
    assertEquals("Journal", values[1], "JournalLib");
    assertEquals("JN000", values[2], "EcritureNum");
    assertEquals("20260503", values[3], "EcritureDate");
    assertEquals("CCA", values[4], "CompteNum");
    assertEquals("Compte destinataire", values[5], "CompteLib");
    assertEquals("", values[6], "CompAuxNum");
    assertEquals("", values[7], "CompAuxLib");
    assertEquals("Transfert Argent BNI", values[8], "PieceRef");
    assertEquals("20260405", values[9], "PieceDate");
    assertEquals("Transfert Argent BNI", values[10], "EcritureLib");
    assertEquals("19.72", values[11], "Debit");
    assertEquals("", values[12], "Credit");
    assertEquals("", values[13], "EcritureLet");
    assertEquals("", values[14], "DateLet");
    assertEquals("20260405", values[15], "ValidDate");
    assertEquals("100000.00", values[16], "Montantdevise");
    assertEquals("MGA", values[17], "Idevise");
  }
}
