package school.hei.patrimoine.modele.recouppement.generateur.info;

import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.recouppement.model.CompteGetter;

@RequiredArgsConstructor
public class FluxArgentInfoGetter extends InfoGetterBase<FluxArgent> {
  private final CompteGetter compteGetter;

  @Override
  public Argent getValeur(FluxArgent possession) {
    return possession.getFluxMensuel();
  }

  @Override
  public Compte getPossessionACorriger(FluxArgent possession) {
    return compteGetter.apply(possession.getCompte().nom());
  }
}
