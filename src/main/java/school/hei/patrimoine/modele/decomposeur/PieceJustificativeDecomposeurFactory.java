package school.hei.patrimoine.modele.decomposeur;

import java.time.LocalDate;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

public class PieceJustificativeDecomposeurFactory {
  public static PossessionDecomposeur<PieceJustificative, PieceJustificative> make(
      LocalDate finSimulation) {
    return new PieceJustificativeDecomposeur(finSimulation);
  }
}
