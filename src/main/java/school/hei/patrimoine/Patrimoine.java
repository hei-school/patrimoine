package school.hei.patrimoine;

import school.hei.patrimoine.possession.Possession;

import java.time.Instant;
import java.util.Set;

public record Patrimoine(
        Personne possesseur, Instant t, Set<Possession> possessions) {

    public int getValeurComptable() {
        int result = 0 ;
        if (possessions.isEmpty()) {
            return result ;
        }
        for(Possession possession : possessions){
            int comptableValue = possession.getValeurComptable();
            result += comptableValue ;
        }
        return result ;
    }
}