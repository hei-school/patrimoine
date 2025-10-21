package school.hei.patrimoine.modele.recouppement.decomposeur;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

public interface PossessionDecomposeur<ToDecompose, Decomposed>
    extends Function<ToDecompose, List<Decomposed>> {
  LocalDate getFinSimulation();
}
