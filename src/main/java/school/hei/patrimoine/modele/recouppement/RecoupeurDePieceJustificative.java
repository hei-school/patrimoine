package school.hei.patrimoine.modele.recouppement;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toSet;

import java.util.Set;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

public class RecoupeurDePieceJustificative {
  private final Set<Possession> possessions;
  private final Set<PieceJustificative> piecesJustificatives;

  private Set<PossessionWithPieceJustificative<? extends Possession>> possessionWithPjs;
  private Set<Possession> possessionsWithoutPj;

  public RecoupeurDePieceJustificative(
      Set<PieceJustificative> piecesJustificatives, Set<Possession> possessions) {
    this.possessions = possessions;
    this.piecesJustificatives = piecesJustificatives;
  }

  public Set<Possession> getPossessionsWithoutPj() {
    if (possessionsWithoutPj != null) {
      return possessionsWithoutPj;
    }

    var possessionsWithPj =
        getPossessionWithPj().stream()
            .filter(association -> association.pieceJustificative() != null)
            .map(PossessionWithPieceJustificative::possession)
            .collect(toSet());

    possessionsWithoutPj =
        possessions.stream().filter(not(possessionsWithPj::contains)).collect(toSet());

    return possessionsWithoutPj;
  }

  public Set<PossessionWithPieceJustificative<? extends Possession>> getPossessionWithPj() {
    if (possessionWithPjs != null) {
      return possessionWithPjs;
    }

    possessionWithPjs =
        possessions.stream()
            .map(
                possession -> {
                  var pj =
                      piecesJustificatives.stream()
                          .filter(pjItem -> pjItem.id().equals(possession.nom()))
                          .findFirst()
                          .orElse(null);

                  return new PossessionWithPieceJustificative<>(possession, pj);
                })
            .collect(toSet());
    return possessionWithPjs;
  }
}
