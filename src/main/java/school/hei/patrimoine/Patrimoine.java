package school.hei.patrimoine;

import java.util.Iterator;
import school.hei.patrimoine.possession.Possession;

import java.time.Instant;
import java.util.Set;

public record Patrimoine(
    Personne possesseur, Instant t, Set<Possession> possessions) {

  public int getValeurComptable() {
    Iterator<Possession> iterator = possessions.iterator();
    int sommeValeursComptables = 0;

    while(iterator.hasNext()) {
      Possession possession = iterator.next();
      sommeValeursComptables += possession.getValeurComptable();
    }

    return sommeValeursComptables;
  }


}
