package school.hei.patrimoine.modele;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class TauxDeChange {
    private Devise source;
    private Devise destination;
    private double taux;
    private double tauxAppreciationAnnuelle;

    public TauxDeChange(Devise source, Devise destination, double taux, double tauxAppreciationAnnuelle) {
        this.source = source;
        this.destination = destination;
        this.taux = taux;
        this.tauxAppreciationAnnuelle = tauxAppreciationAnnuelle;
    }

    public double convertion(double montant, LocalDate dateInitiale, LocalDate dateFinale) {
        long jours = ChronoUnit.DAYS.between(dateInitiale, dateFinale);
        double appreciationTotale = Math.pow(1 + tauxAppreciationAnnuelle, jours / 365.0);
        return montant * taux * appreciationTotale;
    }
}