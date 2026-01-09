package school.hei.patrimoine.modele.recouppement;

import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

public record PossessionWithPieceJustificative<T extends Possession>(
    T possession, PieceJustificative pieceJustificative) {}
