package school.hei.patrimoine.modele.fec.factory;

import static school.hei.patrimoine.modele.fec.PossessionCompteResolver.resolve;

import java.util.List;
import org.jspecify.annotations.NonNull;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.fec.EcritureComptable;
import school.hei.patrimoine.modele.fec.Journal;
import school.hei.patrimoine.modele.fec.LigneEcriture;
import school.hei.patrimoine.modele.fec.PossessionCompteResolver;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

public class EcritureComptableFactory {
  public static EcritureComptable make(Journal journal, OperationComptable operation, PieceJustificative pj) {
    var possession = operation.possession();

    var comptes = resolve(operation);
    var valeurRealise = possession.valeurComptable();
    var mouvementComptable = getMouvementComptable(comptes, pj, valeurRealise);

    return EcritureComptable.builder()
        .id((journal.getNextId()))
        .date(possession.t())
        .libelle(possession.nom())
        .lignes(List.of(mouvementComptable.ligneDebit(), mouvementComptable.ligneCredit()))
        .dateValidation(null)
        .build();
  }

  // null values still need to be implemented
  private static @NonNull MouvementComptable getMouvementComptable(
      PossessionCompteResolver.Comptes comptes, PieceJustificative pj, Argent valeurRealise) {
    var ligneDebit =
        LigneEcriture.builder()
            .compte(comptes.compteDébiteur())
            .pieceJustificative(pj)
            .flux(valeurRealise)
            .build();

    var ligneCredit =
        LigneEcriture.builder()
            .compte(comptes.compteCréditeur())
            .pieceJustificative(pj)
            .flux(valeurRealise.mult(-1))
            .build();

    return new MouvementComptable(ligneDebit, ligneCredit);
  }
  private record MouvementComptable(LigneEcriture ligneDebit, LigneEcriture ligneCredit) {}
}
