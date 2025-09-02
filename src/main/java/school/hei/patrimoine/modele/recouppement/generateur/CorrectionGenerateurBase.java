package school.hei.patrimoine.modele.recouppement.generateur;

import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Possession;

public abstract class CorrectionGenerateurBase<T extends Possession> implements CorrectionGenerateur<T> {
    protected boolean memeDate(T prévu, T réalité) {
        return prévu.t().equals(réalité.t());
    }

    protected boolean memeValeur(T prévu, T réalité) {
        return getValeur(prévu).equals(getValeur(réalité));
    }

    protected Argent getValeur(T possession){
        return  possession.valeurComptable();
    }
}
