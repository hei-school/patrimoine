package school.hei.patrimoine.modele.fec.factory;

import static school.hei.patrimoine.modele.fec.JournalCode.JN;

import java.util.*;
import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.fec.FEC;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

public class FECFactory {
  private FECFactory() {}

  // TODO: use multiple journals
  public static FEC make(
      Collection<OperationComptable> operations, Map<String, PieceJustificative> pjs) {
    var journal = JournalFactory.make(JN, "journal", operations, pjs);
    return new FEC(List.of(journal));
  }
}
