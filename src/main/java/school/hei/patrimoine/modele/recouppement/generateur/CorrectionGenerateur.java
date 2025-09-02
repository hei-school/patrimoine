package school.hei.patrimoine.modele.recouppement.generateur;

import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.Possession;

import java.util.Set;

public interface CorrectionGenerateur<T extends Possession> {
    Set<Correction> comparer(T prévu, T réalité);

    Set<Correction> nonÉxecuté(T nonÉxecuté);

    Set<Correction> nonPrévu(T nonPrévu);
}
