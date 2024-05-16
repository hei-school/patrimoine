package school.hei.patrimoine.possession;

import java.time.Duration;
import java.time.Instant;

public final class Materiel extends Possession {
    private final double tauxDAppreciationAnnuelle;

    public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
        super(nom, t, valeurComptable);
        this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
    }

    @Override
    public Possession projectionFuture(Instant tFutur) {
        double tauxJournalier = valeurComptable * tauxDAppreciationAnnuelle / 365;
        int valeurComptableFutur = ((int) Duration.between(t, tFutur).toDays() * (int) tauxJournalier) + valeurComptable;
        return new Materiel(nom, tFutur, valeurComptableFutur, tauxDAppreciationAnnuelle);
    }
}
