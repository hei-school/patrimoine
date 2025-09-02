package school.hei.patrimoine.modele.recouppement.decomposeur;

import school.hei.patrimoine.modele.possession.Possession;

import java.util.List;
import java.util.function.Function;

public interface PossessionDecomposeur<T extends Possession> extends Function<T, List<T>> {}
