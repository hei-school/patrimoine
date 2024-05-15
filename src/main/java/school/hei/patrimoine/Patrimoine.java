package school.hei.patrimoine;

import school.hei.patrimoine.possession.Possession;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

public record Patrimoine(
        Personne possesseur, Instant t, Set<Possession> possessions) {
    public int getValeurComptable() {
        if (possessions.isEmpty()) {
            return 0;
        }
        return possessions.stream()
                .mapToInt(Possession::getValeurComptable)
                .sum();
    }

    public Patrimoine projectionFuture(Instant tFutur) {
        Set<Possession> possessionsFuture = this.possessions.stream()
                .map(possession -> possession.projectionFuture(tFutur))
                .collect(Collectors.toSet());

        return new Patrimoine(this.possesseur, tFutur, possessionsFuture);
    }
}
