package school.hei.patrimoine.possession;

import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Getter
public final class TrainDeVie extends Possession {
    private final Instant debut;
    private final Instant fin;
    private final int depensesMensuelle;
    private final Argent financePar;
    private final int dateDePonction;

    public TrainDeVie(
            String nom,
            int depensesMensuelle,
            Instant debut,
            Instant fin,
            Argent financePar,
            int dateDePonction) {
        super(nom, null, 0);
        this.debut = debut;
        this.fin = fin;
        this.depensesMensuelle = depensesMensuelle;
        this.dateDePonction = dateDePonction;

        this.financePar = financePar;
        this.financePar.addFinancés(this);
    }

    @Override
    public TrainDeVie projectionFuture(Instant tFutur) {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate tPremierMois = LocalDate
                .ofInstant(financePar.getT(), zoneId)
                .withDayOfMonth(dateDePonction);
        LocalDate tFuturPremierMois = LocalDate
                .ofInstant(tFutur, zoneId)
                .withDayOfMonth(dateDePonction);

        int differenceMois = (int) (ChronoUnit.MONTHS.between(tPremierMois, tFuturPremierMois));
        int valeurComptableFutur = financePar.getValeurComptable() - (differenceMois * depensesMensuelle);
        Argent futurFinancePar = new Argent(nom, tFutur, valeurComptableFutur);
        return new TrainDeVie(
                nom,
                depensesMensuelle,
                debut,
                fin,
                futurFinancePar,
                dateDePonction

        );
    }
}
