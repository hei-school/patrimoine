package school.hei.patrimoine.modele.fec.factory;

import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.fec.EcritureComptable;
import school.hei.patrimoine.modele.fec.FEC;
import school.hei.patrimoine.modele.fec.Journal;
import school.hei.patrimoine.modele.fec.JournalCode;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

import java.util.*;

public class FECFactory {
    private FECFactory() {
    }

    //TODO: use multiple journals
    public FEC make(Collection<OperationComptable> operations, Map<String, PieceJustificative> pjs) {
      var journal = JournalFactory.make( JournalCode.JN, "journal", operations, pjs);
      return new FEC(List.of(journal));
    }
}
