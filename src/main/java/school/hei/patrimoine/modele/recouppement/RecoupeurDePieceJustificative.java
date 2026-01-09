package school.hei.patrimoine.modele.recouppement;

import school.hei.patrimoine.modele.decomposeur.PossessionDecomposeurFactory;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.Collection;
import java.util.stream.Collectors;

public class RecoupeurPieceJustificative {
    private final Set<PieceJustificative> piecesJustificatives;
    private final Set<Possession> possessions;
    private final LocalDate finSimulation;

    public RecoupeurPieceJustificative(
            Set<PieceJustificative> piecesJustificatives,
            Set<Possession> possessions,
            LocalDate finSimulation
    ) {
        this.piecesJustificatives = piecesJustificatives;
        this.finSimulation = finSimulation;

        this.possessions =
                possessions.stream()
                        .map(p -> PossessionDecomposeurFactory.make(p, finSimulation).apply(p))
                        .flatMap(Collection::stream)
                        .collect(Collectors.toSet());
    }

    private Set<PossessionWithPieceJustificative<? extends Possession>> getPossessionWithPj() {
        return possessions.stream()
                .flatMap(p ->
                        piecesJustificatives.stream()
                                .filter(pj -> pj.id().equals(p.nom()))
                                .map(pj -> new PossessionWithPieceJustificative<>(p, pj))
                )
                .collect(Collectors.toSet());
    }

    private List<Possession> getPossessionsWithoutPj() {
        var possessionsWithPj = getPossessionWithPj().stream()
                .map(PossessionWithPieceJustificative::possession)
                .collect(Collectors.toSet());

        return possessions.stream()
                .filter(p -> !possessionsWithPj.contains(p))
                .toList();
    }

    private <T extends Possession> PieceJustificative getPieceJustificativeFor(T possession) {
        return getPossessionWithPj().stream()
                .filter(possessionWithPj -> possessionWithPj.possession().equals(possession))
                .map(PossessionWithPieceJustificative::pieceJustificative)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No PieceJustificative found for possession: " + possession.nom()));
    }
}
