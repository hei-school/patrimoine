package school.hei.patrimoine;

import school.hei.patrimoine.possession.Possession;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record Patrimoine(
    Personne possesseur, Instant t, Set<Possession> possessions) {
  public int getValeurComptable() {
    if (possessions.isEmpty()) {
      return 0;
    }
    int valeur=0;
   for(Possession possession:possessions){
     valeur+= possession.getValeurComptable();
   }
   return  valeur;
  }

  public Patrimoine projectionFuture(Instant tFutur) {
      Set<Possession> futurePossessions=new HashSet<>();

for(Possession possession:possessions){
    futurePossessions.add(possession.projectionFuture(tFutur));
}
    return new Patrimoine(possesseur,tFutur,futurePossessions);
  }
}
