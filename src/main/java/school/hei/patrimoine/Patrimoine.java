package school.hei.patrimoine;

import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Possession;


import java.time.Instant;
import java.util.Set;

public record Patrimoine(
        Personne possesseur, Instant t, Set<Possession> possessions) {

  public int getValeurComptable() {
    if (possessions.isEmpty()) {
      return 0;
    }else{
      int SommeDesValeurs = 0;
      for (Possession possession : possessions) {
        SommeDesValeurs += possession.getValeurComptable();
      }
      return SommeDesValeurs;
      }
  }
}
