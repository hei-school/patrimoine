package school.hei.patrimoine.possession;

import java.time.Instant;

public final class Materiel extends Possession {
    private final double tauxDAppreciationAnnuelle;

    public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
        super(nom, t, valeurComptable);
        this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
    }

    @Override
    public Possession projectionFuture(Instant tFutur) {
        long yearsElapsed = tFutur.getEpochSecond() - t.getEpochSecond() / 31556952L;
        double depreciatedValue = valeurComptable * Math.pow(1 + tauxDAppreciationAnnuelle, yearsElapsed);
        return new Materiel(nom, tFutur, (int) depreciatedValue, tauxDAppreciationAnnuelle);
    }
}
