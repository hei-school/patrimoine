package school.hei.patrimoine.modele.fec.factory;

import java.util.Collection;
import java.util.Map;
import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.fec.Journal;
import school.hei.patrimoine.modele.fec.JournalCode;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

public class JournalFactory {
  private JournalFactory() {}

  public static Journal make(
      JournalCode code,
      String libelle,
      Collection<OperationComptable> operations,
      Map<String, PieceJustificative> pjs) {
    if (operations == null) {
      return new Journal(code, libelle);
    }

    if (pjs == null) {
      pjs = Map.of();
    }

    var journal = new Journal(code, libelle);
    for (var operation : operations) {
      var pj = pjs.get(operation.getPossession().nom());
      journal.addEcriture(operation, pj);
    }

    return journal;
  }
}
