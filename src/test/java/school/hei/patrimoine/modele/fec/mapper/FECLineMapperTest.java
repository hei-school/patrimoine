package school.hei.patrimoine.modele.fec.mapper;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.fec.JournalCode.JN;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.fec.Journal;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;

class FECLineMapperTest {
  private Journal journal;

  @BeforeEach
  void setUp() {
    journal = new Journal(JN, "Journal");
  }

  @Test
  void flux_positif_compteNum_est_banque_001() {
    var compte = new Compte("Compte principal", now(), ariary(0));
    var flux = new FluxArgent("Vente", compte, now(), ariary(50_000));
    var op = OperationComptable.make(flux);

    journal.addEcriture(op, null);
    var ligne = journal.ecritures().getFirst().lignes().getFirst();

    var fecLine = FECLineMapper.toFECLine(journal, journal.ecritures().getFirst(), ligne);

    assertEquals("JN", fecLine.values().get(0));
    assertEquals("Journal", fecLine.values().get(1));
    assertEquals("JN000", fecLine.values().get(2));
    assertEquals("512001", fecLine.values().get(4));
    assertEquals("Vente", fecLine.values().get(10));
    assertEquals("", fecLine.values().get(15));
  }

  @Test
  void flux_negatif_compteNum_actif_est_cca_001() {
    var compte = new Compte("Compte principal", now(), ariary(0));
    var flux = new FluxArgent("Loyer", compte, now(), ariary(-50_000));
    var op = OperationComptable.make(flux);

    journal.addEcriture(op, null);
    var ligne = journal.ecritures().getFirst().lignes().getFirst();

    var fecLine = FECLineMapper.toFECLine(journal, journal.ecritures().getFirst(), ligne);

    assertEquals("486001", fecLine.values().get(4));
  }

  @Test
  void deux_comptes_meme_type_ont_sequences_differentes() {
    var compteA = new Compte("Compte A", now(), ariary(0));
    var compteB = new Compte("Compte B", now(), ariary(0));
    var fluxA = new FluxArgent("Vente A", compteA, now(), ariary(50_000));
    var fluxB = new FluxArgent("Vente B", compteB, now(), ariary(50_000));

    journal.addEcriture(OperationComptable.make(fluxA), null);
    journal.addEcriture(OperationComptable.make(fluxB), null);

    var ligneA = journal.ecritures().get(0).lignes().getFirst();
    var ligneB = journal.ecritures().get(1).lignes().getFirst();

    var fecLineA = FECLineMapper.toFECLine(journal, journal.ecritures().get(0), ligneA);
    var fecLineB = FECLineMapper.toFECLine(journal, journal.ecritures().get(1), ligneB);

    assertEquals("512001", fecLineA.values().get(4));
    assertEquals("512002", fecLineB.values().get(4));
  }

  @Test
  void meme_compte_garde_son_numero_fixe() {
    var compte = new Compte("Compte principal", now(), ariary(0));
    var flux1 = new FluxArgent("Vente 1", compte, now(), ariary(50_000));
    var flux2 = new FluxArgent("Vente 2", compte, now(), ariary(30_000));

    journal.addEcriture(OperationComptable.make(flux1), null);
    journal.addEcriture(OperationComptable.make(flux2), null);

    var ligne1 = journal.ecritures().get(0).lignes().getFirst();
    var ligne2 = journal.ecritures().get(1).lignes().getFirst();

    var fecLine1 = FECLineMapper.toFECLine(journal, journal.ecritures().get(0), ligne1);
    var fecLine2 = FECLineMapper.toFECLine(journal, journal.ecritures().get(1), ligne2);

    assertEquals(fecLine1.values().get(4), fecLine2.values().get(4));
  }

  @Test
  void journal_code_et_libelle_sont_corrects() {
    var compte = new Compte("Compte principal", now(), ariary(0));
    var flux = new FluxArgent("Vente", compte, now(), ariary(50_000));
    journal.addEcriture(OperationComptable.make(flux), null);

    var ligne = journal.ecritures().getFirst().lignes().getFirst();
    var fecLine = FECLineMapper.toFECLine(journal, journal.ecritures().getFirst(), ligne);

    assertEquals("JN", fecLine.values().get(0));
    assertEquals("Journal", fecLine.values().get(1));
  }

  @Test
  void date_ecriture_est_correctement_formatee() {
    var compte = new Compte("Compte principal", now(), ariary(0));
    var flux = new FluxArgent("Vente", compte, LocalDate.of(2025, 1, 15), ariary(50_000));
    journal.addEcriture(OperationComptable.make(flux), null);

    var ligne = journal.ecritures().getFirst().lignes().getFirst();
    var fecLine = FECLineMapper.toFECLine(journal, journal.ecritures().getFirst(), ligne);

    assertEquals("20250115", fecLine.values().get(3));
  }

  @Test
  void flux_positif_montant_est_en_debit() {
    var compte = new Compte("Compte principal", now(), ariary(0));
    var flux = new FluxArgent("Vente", compte, now(), ariary(50_000));
    journal.addEcriture(OperationComptable.make(flux), null);

    var ligne = journal.ecritures().getFirst().lignes().getFirst();
    var fecLine = FECLineMapper.toFECLine(journal, journal.ecritures().getFirst(), ligne);

    assertFalse(fecLine.values().get(11).isEmpty());
    assertTrue(fecLine.values().get(12).isEmpty());
  }

  @Test
  void sans_pj_les_champs_pj_sont_vides() {
    var compte = new Compte("Compte principal", now(), ariary(0));
    var flux = new FluxArgent("Vente", compte, now(), ariary(50_000));
    journal.addEcriture(OperationComptable.make(flux), null);

    var ligne = journal.ecritures().getFirst().lignes().getFirst();
    var fecLine = FECLineMapper.toFECLine(journal, journal.ecritures().getFirst(), ligne);

    assertEquals("", fecLine.values().get(8));
    assertEquals("", fecLine.values().get(9));
    assertEquals("", fecLine.values().get(15));
  }

  @Test
  void sans_compte_auxiliaire_les_champs_compAux_sont_vides() {
    var compte = new Compte("Compte principal", now(), ariary(0));
    var flux = new FluxArgent("Vente", compte, now(), ariary(50_000));
    journal.addEcriture(OperationComptable.make(flux), null);

    var ligne = journal.ecritures().getFirst().lignes().getFirst();
    var fecLine = FECLineMapper.toFECLine(journal, journal.ecritures().getFirst(), ligne);

    assertEquals("", fecLine.values().get(6));
    assertEquals("", fecLine.values().get(7));
  }

  @Test
  void devise_finale_est_mga() {
    var compte = new Compte("Compte principal", now(), ariary(0));
    var flux = new FluxArgent("Vente", compte, now(), ariary(50_000));
    journal.addEcriture(OperationComptable.make(flux), null);

    var ligne = journal.ecritures().getFirst().lignes().getFirst();
    var fecLine = FECLineMapper.toFECLine(journal, journal.ecritures().getFirst(), ligne);

    assertEquals("MGA", fecLine.values().get(17));
  }

  @Test
  void nom_compte_est_correct() {
    var compte = new Compte("Mon compte test", now(), ariary(0));
    var flux = new FluxArgent("Vente", compte, now(), ariary(50_000));
    journal.addEcriture(OperationComptable.make(flux), null);

    var ligne = journal.ecritures().getFirst().lignes().getFirst();
    var fecLine = FECLineMapper.toFECLine(journal, journal.ecritures().getFirst(), ligne);

    assertEquals("Mon compte test", fecLine.values().get(5));
  }
}
