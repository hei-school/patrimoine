package school.hei.patrimoine.modele.recouppement.decomposeur;

import school.hei.patrimoine.modele.possession.Possession;

import java.util.List;

public class PossessionDecomposeurBase<T extends Possession> implements PossessionDecomposeur<T> {
    @Override
    public List<T> apply(T t) {
        return List.of(t);
    }
}
