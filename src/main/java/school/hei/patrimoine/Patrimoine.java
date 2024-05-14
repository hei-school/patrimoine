package school.hei.patrimoine;

import school.hei.patrimoine.possession.Possession;

import java.time.Instant;
import java.util.Iterator;
import java.util.Set;

public record  Patrimoine(
    Personne possesseur, Instant t, Set<Possession> possessions) {

  public int getValeurComptable() {
    int valeur = 0;
    Iterator<Possession> it = possessions.iterator();
    while (it.hasNext()) {
      Possession possession = it.next();
      valeur += possession.getValeurComptable();
    }
    return valeur;
  }
}
