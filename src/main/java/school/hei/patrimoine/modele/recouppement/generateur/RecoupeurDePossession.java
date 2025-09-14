package school.hei.patrimoine.modele.recouppement.generateur;

import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;

public interface RecoupeurDePossession<T extends Possession> {
  PossessionRecoupee comparer(T prevu, T realise);

  PossessionRecoupee nonExecute(T nonExecute);

  PossessionRecoupee imprevu(T nonPrevu);
}
