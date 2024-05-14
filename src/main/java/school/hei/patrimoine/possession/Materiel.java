package school.hei.patrimoine.possession;

import school.hei.NotImplemented;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public final class Materiel extends Possession {
    private  final double tauxDAppreciationAnnuel;

    public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuel) {
        super(nom, t, valeurComptable);
        this.tauxDAppreciationAnnuel = tauxDAppreciationAnnuel;
    }

    @Override
    public int valeurComptableFuture(Instant tFutur) {
        double tauxDAppreciationJournaliere = tauxDAppreciationAnnuel / 12 * 30;

        double nombreDeJoursCumulesDepuisLAchat = ChronoUnit.DAYS.between(t, tFutur);
        double tauxDAppreciationCumule = (tauxDAppreciationJournaliere * nombreDeJoursCumulesDepuisLAchat) + 1;

        double valeurComptableFuture = valeurComptable * tauxDAppreciationCumule;

        return (int) Math.round((valeurComptableFuture));

    }
}
