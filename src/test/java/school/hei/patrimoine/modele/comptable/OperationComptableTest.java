package school.hei.patrimoine.modele.comptable;

import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.comptable.MouvementComptable.CREDIT;
import static school.hei.patrimoine.modele.comptable.MouvementComptable.DEBIT;
import static school.hei.patrimoine.modele.comptable.TypeComptable.*;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.AchatMaterielAuComptant;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.TransfertArgent;

class OperationComptableTest {
  private Compte compteCourant;
  private Compte compteEpargne;
  private OperationComptable subject;

  @BeforeEach
  void setup() {
    compteCourant = new Compte("Compte courant", LocalDate.of(2026, 1, 1), ariary(5_000_000));
    compteEpargne = new Compte("Compte épargne", LocalDate.of(2025, 1, 1), ariary(700_000));
  }

  @Test
  void should_have_a_possesssion_and_two_compte() {
    var transfert =
        new TransfertArgent(
            "Transfert vers épargne",
            compteCourant,
            compteEpargne,
            LocalDate.of(2026, 3, 10),
            LocalDate.of(2026, 3, 10),
            5,
            ariary(1_000_000));
    var optionalOperation = OperationComptable.of(transfert);
    subject = optionalOperation.orElseThrow();

    assertEquals(transfert.nom(), subject.getPossession().nom());
    assertEquals(transfert.getDepuisCompte(), subject.getCompteCrediteur().compte());
    assertEquals(transfert.getVersCompte(), subject.getCompteDebiteur().compte());
  }

  @Test
  void each_compte_comptable_has_different_sens() {
    var achat =
        new AchatMaterielAuComptant(
            "Matériel informatique", LocalDate.of(2026, 3, 10), ariary(90_000), 5, compteCourant);
    var optionalOperation = OperationComptable.of(achat);
    subject = optionalOperation.orElseThrow();

    var credit = subject.getCompteCrediteur();
    var debit = subject.getCompteDebiteur();

    assertEquals(CREDIT, credit.mouvementComptable());
    assertEquals(DEBIT, debit.mouvementComptable());
  }

  @Test
  void each_compte_comptable_has_different_type() {
    var flux =
        new FluxArgent(
            "Entrée d'argent",
            compteCourant,
            LocalDate.of(2026, 3, 10),
            LocalDate.of(2026, 3, 10),
            10,
            ariary(10_000));
    var optionalOperation = OperationComptable.of(flux);
    subject = optionalOperation.orElseThrow();

    var credit = subject.getCompteCrediteur();
    var debit = subject.getCompteDebiteur();

    assertEquals(PCA, credit.typeComptable());
    assertEquals(BANQUE, debit.typeComptable());
  }
}
