package school.hei.patrimoine.modele.recouppement.generateur;

import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;

public interface RecoupeurDePossession<T extends Possession> {
  PossessionRecoupee comparer(T prévu, T réalisé);

  PossessionRecoupee nonÉxecuté(T nonÉxecuté);

  PossessionRecoupee imprévu(T nonPrévu);
}
