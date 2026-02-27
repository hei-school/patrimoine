package school.hei.patrimoine.modele.fec.factory;

import static school.hei.patrimoine.modele.fec.PossessionCompteResolver.resolve;

import java.util.List;
import org.jspecify.annotations.NonNull;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.fec.EcritureComptable;
import school.hei.patrimoine.modele.fec.Journal;
import school.hei.patrimoine.modele.fec.LigneEcriture;
import school.hei.patrimoine.modele.fec.PossessionCompteResolver.Comptes;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

public class EcritureComptableFactory {
  public static EcritureComptable make(
      Journal journal, OperationComptable operation, PieceJustificative pj) {
    var possession = operation.possession();
    var comptes = resolve(operation);
    var valeurRealisee = possession.valeurComptable();
    var mouvementComptable = getMouvementComptable(comptes, valeurRealisee, operation);

    return EcritureComptable.builder()
        .id((journal.getNextId()))
        .date(possession.t())
        .libelle(possession.nom())
        .lignes(List.of(mouvementComptable.ligneDebit(), mouvementComptable.ligneCredit()))
        .dateValidation(pj != null ? pj.date() : null)
        .build();
  }

  private static @NonNull MouvementComptable getMouvementComptable(
      Comptes comptes, Argent valeurRealisee, OperationComptable operation) {
    var ligneDebit =
        LigneEcriture.builder()
            .compte(comptes.compteDébiteur())
            .flux(valeurRealisee)
            .type(operation.type())
            .build();

    var ligneCredit =
        LigneEcriture.builder()
            .compte(comptes.compteCréditeur())
            .flux(valeurRealisee.mult(-1))
            .type(operation.type())
            .build();

    return new MouvementComptable(ligneDebit, ligneCredit);
  }

  private record MouvementComptable(LigneEcriture ligneDebit, LigneEcriture ligneCredit) {}
}
