package school.hei.patrimoine.possession;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

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
        this.financePar = financePar;
        this.dateDePonction = dateDePonction;
    }

    @Override
    public Possession projectionFuture(Instant tFutur) {
        var anneesEntre = ChronoUnit.MONTHS.between(
                LocalDateTime.ofInstant(this.debut, ZoneId.of("UTC")),
                LocalDateTime.ofInstant(tFutur, ZoneId.of("UTC"))
        );
        var totalDepenses = depensesMensuelle * (int) anneesEntre;
        var valeurDisponible = financePar.getValeurComptable() - totalDepenses;
        var argent = new Argent(
                financePar.getNom(),
                tFutur,
                valeurDisponible
        );
        return new TrainDeVie(
                this.nom,
                depensesMensuelle,
                debut,
                fin,
                argent,
                dateDePonction
        );
    }
}
