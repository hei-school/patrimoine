package school.hei.patrimoine.modele.fec.factory;

import java.util.List;
import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.fec.EcritureComptable;
import school.hei.patrimoine.modele.fec.Journal;
import school.hei.patrimoine.modele.fec.LigneEcriture;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

public class EcritureComptableFactory {
  public static EcritureComptable make(
      Journal journal, OperationComptable operation, PieceJustificative pj) {
    var possession = operation.getPossession();

    var debit = LigneEcriture.builder().compte(operation.getCompteDebiteur()).build();
    var credit = LigneEcriture.builder().compte(operation.getCompteCrediteur()).build();

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
