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
    public Materiel projectionFuture(Instant tFutur) {
        double differenceJour = Duration.between(t, tFutur).toDays();
        int valeurAjoute = (int) (valeurComptable * ((tauxDAppreciationAnnuelle * differenceJour) / 365));
        int valeurComptableFutur = valeurComptable + valeurAjoute;

        return new Materiel(
                nom,
                tFutur,
                valeurComptableFutur,
                tauxDAppreciationAnnuelle
        );
    }
}
