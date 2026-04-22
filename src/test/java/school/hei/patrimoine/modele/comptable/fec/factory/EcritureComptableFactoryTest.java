package school.hei.patrimoine.modele.comptable.fec.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.comptable.fec.JournalCode.JN;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.comptable.fec.Journal;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.TransfertArgent;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

class EcritureComptableFactoryTest {
  private Journal journal;
  private Compte compteDebit;
  private Compte compteCredit;
  private PieceJustificative pj;

  @BeforeEach
  void setup() {
    journal = new Journal(JN, "Journal");
    compteCredit = new Compte("Créditeur", LocalDate.of(2025, 1, 31), ariary(5_000_000));
    compteDebit = new Compte("Débiteur", LocalDate.of(2026, 1, 1), ariary(100_000));
    pj = new PieceJustificative("Transfert Argent BFV", LocalDate.of(2026, 4, 5), "lien-facture");
  }

  @Test
  void should_create_complete_ecriture_comptable() {
    var transfert =
        new TransfertArgent(
            "Transfert Argent BFV",
            compteCredit,
            compteDebit,
            LocalDate.of(2026, 5, 3),
            ariary(1_000_000));
    var operation = new OperationComptable(transfert);
    var possession = (TransfertArgent) operation.getPossession();

    var ecriture = EcritureComptableFactory.make(journal, operation, pj);

    assertEquals(journal.getNextId(), ecriture.id());
    assertEquals(possession.t(), ecriture.date());
    assertEquals(possession.nom(), ecriture.libelle());
    assertEquals(possession.getFluxMensuel(), ecriture.valeur());
    assertEquals(pj, ecriture.pj());
    assertEquals(pj.date(), ecriture.dateValidation());
    assertEquals(2, ecriture.lignes().size());
  }

  @Test
  void pj_could_be_null() {
    var flux = new FluxArgent("Versement", compteDebit, LocalDate.of(2026, 2, 14), ariary(300_000));
    var operation = new OperationComptable(flux);

    var ecriture = EcritureComptableFactory.make(journal, operation, null);

    assertNull(ecriture.pj());
    assertNull(ecriture.dateValidation());
    assertEquals(2, ecriture.lignes().size());
  }
}
