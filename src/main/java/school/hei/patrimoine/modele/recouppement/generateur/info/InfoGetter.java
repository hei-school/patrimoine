package school.hei.patrimoine.modele.recouppement.generateur.info;

import java.time.LocalDate;
import java.util.function.Function;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.recouppement.model.Info;

public interface InfoGetter<T extends Possession> extends Function<T, Info<T>> {
  String getNom(T possession);

  Argent getValeur(T possession);

  LocalDate getDate(T possession);

  Possession getPossessionACorriger(T possession);
}
