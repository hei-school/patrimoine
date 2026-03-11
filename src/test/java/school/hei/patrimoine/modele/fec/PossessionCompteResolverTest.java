package school.hei.patrimoine.modele.fec;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.comptable.TypeComptable.*;
import static school.hei.patrimoine.modele.fec.PossessionCompteResolver.resolve;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.*;

class PossessionCompteResolverTest {
  private static final Argent ZERO = ariary(0);
  private static final Argent POSITIF = ariary(500_000);
  private static final Argent NEGATIF = ariary(-500_000);
  private static final Compte COMPTE_A = new Compte("Compte A", now(), ZERO);
  private static final Compte COMPTE_B = new Compte("Compte B", now(), ZERO);

  @Test
  void flux_positif_actif_est_banque_passif_est_pca() {
    var flux = new FluxArgent("Vente", COMPTE_A, now(), POSITIF);
    var comptes = resolve(flux);

    assertEquals(BANQUE, comptes.compteDébiteur().typeComptable());
    assertEquals(PCA, comptes.compteCréditeur().typeComptable());
  }

  @Test
  void flux_negatif_actif_est_cca_passif_est_banque() {
    var flux = new FluxArgent("Charge", COMPTE_A, now(), NEGATIF);
    var comptes = resolve(flux);

    assertEquals(CCA, comptes.compteDébiteur().typeComptable());
    assertEquals(BANQUE, comptes.compteCréditeur().typeComptable());
  }

  @Test
  void transfert_actif_et_passif_sont_virement_interne() {
    var transfert = new TransfertArgent("Transfert", COMPTE_A, COMPTE_B, now(), POSITIF);
    var comptes = resolve(transfert);

    assertEquals(VIREMENT_INTERNE, comptes.compteDébiteur().typeComptable());
    assertEquals(VIREMENT_INTERNE, comptes.compteCréditeur().typeComptable());
  }

  @Test
  void compte_actif_et_passif_sont_banque() {
    var comptes = resolve(COMPTE_A);

    assertEquals(BANQUE, comptes.compteDébiteur().typeComptable());
    assertEquals(BANQUE, comptes.compteCréditeur().typeComptable());
  }

  @Test
  void achat_materiel_actif_est_materiel_passif_est_banque() {
    var achat = new AchatMaterielAuComptant("Ordinateur", now(), POSITIF, 0.1, COMPTE_A);
    var comptes = resolve(achat);

    assertEquals(MATERIEL, comptes.compteDébiteur().typeComptable());
    assertEquals(BANQUE, comptes.compteCréditeur().typeComptable());
  }

  @Test
  void remboursement_actif_est_remboursement_dette_passif_est_banque() {
    var dette = new Dette("Dette", now(), ariary(-100_000));
    var creance = new Creance("Créance", now(), ariary(100_000));
    var remboursement =
        new RemboursementDette(
            "Remboursement", COMPTE_A, COMPTE_B, dette, creance, now(), ariary(100_000));
    var comptes = resolve(remboursement);

    assertEquals(REMBOURSEMENT_DETTE, comptes.compteDébiteur().typeComptable());
    assertEquals(BANQUE, comptes.compteCréditeur().typeComptable());
  }

  @Test
  void materiel_throws_illegal_argument() {
    var materiel = new Materiel("Ordi", now(), now(), POSITIF, 10);
    assertThrows(IllegalArgumentException.class, () -> resolve(materiel));
  }

  @Test
  void correction_throws_illegal_argument() {
    var correction = new Correction(COMPTE_A, "Correction", now(), POSITIF);
    assertThrows(IllegalArgumentException.class, () -> resolve(correction));
  }

  @Test
  void personneMorale_throws_illegal_argument() {
    var personneMorale = new PersonneMorale("Société");
    assertThrows(IllegalArgumentException.class, () -> resolve(personneMorale));
  }

  @Test
  void compteCorrection_throws_illegal_argument() {
    var compteCorrection =
        new CompteCorrection("Correction", school.hei.patrimoine.modele.Devise.MGA);
    assertThrows(IllegalArgumentException.class, () -> resolve(compteCorrection));
  }

  @Test
  void flux_positif_debiteur_est_le_compte_du_flux() {
    var flux = new FluxArgent("Vente", COMPTE_A, now(), POSITIF);
    var comptes = resolve(flux);

    assertEquals(COMPTE_A.nom(), comptes.compteDébiteur().compte().nom());
  }

  @Test
  void flux_negatif_crediteur_est_le_compte_du_flux() {
    var flux = new FluxArgent("Charge", COMPTE_A, now(), NEGATIF);
    var comptes = resolve(flux);

    assertEquals(COMPTE_A.nom(), comptes.compteCréditeur().compte().nom());
  }

  @Test
  void transfert_debiteur_est_vers_compte() {
    var transfert = new TransfertArgent("Transfert", COMPTE_A, COMPTE_B, now(), POSITIF);
    var comptes = resolve(transfert);

    assertEquals(COMPTE_B.nom(), comptes.compteDébiteur().compte().nom());
  }

  @Test
  void transfert_crediteur_est_depuis_compte() {
    var transfert = new TransfertArgent("Transfert", COMPTE_A, COMPTE_B, now(), POSITIF);
    var comptes = resolve(transfert);

    assertEquals(COMPTE_A.nom(), comptes.compteCréditeur().compte().nom());
  }
}
