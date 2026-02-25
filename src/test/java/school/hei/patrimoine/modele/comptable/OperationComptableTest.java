package school.hei.patrimoine.modele.comptable;

import static java.time.Month.DECEMBER;
import static java.time.Month.JANUARY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Devise.EUR;
import static school.hei.patrimoine.modele.comptable.TypeComptable.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.*;

class OperationComptableTest {
  private static final LocalDate DEBUT = LocalDate.of(2025, JANUARY, 1);
  private static final LocalDate FIN = LocalDate.of(2025, DECEMBER, 31);
  private static final Argent ARGENT = new Argent(1000, EUR);

  private Compte monCompte() {
    return new Compte("mon compte", DEBUT, ARGENT);
  }

  @Test
  void operationComptable_resout_typeComptable_automatiquement() {
    var materiel = new Materiel("Voiture", DEBUT, DEBUT, ARGENT, 5);
    var operationComptable = OperationComptable.make(materiel);

    assertEquals(materiel, operationComptable.possession());
    assertEquals(IMMOBILISATION, operationComptable.typeComptable());
  }

  @Test
  void operationComptable_accepte_typeComptable_explicite() {
    Compte compte = monCompte();
    var operationComptable = new OperationComptable(compte, CCA);

    assertEquals(compte, operationComptable.possession());
    assertEquals(CCA, operationComptable.typeComptable());
  }

  @Test
  void operationComptable_fluxArgent_deduit() {
    var flux = new FluxArgent("Salaire", monCompte(), DEBUT, FIN, 5, ARGENT);
    OperationComptable operationComptable = OperationComptable.make(flux);

    assertEquals(PRODUIT, operationComptable.typeComptable());
  }

  @Test
  void operationComptable_fluxArgent_explicite() {
    var flux = new FluxArgent("Loyer", monCompte(), DEBUT, FIN, 5, ARGENT, CHARGE);
    var operationComptable = new OperationComptable(flux, CHARGE);

    assertEquals(CHARGE, operationComptable.typeComptable());
  }
}
