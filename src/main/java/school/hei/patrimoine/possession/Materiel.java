package school.hei.patrimoine.possession;

import lombok.Getter;
import school.hei.patrimoine.NotImplemented;

import java.time.Instant;
import java.time.ZoneId;
@Getter
public final class Materiel extends Possession {
    private final double tauxDAppreciationAnnuelle;

    public Materiel(String nom, Instant t, int valeurComptable, double tauxDAppreciationAnnuelle) {
        super(nom, t, valeurComptable);
        this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
    }

    @Override
    public Materiel projectionFuture(Instant tFutur) {
        int jour_present = getT().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth();
        int jour_future = tFutur.atZone(ZoneId.systemDefault()).toLocalDate().getDayOfMonth();
        long jour_entre_present_fututre = jour_present - jour_future;
        long annee_entre_present_future = jour_entre_present_fututre/365;
        double appreciation = getTauxDAppreciationAnnuelle();
        double valeur_finale = getValeurComptable();
        for(int i = 0 ; i<= annee_entre_present_future;i++){
              valeur_finale +=  valeur_finale*appreciation;
        }
        return new Materiel(
                nom,
                tFutur,
                (int)valeur_finale,
                appreciation
        );
    }
}
