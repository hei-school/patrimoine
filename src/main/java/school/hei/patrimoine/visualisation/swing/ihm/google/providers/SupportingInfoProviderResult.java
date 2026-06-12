package school.hei.patrimoine.visualisation.swing.ihm.google.providers;

import java.util.List;
import java.util.Map;
import school.hei.patrimoine.modele.possession.pj.OperationComment;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

public record SupportingInfoProviderResult(
    Map<String, PieceJustificative> piecesJustificatives,
    List<OperationComment> operationComments) {
  public static SupportingInfoProviderResult empty() {
    return new SupportingInfoProviderResult(Map.of(), List.of());
  }
}
