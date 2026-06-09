package school.hei.patrimoine.patrilang.visitors;

import java.util.List;
import school.hei.patrimoine.modele.possession.pj.OperationComments;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

public record PJAndComments(
    List<PieceJustificative> piecesJustificatives, List<OperationComments> operationComments) {
  // TODO: change record name
  public static PJAndComments empty() {
    return new PJAndComments(List.of(), List.of());
  }
}
