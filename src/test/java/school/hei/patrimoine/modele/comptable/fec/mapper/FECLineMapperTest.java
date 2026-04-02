package school.hei.patrimoine.modele.comptable.fec.mapper;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.comptable.fec.JournalCode.JN;
import static school.hei.patrimoine.modele.comptable.fec.mapper.FECLineMapper.toFECLine;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.comptable.fec.factory.JournalFactory;
import school.hei.patrimoine.modele.possession.*;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

class FECLineMapperTest {
  private static final Compte COMPTE_EXPEDITEUR =
      new Compte("Compte expéditeur", LocalDate.of(2025, JANUARY, 31), ariary(5_000_000));
  private static final Compte COMPTE_DESTINATAIRE =
      new Compte("Compte destinataire", LocalDate.of(2026, JANUARY, 1), ariary(100_000));
  private static final TransfertArgent TRANSFERT =
      new TransfertArgent(
          "Transfert Argent BNI",
          COMPTE_EXPEDITEUR,
          COMPTE_DESTINATAIRE,
          LocalDate.of(2026, MAY, 3),
          ariary(1_000_000));
  private static final OperationComptable OPERATION = new OperationComptable(TRANSFERT);
  private static final PieceJustificative PJ =
      new PieceJustificative("Transfert Argent BNI", LocalDate.of(2026, APRIL, 5), "lien-facture");

  @Test
  void transfert_compte_num_est_virement_interne_580() {
    var journal =
        JournalFactory.make(JN, "Journal", Set.of(OPERATION), Map.of("Transfert Argent BNI", PJ));
    var ecriture = journal.ecritures().getFirst();
    var ligne = ecriture.lignes().getFirst();

    var fecLine = toFECLine(journal, ecriture, ligne);

    assertEquals("580", fecLine.values()[4]);
  }

  @Test
  void transfert_debiteur_est_vers_compte() {
    var journal =
        JournalFactory.make(JN, "Journal", Set.of(OPERATION), Map.of("Transfert Argent BNI", PJ));
    var ecriture = journal.ecritures().getFirst();
    var ligne = ecriture.lignes().getFirst();

    var fecLine = toFECLine(journal, ecriture, ligne);

    assertEquals("Compte destinataire", fecLine.values()[5]);
    assertFalse(fecLine.values()[11].isEmpty());
    assertTrue(fecLine.values()[12].isEmpty());
  }

  @Test
  void transfert_crediteur_est_depuis_compte() {
    var journal =
        JournalFactory.make(JN, "Journal", Set.of(OPERATION), Map.of("Transfert Argent BNI", PJ));
    var ecriture = journal.ecritures().getFirst();
    var ligne = ecriture.lignes().get(1);

    var fecLine = toFECLine(journal, ecriture, ligne);

    assertEquals("580", fecLine.values()[4]);
    assertEquals("Compte expéditeur", fecLine.values()[5]);
    assertTrue(fecLine.values()[11].isEmpty());
    assertFalse(fecLine.values()[12].isEmpty());
  }

  @Test
  void transfert_champs_pj_sont_correctement_remplis() {
    var journal =
        JournalFactory.make(JN, "Journal", Set.of(OPERATION), Map.of("Transfert Argent BNI", PJ));
    var ecriture = journal.ecritures().getFirst();
    var ligne = ecriture.lignes().getFirst();

    var fecLine = toFECLine(journal, ecriture, ligne);

    assertEquals("Transfert Argent BNI", fecLine.values()[8]);
    assertEquals("20260405", fecLine.values()[9]);
    assertEquals("20260405", fecLine.values()[15]);
  }

  @Test
  void flux_positif_compte_num_est_banque_512() {
    var flux =
        new FluxArgent(
            "Vente", COMPTE_EXPEDITEUR, LocalDate.of(2026, JANUARY, 10), ariary(200_000));
    var op = new OperationComptable(flux);
    var journal = JournalFactory.make(JN, "Journal", Set.of(op), Map.of());
    var ecriture = journal.ecritures().getFirst();
    var ligne = ecriture.lignes().getFirst();

    var fecLine = toFECLine(journal, ecriture, ligne);

    assertEquals("512", fecLine.values()[4]);
    assertFalse(fecLine.values()[11].isEmpty());
    assertTrue(fecLine.values()[12].isEmpty());
  }

  @Test
  void flux_positif_passif_compte_num_est_pca_487() {
    var flux =
        new FluxArgent(
            "Vente", COMPTE_EXPEDITEUR, LocalDate.of(2026, JANUARY, 10), ariary(200_000));
    var op = new OperationComptable(flux);
    var journal = JournalFactory.make(JN, "Journal", Set.of(op), Map.of());
    var ecriture = journal.ecritures().getFirst();
    var ligne = ecriture.lignes().get(1);

    var fecLine = toFECLine(journal, ecriture, ligne);

    assertEquals("487", fecLine.values()[4]);
    assertTrue(fecLine.values()[11].isEmpty());
    assertFalse(fecLine.values()[12].isEmpty());
  }

  @Test
  void flux_negatif_compte_num_est_cca_486() {
    var flux =
        new FluxArgent(
            "Loyer", COMPTE_EXPEDITEUR, LocalDate.of(2026, JANUARY, 10), ariary(-200_000));
    var op = new OperationComptable(flux);
    var journal = JournalFactory.make(JN, "Journal", Set.of(op), Map.of());
    var ecriture = journal.ecritures().getFirst();
    var ligne = ecriture.lignes().getFirst();

    var fecLine = toFECLine(journal, ecriture, ligne);

    assertEquals("486", fecLine.values()[4]);
    assertFalse(fecLine.values()[11].isEmpty());
    assertTrue(fecLine.values()[12].isEmpty());
  }

  @Test
  void flux_negatif_passif_compte_num_est_banque_512() {
    var flux =
        new FluxArgent(
            "Loyer", COMPTE_EXPEDITEUR, LocalDate.of(2026, JANUARY, 10), ariary(-200_000));
    var op = new OperationComptable(flux);
    var journal = JournalFactory.make(JN, "Journal", Set.of(op), Map.of());
    var ecriture = journal.ecritures().getFirst();
    var ligne = ecriture.lignes().get(1);

    var fecLine = toFECLine(journal, ecriture, ligne);

    assertEquals("512", fecLine.values()[4]);
  }

  @Test
  void achat_materiel_compte_num_est_materiel_2183() {
    var achat =
        new AchatMaterielAuComptant(
            "Ordinateur", LocalDate.of(2026, JANUARY, 10), ariary(500_000), 2.4, COMPTE_EXPEDITEUR);
    var op = new OperationComptable(achat);
    var journal = JournalFactory.make(JN, "Journal", Set.of(op), Map.of());
    var ecriture = journal.ecritures().getFirst();
    var ligne = ecriture.lignes().getFirst();

    var fecLine = toFECLine(journal, ecriture, ligne);

    assertEquals("2183", fecLine.values()[4]);
    assertFalse(fecLine.values()[11].isEmpty());
  }

  @Test
  void achat_materiel_passif_compte_num_est_banque_512() {
    var achat =
        new AchatMaterielAuComptant(
            "Ordinateur", LocalDate.of(2026, JANUARY, 10), ariary(500_000), 2.4, COMPTE_EXPEDITEUR);
    var op = new OperationComptable(achat);
    var journal = JournalFactory.make(JN, "Journal", Set.of(op), Map.of());
    var ecriture = journal.ecritures().getFirst();
    var ligne = ecriture.lignes().get(1);

    var fecLine = toFECLine(journal, ecriture, ligne);

    assertEquals("512", fecLine.values()[4]);
  }

  @Test
  void remboursement_dette_compte_num_est_164() {
    var dette = new Dette("Dette", LocalDate.of(2024, JULY, 1), ariary(-500_000));
    var creance = new Creance("Créance", LocalDate.of(2024, JULY, 1), ariary(500_000));
    var remboursement =
        new RemboursementDette(
            "Rembourserment",
            COMPTE_EXPEDITEUR,
            COMPTE_DESTINATAIRE,
            dette,
            creance,
            LocalDate.of(2026, JANUARY, 31),
            ariary(500_000));
    var op = new OperationComptable(remboursement);
    var journal = JournalFactory.make(JN, "Journal", Set.of(op), Map.of());
    var ecriture = journal.ecritures().getFirst();
    var ligne = ecriture.lignes().getFirst();

    var fecLine = toFECLine(journal, ecriture, ligne);

    assertEquals("164", fecLine.values()[4]);
  }
}
