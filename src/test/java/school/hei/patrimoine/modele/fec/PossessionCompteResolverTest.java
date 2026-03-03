package school.hei.patrimoine.modele.fec;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.possession.*;

class PossessionCompteResolverTest {
  private static final Argent ZERO = ariary(0);
  private static final Argent POSITIF = ariary(500_000);
  private static final Argent NEGATIF = ariary(-500_000);
  private static final Compte COMPTE_A = new Compte("Compte A", now(), ZERO);
  private static final Compte COMPTE_B = new Compte("Compte B", now(), ZERO);

  @Test
  void flux_positif_debit_compte_credit_attente() {
    var flux = new FluxArgent("Vente", COMPTE_A, now(), POSITIF);
    var comptes = PossessionCompteResolver.resolve(OperationComptable.make(flux));

    assertEquals("Compte A", comptes.compteDébiteur().nom());
    assertEquals("Compte d'attente", comptes.compteCréditeur().nom());
  }

  @Test
  void flux_negatif_debit_attente_credit_compte() {
    var flux = new FluxArgent("Charge", COMPTE_A, now(), NEGATIF);
    var comptes = PossessionCompteResolver.resolve(OperationComptable.make(flux));

    assertEquals("Compte d'attente", comptes.compteDébiteur().nom());
    assertEquals("Compte A", comptes.compteCréditeur().nom());
  }

  @Test
  void transfert_debit_vers_credit_depuis() {
    var transfert = new TransfertArgent("Transfert", COMPTE_A, COMPTE_B, now(), POSITIF);
    var comptes = PossessionCompteResolver.resolve(OperationComptable.make(transfert));

    assertEquals("Compte B", comptes.compteDébiteur().nom());
    assertEquals("Compte A", comptes.compteCréditeur().nom());
  }

  @Test
  void compte_debit_compte_credit_capital_social() {
    var comptes = PossessionCompteResolver.resolve(OperationComptable.make(COMPTE_A));

    assertEquals("Compte A", comptes.compteDébiteur().nom());
    assertEquals("Capital social", comptes.compteCréditeur().nom());
  }

  @Test
  void achat_materiel_debit_finance_credit_financeur() {
    var achat = new AchatMaterielAuComptant("Ordinateur", now(), POSITIF, 0.1, COMPTE_A);
    var comptes = PossessionCompteResolver.resolve(OperationComptable.make(achat));

    assertEquals("Matériel", comptes.compteDébiteur().nom());
    assertEquals("Compte A", comptes.compteCréditeur().nom());
  }

  @Test
  void remboursement_dette_debit_rembourse_credit_rembourseur() {
    var dette = new Dette("Dette", now(), ariary(-100_000));
    var creance = new Creance("Créance", now(), ariary(100_000));
    var remboursement =
        new RemboursementDette(
            "Remboursement", COMPTE_A, COMPTE_B, dette, creance, now(), ariary(100_000));
    var comptes = PossessionCompteResolver.resolve(OperationComptable.make(remboursement));

    assertEquals(COMPTE_B.nom(), comptes.compteDébiteur().nom());
    assertEquals(COMPTE_A.nom(), comptes.compteCréditeur().nom());
  }
}
