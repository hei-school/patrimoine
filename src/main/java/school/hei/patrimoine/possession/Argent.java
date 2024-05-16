package school.hei.patrimoine.possession;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.time.temporal.ChronoUnit;
import static java.util.stream.Collectors.toSet;

public final class Argent extends Possession {
    private final Set<TrainDeVie> financés;

    public Argent(String nom, Instant t, int valeurComptable) {
        this(nom, t, valeurComptable, new HashSet<>());
    }

    private Argent(String nom, Instant t, int valeurComptable, Set<TrainDeVie> financés) {
        super(nom, t, valeurComptable);
        this.financés = financés;
    }

    @Override
    public Argent projectionFuture(Instant tFutur) {
        return new Argent(
                nom,
                tFutur,
                valeurComptable - financementsFutur(tFutur),
                financés.stream().map(f -> f.projectionFuture(tFutur)).collect(toSet()));
    }

    private int financementsFutur(Instant tFutur) {
        return financés.stream()
                .mapToInt(f -> {
                    long differenceJour = ChronoUnit.DAYS.between(f.getDebut(), tFutur);
                    long moisEntre = differenceJour / 30;
                    return (int) (moisEntre * f.getDepensesMensuelle());
                })
                .sum();
    }
    void addFinancés(TrainDeVie trainDeVie) {
        financés.add(trainDeVie);
    }
}