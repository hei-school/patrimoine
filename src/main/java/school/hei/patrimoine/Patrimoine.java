package school.hei.patrimoine;

import school.hei.NotImplemented;
import school.hei.patrimoine.possession.Possession;

import java.time.Instant;
import java.util.Set;

public record Patrimoine
        (Personne possesseur, Instant t, Set<Possession> possessions) {
    //on supprime et on le met aec possession private int valeurComptable;

    public int getValeurComptable(Instant t) {
        if (possessions.isEmpty()) {
            return 0;
        }
        throw new NotImplemented();
    }
}
