package school.hei.patrimoine.modele.recouppement.decomposeur;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

@RequiredArgsConstructor
public class DecomposeurOperationPieceJustificative {
  private final LocalDate finSimulation;

  public OperationDecomposee decomposePossession(List<Possession> possessions) {
    List<Possession> decomposedPossessions =
        possessions.stream()
            .map(p -> PossessionDecomposeurFactory.make(p, finSimulation).apply(p))
            .flatMap(List::stream)
            .toList();

    List<PieceJustificative> decomposedPiecesJustificatives =
        decomposedPossessions.stream()
            .flatMap(
                p -> {
                  if (p instanceof FluxArgent flux && flux.getPieceJustificative() != null) {
                    return PieceJustificativeDecomposeurFactory.make(finSimulation)
                        .apply(flux.getPieceJustificative())
                        .stream();
                  }
                  return Stream.empty();
                })
            .collect(Collectors.toList());

    return new OperationDecomposee(decomposedPossessions, decomposedPiecesJustificatives);
  }
}
