package school.hei.patrimoine.modele.comptable;

import static java.time.Month.DECEMBER;
import static java.time.Month.JANUARY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Devise.EUR;
import static school.hei.patrimoine.modele.comptable.OperationComptable.make;
import static school.hei.patrimoine.modele.comptable.TypeComptable.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.*;

class OperationComptableTest {
  private static final LocalDate DEBUT = LocalDate.of(2025, JANUARY, 1);
  private static final LocalDate FIN = LocalDate.of(2025, DECEMBER, 31);
  private static final Argent ARGENT = new Argent(1000, EUR);
  private static final Argent ARGENT_NEGATIF = new Argent(-1000, EUR);

  private Compte monCompte() {
    return new Compte("mon compte", DEBUT, ARGENT);
  }

  @Test
  void achatMateriel_actif_est_materiel() {
    var achat = new AchatMaterielAuComptant("Voiture", DEBUT, ARGENT, 5, monCompte());
    var op = make(achat);

    assertEquals(achat, op.getPossession());
    assertEquals(MATERIEL, op.getCompteActif().typeComptable());
  }

  @Test
  void achatMateriel_passif_est_banque() {
    var achat = new AchatMaterielAuComptant("Voiture", DEBUT, ARGENT, 5, monCompte());
    assertEquals(BANQUE, make(achat).getComptePassif().typeComptable());
  }

  @Test
  void compte_actif_est_banque() {
    var op = make(monCompte());
    assertEquals(BANQUE, op.getCompteActif().typeComptable());
  }

  @Test
  void fluxArgent_positif_actif_est_banque() {
    var flux = new FluxArgent("Salaire", monCompte(), DEBUT, FIN, 5, ARGENT);
    assertEquals(BANQUE, make(flux).getCompteActif().typeComptable());
  }

  @Test
  void fluxArgent_positif_passif_est_pca() {
    var flux = new FluxArgent("Salaire", monCompte(), DEBUT, FIN, 5, ARGENT);
    assertEquals(PCA, make(flux).getComptePassif().typeComptable());
  }

  @Test
  void fluxArgent_negatif_actif_est_cca() {
    var flux = new FluxArgent("Loyer", monCompte(), DEBUT, FIN, 5, ARGENT_NEGATIF);
    assertEquals(CCA, make(flux).getCompteActif().typeComptable());
  }

  @Test
  void fluxArgent_negatif_passif_est_banque() {
    var flux = new FluxArgent("Loyer", monCompte(), DEBUT, FIN, 5, ARGENT_NEGATIF);
    assertEquals(BANQUE, make(flux).getComptePassif().typeComptable());
  }
}
