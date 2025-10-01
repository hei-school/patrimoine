package school.hei.patrimoine.modele.recouppement.generateur;

import java.util.Set;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;

public interface RecoupeurDePossession<T extends Possession> {
  PossessionRecoupee comparer(T prevu, Set<T> realises);

  PossessionRecoupee nonExecute(T nonExecute, Set<Possession> possessions);

  PossessionRecoupee imprevu(T nonPrevu);
}
