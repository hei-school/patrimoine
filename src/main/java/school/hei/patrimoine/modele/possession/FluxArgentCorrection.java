package school.hei.patrimoine.modele.possession;

import school.hei.patrimoine.modele.Argent;

import java.time.LocalDate;

import static school.hei.patrimoine.modele.possession.TypeAgregat.CORRECTION;

public final class FluxArgentCorrection extends FluxArgent {
    public FluxArgentCorrection(String nom, Compte compte, LocalDate debut, LocalDate fin, int dateOperation, Argent fluxMensuel) {
        super(nom, compte, debut, fin, dateOperation, fluxMensuel);
    }

    public FluxArgentCorrection(String nom, Compte compte, LocalDate date, Argent montant) {
        super(nom, compte, date, montant);
    }

    @Override
    public TypeAgregat typeAgregat(){
        return CORRECTION;
    }
}
