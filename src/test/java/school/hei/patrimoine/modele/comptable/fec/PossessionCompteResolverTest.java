package school.hei.patrimoine.modele.comptable.fec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.comptable.MouvementComptable.CREDIT;
import static school.hei.patrimoine.modele.comptable.MouvementComptable.DEBIT;
import static school.hei.patrimoine.modele.comptable.fec.PossessionCompteResolver.resolve;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.TransfertArgent;

class PossessionCompteResolverTest {
  private static final Compte COMPTE_COURANT =
      new Compte("Compte courant", LocalDate.of(2026, 1, 1), ariary(5_000_000));
  private static final Compte COMPTE_EPARGNE =
      new Compte("Compte épargne", LocalDate.of(2025, 1, 1), ariary(700_000));
  private static final TransfertArgent TRANSFERT =
      new TransfertArgent(
          "Transfert vers épargne",
          COMPTE_COURANT,
          COMPTE_EPARGNE,
          LocalDate.of(2026, 3, 10),
          LocalDate.of(2026, 3, 10),
          5,
          ariary(1_000_000));

  @Test
  void should_return_two_compte_comptable() {
    var comptes = resolve(TRANSFERT);
    var debiteur = comptes.compteDébiteur().compte();
    var crediteur = comptes.compteCréditeur().compte();

    assertEquals(TRANSFERT.getVersCompte(), debiteur);
    assertEquals(TRANSFERT.getDepuisCompte(), crediteur);
  }

  @Test
  void should_have_correct_mouvement_for_operation() {
    var comptes = resolve(TRANSFERT);
    var debiteur = comptes.compteDébiteur();
    var crediteur = comptes.compteCréditeur();

    assertEquals(DEBIT, debiteur.mouvementComptable());
    assertEquals(CREDIT, crediteur.mouvementComptable());
  }
}
