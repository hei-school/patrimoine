package school.hei.patrimoine.modele.recouppement;

import static java.util.function.Function.identity;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

public class RecoupeurDePieceJustificative {
  private final Set<Possession> possessions;
  private final Set<PieceJustificative> piecesJustificatives;

  private Set<Possession> possessionsWithoutPj;
  private Set<PossessionWithPieceJustificative<? extends Possession>> possessionWithPjs;

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

    var possessionByNom = possessions.stream().collect(toMap(Possession::nom, identity()));

    possessionWithPjs =
        piecesJustificatives.stream()
            .map(pj -> new PossessionWithPieceJustificative<>(possessionByNom.get(pj.id()), pj))
            .collect(toSet());

    return possessionWithPjs;
  }

  public Set<PossessionWithPieceJustificative<? extends Possession>> getRecouped() {
    var result = new HashSet<>(getPossessionWithPj());

    var possessionsAlreadyUsed =
        result.stream()
            .map(PossessionWithPieceJustificative::possession)
            .filter(Objects::nonNull)
            .collect(toSet());

    possessions.stream()
        .filter(not(possessionsAlreadyUsed::contains))
        .forEach(p -> result.add(new PossessionWithPieceJustificative<>(p, null)));

    return result;
  }
}
