package school.hei.patrimoine.modele;

import lombok.AllArgsConstructor;

import java.time.LocalDate;

import static java.lang.Math.max;
import static java.time.temporal.ChronoUnit.DAYS;

@AllArgsConstructor
public class Monnaie {
    private String name;
    private int valeur;
    private LocalDate date;
    private final double tauxDAppreciationAnnuelle;

    public Monnaie projectionFutur(LocalDate tFutur) {
        if (tFutur.isBefore(date)) {
            return new Monnaie(name, valeur, tFutur, tauxDAppreciationAnnuelle);
        }
        var joursEcoules = DAYS.between(date, tFutur);
        double valeurAjouteeJournaliere = valeur * (tauxDAppreciationAnnuelle / 365.);
        int valeurComptableFuture = max(0, (int) (valeur + valeurAjouteeJournaliere * joursEcoules));
        return new Monnaie(name, valeurComptableFuture, tFutur, tauxDAppreciationAnnuelle);
    }

    public int convert(int argent) {
        return argent / valeur;
    }

    public int convert(int argent, Monnaie autreDevise) {
        return convert(argent) * autreDevise.valeur;
    }
}
