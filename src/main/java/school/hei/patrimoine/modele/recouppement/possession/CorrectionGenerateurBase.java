package school.hei.patrimoine.modele.recouppement.possession;

import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;

import java.util.Set;

@RequiredArgsConstructor
public class CorrectionGenerateurBase<T extends Possession> implements CorrectionGenerateur {
    private final T prévu;
    private final T réalité;

    @Override
    public Set<Correction> get() {
        var t = prévu.t();
        var nom = prévu.nom();
        //TODO: check account
        var compteCorrection = prévu.getCompteCorrection().getCompte();
        var diffEntreValeurComptable = prévu.valeurComptable().minus(réalité.valeurComptable(), t);

        if(memeDate(prévu, réalité)){
           if(memeValeurComptable(prévu, réalité)) {
               return Set.of();
           }

           return Set.of(
                new Correction(new FluxArgent("Correction_de_valeur_pour_" + nom, compteCorrection, t, diffEntreValeurComptable))
           );
        }

        throw new RuntimeException("Not Implemented");
    }

    protected static boolean memeDate(Possession prévu, Possession réalité){
       return prévu.t().equals(réalité.t());
    }

    protected static boolean memeValeurComptable(Possession prévu, Possession réalité){
        return prévu.valeurComptable().equals(réalité.valeurComptable());
    }
}
