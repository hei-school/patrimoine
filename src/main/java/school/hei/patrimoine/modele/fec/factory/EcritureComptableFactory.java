package school.hei.patrimoine.modele.fec.factory;

import static school.hei.patrimoine.modele.fec.PossessionCompteResolver.resolve;

import java.util.List;
import java.util.Map;
import org.jspecify.annotations.NonNull;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.fec.EcritureComptable;
import school.hei.patrimoine.modele.fec.Journal;
import school.hei.patrimoine.modele.fec.LigneEcriture;
import school.hei.patrimoine.modele.fec.PossessionCompteResolver;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

public class EcritureComptableFactory {
  public static EcritureComptable make(
      Journal journal,
      OperationComptable operation,
      Map<String, PieceJustificative> pieces,
      int sequence) {
    Possession possession = operation.possession();
    var pj = pieces.get(possession.nom());
    var valeurRealise = possession.valeurComptable();
    var comptes = resolve(operation);

    var mouvementComptable = getMouvementComptable(comptes, pj, valeurRealise);

    return EcritureComptable.builder()
        .id(idFormat(journal, sequence))
        .date(possession.t())
        .libelle(possession.nom())
        .lignes(List.of(mouvementComptable.ligneDebit(), mouvementComptable.ligneCredit()))
        .dateValidation(null)
        .build();
  }

  // JournalCode format: JN001
  private static @NonNull String idFormat(Journal journal, int sequence) {
    return journal.code().toString() + String.format("%03d", sequence);
  }

  // null values still need to be implemented
  private static @NonNull MouvementComptable getMouvementComptable(
      PossessionCompteResolver.Comptes comptes, PieceJustificative pj, Argent valeurRealise) {
    var ligneDebit =
        LigneEcriture.builder()
            .compte(comptes.compteDébiteur())
            .pieceJustificative(pj)
            .flux(valeurRealise)
            .lettrage(null)
            .dateLettrage(null)
            .compteAuxiliaire(null)
            .build();

    var ligneCredit =
        LigneEcriture.builder()
            .compte(comptes.compteCréditeur())
            .pieceJustificative(pj)
            .flux(valeurRealise.mult(-1))
            .lettrage(null)
            .dateLettrage(null)
            .compteAuxiliaire(null)
            .build();

    return new MouvementComptable(ligneDebit, ligneCredit);
  }

  private record MouvementComptable(LigneEcriture ligneDebit, LigneEcriture ligneCredit) {}
}
