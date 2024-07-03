package school.hei.patrimoine.modele;

import java.time.LocalDate;

public class TauxChange {
    private Devise from;
    private Devise to;
    private LocalDate date;
    private double taux;
    private double tauxAppreciationAnnuel;

    public TauxChange(Devise from, Devise to, LocalDate date, double taux, double tauxAppreciationAnnuel) {
        this.from = from;
        this.to = to;
        this.date = date;
        this.taux = taux;
        this.tauxAppreciationAnnuel = tauxAppreciationAnnuel;
    }

    public double getTaux(LocalDate dateCalcul) {
        long daysBetween = ChronoUnit.DAYS.between(date, dateCalcul);
        double yearsElapsed = daysBetween / 365.0;
        return taux * Math.pow(1 + tauxAppreciationAnnuel, yearsElapsed);
    }
}
