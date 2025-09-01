package school.hei.patrimoine.modele.recouppement.possession;

import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;

public class CorrectionGenerateurFactory {
    @SuppressWarnings("unchecked")
    public static <T extends Possession> CorrectionGenerateur<T> make(T possession){
        if(possession instanceof FluxArgent){
            return (CorrectionGenerateur<T>) new FluxArgentCorrectionGenerateur();
        }

        return new CorrectionGenerateurBase<>();
    }
}
