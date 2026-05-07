package school.hei.patrimoine.modele.comptable.fec.factory;

import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.comptable.fec.JournalCode.JN;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.TransfertArgent;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

class FECFactoryTest {
  private PieceJustificative pj;
  private Compte compteExpediteur;
  private Compte compteDestinataire;

  @BeforeEach
  void setup() {
    pj =
        new PieceJustificative(
            "Transfert Argent BFV", LocalDate.of(2026, 4, 5), "FAC0001", "lien-facture");
    compteExpediteur =
        new Compte("Compte expéditeur", LocalDate.of(2025, 1, 31), ariary(5_000_000));
    compteDestinataire =
        new Compte("Compte destinataire", LocalDate.of(2026, 1, 1), ariary(100_000));
  }

  @Test
  void create_FEC_with_journals() {
    var transfert =
        new TransfertArgent(
            "Transfert Argent BFV",
            compteExpediteur,
            compteDestinataire,
            LocalDate.of(2026, 5, 3),
            ariary(1_000_000));
    var optionalOperation = OperationComptable.of(transfert);
    var operation = optionalOperation.orElseThrow();

    var fec = FECFactory.make(List.of(operation), Map.of("Transfert Argent BFV", pj));
    var journal = fec.journals().getFirst();

    assertEquals(1, fec.journals().size());
    assertEquals(JN, journal.code());
    assertEquals("Journal", journal.libelle());
    assertEquals(1, journal.ecritures().size());
  }
}
