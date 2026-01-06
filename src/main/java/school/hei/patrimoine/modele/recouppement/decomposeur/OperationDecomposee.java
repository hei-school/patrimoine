package school.hei.patrimoine.modele.recouppement.decomposeur;

import java.util.List;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

public record OperationDecomposee(
    List<Possession> possessions, List<PieceJustificative> piecesJustificatives) {}
