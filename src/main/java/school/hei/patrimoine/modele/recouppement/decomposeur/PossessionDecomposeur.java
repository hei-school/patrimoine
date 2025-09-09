package school.hei.patrimoine.modele.recouppement.decomposeur;

import java.util.List;
import java.util.function.Function;
import school.hei.patrimoine.modele.possession.Possession;

public interface PossessionDecomposeur<T extends Possession> extends Function<T, List<T>> {}
