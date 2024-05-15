package school.hei.patrimoine.possession;


import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public final class Materiel extends Possession {
    private final double tauxDAppreciationAnnuelle;

    public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
        super(nom, t, valeurComptable);
        this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
    }

    @Override
    public Possession projectionFuture(Instant tFutur) {
        long differenceAnnee = ChronoUnit.YEARS.between(t.atZone(ZoneId.systemDefault()), tFutur.atZone(ZoneId.systemDefault()));
        long differenceMois = ChronoUnit.MONTHS.between(t.atZone(ZoneId.systemDefault()), tFutur.atZone(ZoneId.systemDefault()));
        int valeurComptableFutur;
        switch ((int) differenceAnnee) {
            case 0:
                valeurComptableFutur = (int) (valeurComptable + (valeurComptable * (tauxDAppreciationAnnuelle * differenceMois)));
                break;
            default:
                valeurComptableFutur = (int) (valeurComptable + (valeurComptable * (tauxDAppreciationAnnuelle * differenceAnnee)));
        }

        return new Materiel(
                this.nom,
                tFutur,
                valeurComptableFutur,
                this.tauxDAppreciationAnnuelle
        );
    }
}
