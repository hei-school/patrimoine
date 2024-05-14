package school.hei.patrimoine.possession;

import school.hei.NotImplemented;

import java.time.Instant;

public final class Materiel extends Possession {
    private  final double tauxDAppreciationAnnuel;

    public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuel) {
        super(nom, t, valeurComptable);
        this.tauxDAppreciationAnnuel = tauxDAppreciationAnnuel;
    }

    @Override
    public int valeurComptableFuture(Instant tFutur){
        throw new NotImplemented();
    }
}
