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
    public int valeurComptableFuture(Instant tFutur) {
        int joursEntreTEtTFutur = (int) Duration.between(this.t, tFutur).toDays();
        double appreciationTotal = this.valeurComptable * ((joursEntreTEtTFutur * this.tauxDAppreciationAnnuelle) / 365);
        return (int) (this.valeurComptable + appreciationTotal);
    }
}
