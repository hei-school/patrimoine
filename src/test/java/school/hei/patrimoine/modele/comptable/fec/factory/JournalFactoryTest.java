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

class JournalFactoryTest {
  private PieceJustificative pj;
  private Compte compteExpediteur;
  private Compte compteDestinataire;

  @BeforeEach
  void setup() {
    pj = new PieceJustificative("Transfert Argent BFV", LocalDate.of(2026, 4, 5), "lien-facture");
    compteExpediteur =
        new Compte("Compte expéditeur", LocalDate.of(2025, 1, 31), ariary(5_000_000));
    compteDestinataire =
        new Compte("Compte destinataire", LocalDate.of(2026, 1, 1), ariary(100_000));
  }

  @Test
  void create_a_journal() {
    var tranfert =
        new TransfertArgent(
            "Transfert Argent BFV",
            compteExpediteur,
            compteDestinataire,
            LocalDate.of(2026, 5, 3),
            ariary(1_000_000));
    var operation = new OperationComptable(tranfert);

    var journal =
        JournalFactory.make(JN, "Journal", List.of(operation), Map.of("Transfert Argent BFV", pj));

    assertEquals(JN, journal.code());
    assertEquals("Journal", journal.libelle());
    assertFalse(journal.ecritures().isEmpty());
  }

  @Test
  void a_journal_should_contain_at_least_two_ligne_ecriture() {
    var tranfert =
        new TransfertArgent(
            "Transfert Argent BFV",
            compteExpediteur,
            compteDestinataire,
            LocalDate.of(2026, 5, 3),
            ariary(1_000_000));
    var operation = new OperationComptable(tranfert);

    var journal =
        JournalFactory.make(JN, "Journal", List.of(operation), Map.of("Transfert Argent BFV", pj));
    var lignes = journal.ecritures().getFirst().lignes();

    assertEquals(2, lignes.size());
  }
}
