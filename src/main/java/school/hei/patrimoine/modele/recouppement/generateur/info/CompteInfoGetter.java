package school.hei.patrimoine.modele.recouppement.generateur.info;

import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.recouppement.model.CompteGetter;

@RequiredArgsConstructor
public class CompteInfoGetter<T extends Compte> extends InfoGetterBase<T> {
  private final CompteGetter compteGetter;

  @Override
  public Argent getValeur(T possession) {
    return possession.valeurComptable();
  }

  @Override
  @SuppressWarnings("all")
  public T getPossessionACorriger(T possession) {
    var compte = compteGetter.apply(possession.nom());
    if (possession.getClass().isInstance(compte)) {
      return (T) compte;
    }

    throw new IllegalArgumentException(
        "Un compte portant le même nom mais d'un type différent a été trouvé. Nom="
            + possession.nom());
  }
}
