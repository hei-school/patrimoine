package school.hei.patrimoine.modele.possession;

import java.time.LocalDate;

import lombok.Getter;
import school.hei.patrimoine.modele.Argent;

@Getter
public final class Entreprise extends Possession {
    public Entreprise(String nom, LocalDate t, Argent valeurComptable) {
        super(nom, t, valeurComptable);
    }

    @Override
    public Entreprise projectionFuture(LocalDate tFutur) {
        return new Entreprise(nom, tFutur, valeurComptable);
    }

    @Override
    public TypeAgregat typeAgregat() {
        return TypeAgregat.ENTREPRISE;
    }
}