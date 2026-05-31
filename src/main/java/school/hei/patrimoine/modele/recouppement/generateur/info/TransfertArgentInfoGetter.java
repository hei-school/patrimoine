package school.hei.patrimoine.modele.recouppement.generateur.info;

import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.TransfertArgent;
import school.hei.patrimoine.modele.recouppement.model.CompteGetter;

@RequiredArgsConstructor
public class TransfertArgentInfoGetter extends InfoGetterBase<TransfertArgent> {
  private final CompteGetter compteGetter;

  @Override
  public Argent getValeur(TransfertArgent possession) {
    return possession.getFluxMensuel();
  }

  @Override
  public Compte getPossessionACorriger(TransfertArgent possession) {
    return compteGetter.apply(possession.getVersCompte().nom());
  }

  @Override
  public Compte getPossessionACorrigerNegativement(TransfertArgent possession) {
    return compteGetter.apply(possession.getDepuisCompte().nom());
  }
}
