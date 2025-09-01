package school.hei.patrimoine.modele.recouppement.possession;

import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;

import java.util.Set;

public class CorrectionGenerateurBase<T extends Possession> implements CorrectionGenerateur<T>{
    @Override
    public Set<Correction> comparer(T prévu, T réalité) {
        var compte = prévu.getCompteCorrection().getCompte();
        if(memeDate(prévu, réalité)){
            if(memeValeurComptable(prévu, réalité)){
                return Set.of();
            }

            var valeurComptableDiff = réalité.valeurComptable().minus(prévu.valeurComptable(), prévu.t());
            return Set.of(new Correction(
                    new FluxArgent(prévu.nom(), compte, prévu.t(), valeurComptableDiff)
            ));
        }

        return Set.of(
            new Correction(new FluxArgent("retard" + prévu.nom(), compte, prévu.t(), prévu.valeurComptable())),
            new Correction(new FluxArgent("retard_pair" + prévu.nom(), compte, prévu.t(), réalité.valeurComptable().mult(-1)))
        );
    }

    @Override
    public Set<Correction> nonExecuté(T nonExecuté) {
        var compte = nonExecuté.getCompteCorrection().getCompte();
        return Set.of(new Correction(new FluxArgent("non_execute_", compte, nonExecuté.t(), nonExecuté.valeurComptable())));
    }

    @Override
    public Set<Correction> nonPrévu(T nonPrévu) {
        return Set.of();
    }

    public boolean memeDate(Possession prévu, Possession réalité){
        return prévu.t().equals(réalité.t());
    }

    public boolean memeValeurComptable(Possession prévu, Possession réalité){
        return prévu.valeurComptable().equals(réalité.valeurComptable());
    }
}
