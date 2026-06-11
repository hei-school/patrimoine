package school.hei.patrimoine.patrilang.visitors;

import java.util.List;
import school.hei.patrimoine.modele.possession.pj.OperationComment;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

public record OperationSupportingInformation(
    List<PieceJustificative> piecesJustificatives, List<OperationComment> operationComments) {

  public static OperationSupportingInformation empty() {
    return new OperationSupportingInformation(List.of(), List.of());
  }
}
