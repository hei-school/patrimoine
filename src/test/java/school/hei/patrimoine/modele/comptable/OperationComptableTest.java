package school.hei.patrimoine.modele.comptable;

import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.comptable.MouvementComptable.CREDIT;
import static school.hei.patrimoine.modele.comptable.MouvementComptable.DEBIT;
import static school.hei.patrimoine.modele.comptable.TypeComptable.VIREMENT_INTERNE;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.TransfertArgent;

class OperationComptableTest {
  private OperationComptable subject;
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

  @BeforeEach
  void setup() {
    subject = new OperationComptable(TRANSFERT);
  }

  @Test
  void should_have_a_possesssion_and_two_compte() {
    assertEquals(TRANSFERT.nom(), subject.getPossession().nom());
    assertEquals(TRANSFERT.getDepuisCompte(), subject.getCompteCrediteur().compte());
    assertEquals(TRANSFERT.getVersCompte(), subject.getCompteDebiteur().compte());
  }

  @Test
  void each_compte_comptable_has_different_sens() {
    var credit = subject.getCompteCrediteur();
    var debit = subject.getCompteDebiteur();

    assertEquals(CREDIT, credit.mouvementComptable());
    assertEquals(DEBIT, debit.mouvementComptable());
  }

  @Test
  void each_compte_comptable_has_different_type() {
    var credit = subject.getCompteCrediteur();
    var debit = subject.getCompteDebiteur();

    assertEquals(VIREMENT_INTERNE, credit.typeComptable());
    assertEquals(VIREMENT_INTERNE, debit.typeComptable());
  }
}
