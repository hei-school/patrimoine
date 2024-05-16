package school.hei.patrimoine.possession;

import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

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
        LocalDate debutEnLocalDate = this.debut.atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate tFuturEnLocalDate = tFutur.atZone(ZoneId.systemDefault()).toLocalDate();
        int nombreDePonction = Period.between(debutEnLocalDate, tFuturEnLocalDate).getMonths();

        if (this.dateDePonction <= tFuturEnLocalDate.getDayOfMonth() &&
                tFuturEnLocalDate.getDayOfMonth() < debutEnLocalDate.getDayOfMonth()) {
            nombreDePonction += 1;
        }

        int valeurComptableFuture = this.financePar.getValeurComptable() - (this.depensesMensuelle * nombreDePonction);
        Argent financeParFutur = new Argent(
                this.financePar.getNom(),
                this.financePar.getT(),
                valeurComptableFuture
        );
        return new TrainDeVie(
                this.nom,
                this.depensesMensuelle,
                this.debut,
                this.fin,
                financeParFutur,
                this.dateDePonction);
    }
}
