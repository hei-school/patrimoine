package school.hei.patrimoine.modele.recouppement.decomposeur;

import java.time.LocalDate;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

public class PieceJustificativeDecomposeurFactory {

  public static PossessionDecomposeur<PieceJustificative, PieceJustificative> make(
      LocalDate finSimulation) {
    return new PieceJustificativeDecomposeur(finSimulation);
  }

  static String normalize(String nom) {
    return nom.replaceAll("-", "_");
  }
}
