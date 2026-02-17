package school.hei.patrimoine.modele.recouppement.generateur.correction;

import java.util.Set;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.recouppement.model.Info;
import school.hei.patrimoine.modele.recouppement.model.PossessionRecoupee;

public interface RecoupeurDePossession<T extends Possession> {
  PossessionRecoupee<T> recouper(Info<T> prevu, Set<Info<T>> realises);
}
