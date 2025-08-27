package school.hei.patrimoine.modele.recouppement.possession;

import school.hei.patrimoine.modele.possession.Correction;

import java.util.Set;
import java.util.function.Supplier;

public interface CorrectionGenerateur extends Supplier<Set<Correction>> {}
