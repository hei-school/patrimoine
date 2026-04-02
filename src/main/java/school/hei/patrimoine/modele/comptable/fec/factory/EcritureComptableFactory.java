package school.hei.patrimoine.modele.comptable.fec.factory;

import java.util.List;
import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.comptable.fec.EcritureComptable;
import school.hei.patrimoine.modele.comptable.fec.Journal;
import school.hei.patrimoine.modele.comptable.fec.LigneEcriture;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

public class EcritureComptableFactory {
  private EcritureComptableFactory() {}

  public static EcritureComptable make(
      Journal journal, OperationComptable operation, PieceJustificative pj) {
    var possession = operation.getPossession();

    var debit = LigneEcriture.builder().compteComptable(operation.getCompteDebiteur()).build();
    var credit = LigneEcriture.builder().compteComptable(operation.getCompteCrediteur()).build();

    return EcritureComptable.builder()
        .id((journal.getNextId()))
        .date(possession.t())
        .libelle(possession.nom())
        .lignes(List.of(debit, credit))
        .dateValidation(pj != null ? pj.date() : null)
        .pj(pj)
        .build();
  }
}
