package school.hei.patrimoine.possession;


import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Getter
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
        int ponctuationDate = 1;
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate tPremierMois = LocalDate
                .ofInstant(t, zoneId)
                .withDayOfMonth(ponctuationDate);
        LocalDate tFuturPremierMois = LocalDate
                .ofInstant(tFutur, zoneId)
                .withDayOfMonth(ponctuationDate);

        int differenceMois = (int) (ChronoUnit.MONTHS.between(tPremierMois, tFuturPremierMois));
        return financés
                .stream()
                .mapToInt(t -> t
                                       .projectionFuture(tFutur)
                                       .getDepensesMensuelle() * differenceMois)
                .sum();
    }

    void addFinancés(TrainDeVie trainDeVie) {
        financés.add(trainDeVie);
    }
}
