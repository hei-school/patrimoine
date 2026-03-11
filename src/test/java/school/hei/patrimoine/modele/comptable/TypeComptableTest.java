package school.hei.patrimoine.modele.comptable;

import static java.time.Month.DECEMBER;
import static java.time.Month.JANUARY;
import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Devise.EUR;
import static school.hei.patrimoine.modele.comptable.TypeComptable.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.*;

class TypeComptableTest {
  private static final LocalDate DEBUT = LocalDate.of(2025, JANUARY, 1);
  private static final LocalDate FIN = LocalDate.of(2025, DECEMBER, 31);
  private static final Argent ARGENT_POSITIF = new Argent(1000, EUR);
  private static final Argent ARGENT_NEGATIF = new Argent(-1000, EUR);
  private static final Dette DETTE = new Dette("ma dette", FIN, ARGENT_NEGATIF);
  private static final Creance CREANCE = new Creance("ma créance", FIN, ARGENT_POSITIF);

  private Compte monCompte() {
    return new Compte("mon compte", DEBUT, ARGENT_POSITIF);
  }

  @Test
  void achatMaterielAuComptant_actif_est_materiel() {
    var achat = new AchatMaterielAuComptant("Achat PC", DEBUT, ARGENT_POSITIF, 10, monCompte());
    var op = OperationComptable.make(achat);
    assertEquals(MATERIEL, op.getCompteActif().typeComptable());
  }

  @Test
  void achatMaterielAuComptant_passif_est_banque() {
    var achat = new AchatMaterielAuComptant("Achat PC", DEBUT, ARGENT_POSITIF, 10, monCompte());
    var op = OperationComptable.make(achat);
    assertEquals(BANQUE, op.getComptePassif().typeComptable());
  }

  @Test
  void remboursementDette_actif_est_remboursement_dette() {
    var remboursement =
        new RemboursementDette(
            "Remb. prêt", monCompte(), monCompte(), DETTE, CREANCE, DEBUT, ARGENT_POSITIF);
    var op = OperationComptable.make(remboursement);
    assertEquals(REMBOURSEMENT_DETTE, op.getCompteActif().typeComptable());
  }

  @Test
  void remboursementDette_passif_est_banque() {
    var remboursement =
        new RemboursementDette(
            "Remb. prêt", monCompte(), monCompte(), DETTE, CREANCE, DEBUT, ARGENT_POSITIF);
    var op = OperationComptable.make(remboursement);
    assertEquals(BANQUE, op.getComptePassif().typeComptable());
  }

  @Test
  void compte_actif_est_banque() {
    var op = OperationComptable.make(monCompte());
    assertEquals(BANQUE, op.getCompteActif().typeComptable());
  }

  @Test
  void compte_passif_est_banque() {
    var op = OperationComptable.make(monCompte());
    assertEquals(BANQUE, op.getComptePassif().typeComptable());
  }

  @Test
  void transfert_actif_et_passif_sont_virement_interne() {
    var transfert =
        new TransfertArgent("Transfert", monCompte(), monCompte(), DEBUT, ARGENT_POSITIF);
    var op = OperationComptable.make(transfert);
    assertEquals(VIREMENT_INTERNE, op.getCompteActif().typeComptable());
    assertEquals(VIREMENT_INTERNE, op.getComptePassif().typeComptable());
  }

  @Test
  void fluxArgent_positif_actif_est_banque() {
    var flux = new FluxArgent("Salaire", monCompte(), DEBUT, FIN, 5, ARGENT_POSITIF);
    var op = OperationComptable.make(flux);
    assertEquals(BANQUE, op.getCompteActif().typeComptable());
  }

  @Test
  void fluxArgent_positif_passif_est_pca() {
    var flux = new FluxArgent("Salaire", monCompte(), DEBUT, FIN, 5, ARGENT_POSITIF);
    var op = OperationComptable.make(flux);
    assertEquals(PCA, op.getComptePassif().typeComptable());
  }

  @Test
  void fluxArgent_negatif_actif_est_cca() {
    var flux = new FluxArgent("Loyer", monCompte(), DEBUT, FIN, 5, ARGENT_NEGATIF);
    var op = OperationComptable.make(flux);
    assertEquals(CCA, op.getCompteActif().typeComptable());
  }

  @Test
  void fluxArgent_negatif_passif_est_banque() {
    var flux = new FluxArgent("Loyer", monCompte(), DEBUT, FIN, 5, ARGENT_NEGATIF);
    var op = OperationComptable.make(flux);
    assertEquals(BANQUE, op.getComptePassif().typeComptable());
  }
}
