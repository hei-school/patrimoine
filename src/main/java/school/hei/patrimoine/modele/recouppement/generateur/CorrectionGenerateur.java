package school.hei.patrimoine.modele.recouppement.generateur;

import java.util.Set;
import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.Possession;

public interface CorrectionGenerateur<T extends Possession> {
  Set<Correction> comparer(T prévu, T réalité);

  Set<Correction> nonÉxecuté(T nonÉxecuté);

  Set<Correction> imprévu(T nonPrévu);
}
