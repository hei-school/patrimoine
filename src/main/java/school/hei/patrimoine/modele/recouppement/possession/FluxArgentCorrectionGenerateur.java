package school.hei.patrimoine.modele.recouppement.possession;

import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.FluxArgent;

import java.util.Set;

public class FluxArgentCorrectionGenerateur extends CorrectionGenerateurBase<FluxArgent> {
    @Override
    public Set<Correction> nonPrévu(FluxArgent nonPrévu){
        return Set.of();
    }

    @Override
    public Set<Correction> nonExecuté(FluxArgent nonExecuté) {
        var compte = nonExecuté.getCompte();
        return Set.of(new Correction(new FluxArgent("non_execute_", compte, nonExecuté.t(), nonExecuté.getFluxMensuel())));
    }

    @Override
    public Set<Correction> comparer(FluxArgent prévu, FluxArgent réalité) {
        var compte= prévu.getCompte();
        if(memeDate(prévu, réalité)){
            if(memeFluxMensuel(prévu, réalité)){
                return Set.of();
            }

            var fluxMensuelDiff = réalité.getFluxMensuel().minus(prévu.getFluxMensuel(), prévu.t());
            return Set.of(new Correction(
                new FluxArgent(prévu.nom(), compte, prévu.t(), prévu.getFin(), prévu.getDateOperation(), fluxMensuelDiff)
            ));
        }

        return Set.of(
            new Correction(new FluxArgent("retard" + prévu.nom(), compte, prévu.t(), prévu.getFluxMensuel())),
            new Correction(new FluxArgent("retard_pair" + prévu.nom(), compte, prévu.t(), réalité.getFluxMensuel().mult(-1)))
        );
    }

    private boolean memeFluxMensuel(FluxArgent prévu, FluxArgent réalité){
        return prévu.getFluxMensuel().equals(réalité.getFluxMensuel());
    }
}
