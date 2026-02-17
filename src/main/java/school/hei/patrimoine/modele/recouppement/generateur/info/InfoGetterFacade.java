package school.hei.patrimoine.modele.recouppement.generateur.info;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.recouppement.model.CompteGetter;
import school.hei.patrimoine.modele.recouppement.model.Info;

@Builder
@RequiredArgsConstructor
public class InfoGetterFacade {
  private final CompteGetter compteGetter;

  public <T extends Possession> Info<T> get(T possession) {
    var infoGetter = getInfoGetter(possession);
    return infoGetter.apply(possession);
  }

  @SuppressWarnings("all")
  private <T extends Possession> InfoGetter<T> getInfoGetter(T possession) {
    return switch (possession) {
      case FluxArgent flux -> (InfoGetter<T>) new FluxArgentInfoGetter(compteGetter);
      default -> new InfoGetterBase<>();
    };
  }
}
