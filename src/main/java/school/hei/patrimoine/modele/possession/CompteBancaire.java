package school.hei.patrimoine.modele.possession;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class CompteBancaire extends Possession{

    public CompteBancaire(String nom, LocalDate t, int valeurComptable) {
        super(nom, t, valeurComptable);
    }
    @Override
    public Possession projectionFuture(LocalDate tFutur) {
        long moisEcoules = ChronoUnit.MONTHS.between(t, tFutur);
        int fraisCompteTotal = (int) (moisEcoules * 20000);
        int nouvelleValeur = Math.max(0, valeurComptable - fraisCompteTotal);
        return new CompteBancaire(nom, tFutur, nouvelleValeur);
    }
}
