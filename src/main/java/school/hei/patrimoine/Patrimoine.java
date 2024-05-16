package school.hei.patrimoine;

import school.hei.patrimoine.possession.Argent;
import school.hei.patrimoine.possession.Materiel;
import school.hei.patrimoine.possession.Possession;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record Patrimoine(
    Personne possesseur, Instant t, Set<Possession> possessions) {
    public int getValeurComptable() {
      int valeurComptable = 0;
      if (possessions.isEmpty()) {
        return 0;
      }else {
        for (Possession possession : possessions) {
          valeurComptable += possession.getValeurComptable();
        }
      }
      return valeurComptable;
    }
    public Patrimoine projectionFuture(Instant tFutur) {
        return new Patrimoine(possesseur,tFutur,possessions.stream().map(p->p.projectionFuture(tFutur)).collect(Collectors.toSet()));
    }

}


