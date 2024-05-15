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
        int joursEntreTEtTFutur = (int) Duration.between(this.t, tFutur).toDays();
        double appreciationTotal = this.valeurComptable * ((joursEntreTEtTFutur * this.tauxDAppreciationAnnuelle) / 365);

        int valeurComptableFuture = (int) (this.valeurComptable + appreciationTotal);

        return new Materiel(
                this.nom,
                tFutur,
                valeurComptableFuture,
                this.tauxDAppreciationAnnuelle);
    }
}
