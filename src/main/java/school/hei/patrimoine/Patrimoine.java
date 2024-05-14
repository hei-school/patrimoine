package school.hei.patrimoine;

import school.hei.patrimoine.possession.Possession;

import java.time.Instant;
import java.util.Set;

public record Patrimoine(
        Personne possesseur, Instant t, Set<Possession> possessions) {

    public int getValeurComptable() {
        if (possessions.isEmpty()) {
            return 0;
        } else {
            int total = 0;
            for (Possession possession : possessions) {
                total += possession.getValeurComptable();
                return total;
            }
        }
        throw new NotImplemented();
    }
}
