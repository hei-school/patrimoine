package school.hei.patrimoine.possession;

import lombok.Getter;
import lombok.ToString;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
@ToString
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
        super(nom, financePar.t, financePar.getValeurComptable()); //TODO: dirty, redesign
        this.debut = debut;
        this.fin = fin;
        this.depensesMensuelle = depensesMensuelle;
        this.financePar = financePar;
        this.dateDePonction = dateDePonction;
    }

    @Override
    public Possession projectionFuture(Instant tFutur) {
        long differenceMois = ChronoUnit.MONTHS.between(t.atZone(ZoneId.systemDefault()), tFutur.atZone(ZoneId.systemDefault()))+1;
        int valeurComptableFuture= (int) (valeurComptable - (depensesMensuelle * differenceMois));


        return new TrainDeVie(
                nom,
                depensesMensuelle,
                debut,
                fin,
                new Argent(
                        this.nom,
                        tFutur,
                        valeurComptableFuture

                ),
                dateDePonction
        );
    }


}
