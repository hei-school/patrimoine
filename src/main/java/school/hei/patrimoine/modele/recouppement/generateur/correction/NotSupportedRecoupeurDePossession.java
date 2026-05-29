package school.hei.patrimoine.modele.recouppement.generateur.correction;

import static school.hei.patrimoine.modele.recouppement.model.RecoupementStatus.EXECUTE_SANS_CORRECTION;

import java.util.Set;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.recouppement.model.Info;
import school.hei.patrimoine.modele.recouppement.model.PossessionRecoupee;

public class NotSupportedRecoupeurDePossession<T extends Possession>
    extends RecoupeurDePossessionBase<T> {
  @Override
  public PossessionRecoupee<T> recouper(Info<T> prevu, Set<Info<T>> realises) {
    var possession = realises.stream().findFirst().orElse(prevu);
    return PossessionRecoupee.<T>builder()
        .prevu(possession)
        .realises(Set.of(possession))
        .corrections(Set.of())
        .status(EXECUTE_SANS_CORRECTION)
        .build();
  }
}
