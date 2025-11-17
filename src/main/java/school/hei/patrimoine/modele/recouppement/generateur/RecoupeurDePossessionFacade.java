package school.hei.patrimoine.modele.recouppement.generateur;

import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.recouppement.CompteGetterFactory.CompteGetter;

@RequiredArgsConstructor
public class RecoupeurDePossessionFacade {
  private final CompteGetter compteGetter;
  private RecoupeurDePossessionBase<?> recoupeurDePossessionBase;
  private FluxArgentRecoupeurDePossession fluxArgentRecoupeurDePossession;

  @SuppressWarnings("unchecked")
  public <T extends Possession> RecoupeurDePossession<T> getRecoupeur(T possession) {
    if (possession instanceof FluxArgent) {
      return (RecoupeurDePossession<T>) getFluxArgentRecoupeurDePossession();
    }
    return getRecoupeurDePossessionBase();
  }

  private FluxArgentRecoupeurDePossession getFluxArgentRecoupeurDePossession() {
    if (fluxArgentRecoupeurDePossession == null) {
      fluxArgentRecoupeurDePossession = new FluxArgentRecoupeurDePossession(compteGetter);
    }

    return fluxArgentRecoupeurDePossession;
  }

  @SuppressWarnings("all")
  private <T extends Possession> RecoupeurDePossessionBase<T> getRecoupeurDePossessionBase() {
    if (recoupeurDePossessionBase == null) {
      recoupeurDePossessionBase = new RecoupeurDePossessionBase<>();
    }

    return (RecoupeurDePossessionBase<T>) recoupeurDePossessionBase;
  }
}
