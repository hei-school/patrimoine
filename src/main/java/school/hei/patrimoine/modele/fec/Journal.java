package school.hei.patrimoine.modele.fec;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.fec.factory.EcritureComptableFactory;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

@Builder
public record Journal(JournalCode code, String libelle, List<EcritureComptable> ecritures) {

  public Journal(JournalCode code, String libelle) {
    this(code, libelle, new ArrayList<>());
  }

  public Journal addEcriture(OperationComptable operation, Map<String, PieceJustificative> pieces) {
    var ecritureToAdd = EcritureComptableFactory.make(this, operation, pieces, ecritures.size());
    var newEcritures = new ArrayList<>(ecritures);
    newEcritures.add(ecritureToAdd);

    return Journal.builder().code(code).libelle(libelle).ecritures(newEcritures).build();
  }
}
