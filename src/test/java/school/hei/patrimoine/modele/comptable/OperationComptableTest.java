package school.hei.patrimoine.modele.comptable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.comptable.TypeComptable.*;

import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.possession.*;

class OperationComptableTest {
  private static final LocalDate DATE = LocalDate.of(2025, 1, 1);
  private static final LocalDate DATE_FIN = LocalDate.of(2025, 12, 31);
  private static final Argent ARGENT_POSITIF = new Argent(1000, Devise.EUR);
  private static final Argent ARGENT_NEGATIF = new Argent(-1000, Devise.EUR);
  private static final Dette DETTE = new Dette("ma dette", DATE_FIN, ARGENT_NEGATIF);
  private static final Creance CREANCE = new Creance("ma créance", DATE_FIN, ARGENT_POSITIF);

  private Compte monCompte() {
    return new Compte("Compte test", DATE, ARGENT_POSITIF);
  }

  @Test
  void materiel_est_immobilisation() {
    var materiel = new Materiel("Ordinateur", DATE, DATE.plusDays(3), ARGENT_POSITIF, 10);
    assertEquals(IMMOBILISATION, TypeComptable.from(materiel));
  }

  @Test
  void achatMaterielAuComptant_est_immobilisation() {
    var achat = new AchatMaterielAuComptant("Achat PC", DATE, ARGENT_POSITIF, 10, monCompte());
    assertEquals(IMMOBILISATION, TypeComptable.from(achat));
  }

  @Test
  void remboursementDette_est_charge() {
    var remboursement =
        new RemboursementDette(
            "Remb. prêt", monCompte(), monCompte(), DETTE, CREANCE, DATE, ARGENT_POSITIF);
    assertEquals(CHARGE, TypeComptable.from(remboursement));
  }

  @Test
  void compte_est_autre() {
    var compte = monCompte();
    assertEquals(AUTRE, TypeComptable.from(compte));
  }

  @Test
  void compteCorrection_est_autre() {
    var compteCorrection = new CompteCorrection("Correction", Devise.EUR);
    assertEquals(AUTRE, TypeComptable.from(compteCorrection));
  }

  @Test
  void correction_est_autre() {
    var correction = new Correction(monCompte(), "Correction de compte", DATE, ariary(20000));
    assertEquals(AUTRE, TypeComptable.from(correction));
  }

  @Test
  void groupePossession_est_autre() {
    var groupe =
        new GroupePossession(
            "Mon groupe",
            Devise.EUR,
            DATE,
            Set.of(
                new Materiel("Matériel 1", DATE, DATE.plusDays(3), ARGENT_POSITIF, 10),
                new Compte("Compte dans groupe", DATE, ARGENT_POSITIF)));
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
        new TransfertArgent("Transfert", monCompte(), monCompte(), DATE, ARGENT_POSITIF);
    assertEquals(AUTRE, TypeComptable.from(transfert));
  }

  @Test
  void fluxArgent_positif_est_produit_par_defaut() {
    var flux = new FluxArgent("Salaire", monCompte(), DATE, DATE_FIN, 5, ARGENT_POSITIF);
    assertEquals(PRODUIT, TypeComptable.from(flux));
  }

  @Test
  void fluxArgent_negatif_est_charge_par_defaut() {
    var flux = new FluxArgent("Loyer", monCompte(), DATE, DATE_FIN, 5, ARGENT_NEGATIF);
    assertEquals(CHARGE, TypeComptable.from(flux));
  }

  @Test
  void fluxArgent_positif_peut_etre_force_en_charge() {
    var flux = new FluxArgent("Flux forcé", monCompte(), DATE, DATE_FIN, 5, ARGENT_POSITIF, CHARGE);
    assertEquals(CHARGE, TypeComptable.from(flux));
  }

  @Test
  void fluxArgent_negatif_peut_etre_force_en_produit() {
    var flux =
        new FluxArgent("Flux forcé", monCompte(), DATE, DATE_FIN, 5, ARGENT_NEGATIF, PRODUIT);
    assertEquals(PRODUIT, TypeComptable.from(flux));
  }

  @Test
  void fluxArgent_peut_etre_force_en_cca() {
    var flux = new FluxArgent("Flux CCA", monCompte(), DATE, DATE_FIN, 5, ARGENT_POSITIF, CCA);
    assertEquals(CCA, TypeComptable.from(flux));
  }

  @Test
  void operationComptable_resout_typeComptable_automatiquement() {
    var materiel = new Materiel("Voiture", DATE, DATE, ARGENT_POSITIF, 5);
    var operationComptable = OperationComptable.make(materiel);

    assertEquals(materiel, operationComptable.possession());
    assertEquals(IMMOBILISATION, operationComptable.typeComptable());
  }

  @Test
  void operationComptable_accepte_typeComptable_explicite() {
    Compte compte = monCompte();
    OperationComptable operationComptable = new OperationComptable(compte, CCA);

    assertEquals(compte, operationComptable.possession());
    assertEquals(CCA, operationComptable.typeComptable());
  }

  @Test
  void operationComptable_fluxArgent_deduit() {
    FluxArgent flux = new FluxArgent("Salaire", monCompte(), DATE, DATE_FIN, 5, ARGENT_POSITIF);
    OperationComptable operationComptable = OperationComptable.make(flux);

    assertEquals(PRODUIT, operationComptable.typeComptable());
  }

  @Test
  void operationComptable_fluxArgent_explicite() {
    FluxArgent flux =
        new FluxArgent("Loyer", monCompte(), DATE, DATE_FIN, 5, ARGENT_POSITIF, CHARGE);
    OperationComptable operationComptable = new OperationComptable(flux, CHARGE);

    assertEquals(CHARGE, operationComptable.typeComptable());
  }
}
