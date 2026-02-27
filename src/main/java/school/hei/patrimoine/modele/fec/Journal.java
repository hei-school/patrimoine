package school.hei.patrimoine.modele.fec;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.fec.factory.EcritureComptableFactory;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

@Builder
public record Journal(JournalCode code, String libelle, List<EcritureComptable> ecritures) {

  public Journal(JournalCode code, String libelle) {
    this(code, libelle, new ArrayList<>());
  }

  public void addEcriture(OperationComptable operation, PieceJustificative pj) {
    var ecritureToAdd = EcritureComptableFactory.make(this, operation, pj);
    ecritures.add(ecritureToAdd);
  }

  public String getNextId() {
    return code().toString() + String.format("%03d", ecritures.size());
  }
}
