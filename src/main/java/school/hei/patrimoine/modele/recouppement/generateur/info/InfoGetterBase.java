package school.hei.patrimoine.modele.recouppement.generateur.info;

import java.time.LocalDate;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.recouppement.model.Info;

public class InfoGetterBase<T extends Possession> implements InfoGetter<T> {
  @Override
  public Info<T> apply(T possession) {
    return new Info<>(
        getNom(possession),
        getDate(possession),
        getValeur(possession),
        possession,
        getPossessionACorriger(possession));
  }

  @Override
  public Possession getPossessionACorriger(T possession) {
    return possession;
  }

  @Override
  public String getNom(T possession) {
    return possession.nom();
  }

  @Override
  public LocalDate getDate(T possession) {
    return possession.t();
  }

  @Override
  public Argent getValeur(T possession) {
    return possession.valeurComptable();
  }

  @Override
  public Possession getPossessionACorrigerNegativement(T possession) {
    return null;
  }
}
