package school.hei.patrimoine;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public record Patrimoine
        (Personne possesseur, Instant t, Set<Possession> possessions) {
    //on supprime et on le met aec possession private int valeurComptable;

    public int getValeurComptable(Instant t) {
        if (possessions.isEmpty()) {
            return 0;
        }
        throw new RuntimeException("TODO");
    }
}
