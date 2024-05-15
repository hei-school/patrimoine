package school.hei.patrimoine.possession;

import java.time.Instant;
import java.time.ZoneId;

public final class Materiel extends Possession {
    private final double tauxDAppreciationAnnuelle;

    public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
        super(nom, t, valeurComptable);
        this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
    }

    @Override
    public int valeurComptableFuture(Instant tFutur) {
        int nombreAnnées = ((tFutur.atZone(ZoneId.systemDefault()).getYear()) - t.atZone(ZoneId.systemDefault()).getYear());
        double valeurFutur = valeurComptable + (valeurComptable * (tauxDAppreciationAnnuelle * nombreAnnées));
        return (int) valeurFutur;
    }
}
