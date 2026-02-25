package school.hei.patrimoine.modele.comptable;

import static java.time.Month.DECEMBER;
import static java.time.Month.JANUARY;
import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Devise.EUR;
import static school.hei.patrimoine.modele.comptable.TypeComptable.*;

import java.time.LocalDate;
import java.util.Set;
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
  void materiel_est_immobilisation() {
    var materiel = new Materiel("Ordinateur", DEBUT, DEBUT.plusDays(3), ARGENT_POSITIF, 10);
    assertEquals(IMMOBILISATION, TypeComptable.from(materiel));
  }

  @Test
  void achatMaterielAuComptant_est_immobilisation() {
    var achat = new AchatMaterielAuComptant("Achat PC", DEBUT, ARGENT_POSITIF, 10, monCompte());
    assertEquals(IMMOBILISATION, TypeComptable.from(achat));
  }

  @Test
  void remboursementDette_est_charge() {
    var remboursement =
        new RemboursementDette(
            "Remb. prêt", monCompte(), monCompte(), DETTE, CREANCE, DEBUT, ARGENT_POSITIF);
    assertEquals(CHARGE, TypeComptable.from(remboursement));
  }

  @Test
  void compte_est_autre() {
    var compte = monCompte();
    assertEquals(AUTRE, TypeComptable.from(compte));
  }

  @Test
  void compteCorrection_est_autre() {
    var compteCorrection = new CompteCorrection("Correction", EUR);
    assertEquals(AUTRE, TypeComptable.from(compteCorrection));
  }

  @Test
  void correction_est_autre() {
    var correction = new Correction(monCompte(), "Correction de compte", DEBUT, ariary(20000));
    assertEquals(AUTRE, TypeComptable.from(correction));
  }

  @Test
  void groupePossession_est_autre() {
    var groupe =
        new GroupePossession(
            "Mon groupe",
            EUR,
            DEBUT,
            Set.of(
                new Materiel("Matériel 1", DEBUT, DEBUT.plusDays(3), ARGENT_POSITIF, 10),
                new Compte("Compte dans groupe", DEBUT, ARGENT_POSITIF)));
    assertEquals(AUTRE, TypeComptable.from(groupe));
  }

  @Test
  void personneMorale_est_autre() {
    var monEntreprise = new PersonneMorale("Société");
    assertEquals(AUTRE, TypeComptable.from(monEntreprise));
  }

  @Test
  void transfertArgent_est_autre() {
    var transfert =
        new TransfertArgent("Transfert", monCompte(), monCompte(), DEBUT, ARGENT_POSITIF);
    assertEquals(AUTRE, TypeComptable.from(transfert));
  }

  @Test
  void fluxArgent_positif_peut_etre_force_en_charge() {
    var flux = new FluxArgent("Flux forcé", monCompte(), DEBUT, FIN, 5, ARGENT_POSITIF);
    var op = new OperationComptable(flux, CHARGE);
    assertEquals(CHARGE, op.type());
  }

  @Test
  void fluxArgent_negatif_peut_etre_force_en_produit() {
    var flux = new FluxArgent("Flux forcé", monCompte(), DEBUT, FIN, 5, ARGENT_NEGATIF);
    var op = new OperationComptable(flux, PRODUIT);
    assertEquals(PRODUIT, op.type());
  }

  @Test
  void fluxArgent_peut_etre_force_en_cca() {
    var flux = new FluxArgent("Flux CCA", monCompte(), DEBUT, FIN, 5, ARGENT_POSITIF);
    var op = new OperationComptable(flux, CCA);
    assertEquals(CCA, op.type());
  }

  @Test
  void operationComptable_fluxArgent_explicite() {
    var flux = new FluxArgent("Loyer", monCompte(), DEBUT, FIN, 5, ARGENT_POSITIF);
    var op = new OperationComptable(flux, CHARGE);
    assertEquals(CHARGE, op.type());
  }
}
