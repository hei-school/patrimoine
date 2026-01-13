package school.hei.patrimoine.modele.recouppement;

import static java.util.stream.Collectors.toSet;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import school.hei.patrimoine.modele.decomposeur.PieceJustificativeDecomposeurFactory;
import school.hei.patrimoine.modele.decomposeur.PossessionDecomposeurFactory;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

public class RecoupeurDePieceJustificative {
  private final Set<PieceJustificative> piecesJustificatives;
  private final Set<Possession> possessions;
  private final LocalDate finSimulation;
  private static final String PIECE_JUSTIFICATIVE_SEPARATEUR = "__du_";

  public RecoupeurDePieceJustificative(
      Set<PieceJustificative> piecesJustificatives,
      Set<Possession> possessions,
      LocalDate finSimulation) {
    this.piecesJustificatives =
        piecesJustificatives.stream()
            .map(pj -> PieceJustificativeDecomposeurFactory.make(finSimulation).apply(pj))
            .flatMap(Collection::stream)
            .collect(toSet());
    this.finSimulation = finSimulation;

    this.possessions =
        possessions.stream()
            .map(p -> PossessionDecomposeurFactory.make(p, finSimulation).apply(p))
            .flatMap(Collection::stream)
            .collect(toSet());
  }

  public Set<PossessionWithPieceJustificative<? extends Possession>> getPossessionWithPj() {
    return possessions.stream()
        .flatMap(
            p ->
                piecesJustificatives.stream()
                    .filter(
                        pj ->
                            pj.id().equals(p.nom())
                                || pj.id().startsWith(p.nom() + PIECE_JUSTIFICATIVE_SEPARATEUR))
                    .map(pj -> new PossessionWithPieceJustificative<>(p, pj)))
        .collect(toSet());
  }

  public List<Possession> getPossessionsWithoutPj() {
    var possessionsWithPj =
        getPossessionWithPj().stream()
            .map(PossessionWithPieceJustificative::possession)
            .collect(toSet());

    return possessions.stream().filter(p -> !possessionsWithPj.contains(p)).toList();
  }

  public <T extends Possession> PieceJustificative getPieceJustificativeFor(T possession) {
    return getPossessionWithPj().stream()
        .filter(possessionWithPj -> possessionWithPj.possession().equals(possession))
        .map(PossessionWithPieceJustificative::pieceJustificative)
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    "No PieceJustificative found for possession: " + possession.nom()));
  }
}
