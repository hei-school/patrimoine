package school.hei.patrimoine;

import school.hei.patrimoine.possession.Possession;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public record Patrimoine(
        Personne possesseur, Instant t, Set<Possession> possessions) {
    public int getValeurComptable() {
        if (possessions.isEmpty()) {
            return 0;
        }
        return possessions
                .stream()
                .mapToInt(Possession::getValeurComptable)
                .sum();
    }

    public Patrimoine projectionFuture(Instant tFutur) {
        Set<Possession> possessionsProjeteesDansLeFutur = new HashSet<>();
        for (Possession possession : possessions) {
            Possession possessionFutur = possession.projectionFuture(tFutur);
            possessionsProjeteesDansLeFutur.add(possessionFutur);
        }
        return new Patrimoine(this.possesseur, tFutur, possessionsProjeteesDansLeFutur);
    }
}
