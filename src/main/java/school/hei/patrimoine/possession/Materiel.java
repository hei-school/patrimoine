package school.hei.patrimoine.possession;

import java.time.Instant;
import java.util.Date;

public final class Materiel extends Possession {
    private final double tauxDAppreciationAnnuelle;

    public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
        super(nom, t, valeurComptable);
        this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
    }

    @Override
    public int valeurComptableFuture(Instant tFutur) {
        Date t1 = Date.from(t);
        Date t1Futur = Date.from(tFutur);
        long differenceDansLeTempsEnMilliseconde = t1Futur.getTime() - t1.getTime();
        long jours = (differenceDansLeTempsEnMilliseconde / (1000 * 60 * 60 * 24)) % 365;
        long années = (differenceDansLeTempsEnMilliseconde / (1_000L * 60 * 60 * 24 * 365));
        double tauxDappreciationJournalier = (tauxDAppreciationAnnuelle / 12) / 30;
        double tauxTotal = Math.abs((tauxDAppreciationAnnuelle * années) + (tauxDappreciationJournalier * jours));
        return (int) (valeurComptable * tauxTotal);

    }
}
