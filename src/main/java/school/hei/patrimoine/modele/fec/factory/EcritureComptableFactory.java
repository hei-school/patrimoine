package school.hei.patrimoine.modele.fec.factory;

import static school.hei.patrimoine.modele.fec.PossessionCompteResolver.resolve;

import java.util.List;
import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.fec.EcritureComptable;
import school.hei.patrimoine.modele.fec.Journal;
import school.hei.patrimoine.modele.fec.LigneEcriture;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

public class EcritureComptableFactory {
  public static EcritureComptable make(
      Journal journal, OperationComptable operation, PieceJustificative pj) {
    var possession = operation.possession();
    var comptes = resolve(operation);
    var valeurRealisee = possession.valeurComptable();

    var debit =
        LigneEcriture.builder()
            .compte(comptes.compteDébiteur())
            .flux(valeurRealisee)
            .type(operation.type())
            .build();

    var credit =
        LigneEcriture.builder()
            .compte(comptes.compteCréditeur())
            .flux(valeurRealisee.mult(-1))
            .type(operation.type())
            .build();

    return EcritureComptable.builder()
        .id((journal.getNextId()))
        .date(possession.t())
        .libelle(possession.nom())
        .lignes(List.of(debit, credit))
        .dateValidation(pj != null ? pj.date() : null)
        .build();
  }
}
