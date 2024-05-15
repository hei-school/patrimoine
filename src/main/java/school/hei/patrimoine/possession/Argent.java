package school.hei.patrimoine.possession;

import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public final class Argent extends Possession {
  public Argent(String nom, Instant t, int valeurComptable) {
    super(nom, t, valeurComptable);
  }

  @Override
  public Possession projectionFuture(Instant tFutur) {
    LocalDate datePresent = LocalDate.ofInstant(getT(), ZoneId.systemDefault());
    LocalDate future = LocalDate.ofInstant(tFutur, ZoneId.systemDefault());
    int Annee = future.getYear() - datePresent.getYear();
    int FutureValeur = getValeurComptable(); // La valeur reste la même indépendamment des années
    return new Argent(getNom(), tFutur, FutureValeur);
  }
  }

