package school.hei.patrimoine.modele.recouppement.possession;

import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.Possession;

import java.util.Set;

public interface CorrectionGenerateur<T extends Possession> {
    Set<Correction> comparer(T prévu, T réalité);

    Set<Correction> nonExecuté(T nonExecuté);

    Set<Correction> nonPrévu(T nonPrévu);
}
