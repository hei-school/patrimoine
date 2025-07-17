package school.hei.patrimoine.modele.possession;


import lombok.Getter;
import school.hei.patrimoine.modele.Argent;
import java.time.LocalDate;

import static school.hei.patrimoine.modele.possession.TypeAgregat.ENTREPRISE;

@Getter
public final class Entreprise extends Possession{


    public Entreprise(
        String nom,
        LocalDate t,
        Argent valeurComptable,
        Argent valeurMarcheInitiale) {
      super(nom, t, valeurComptable, valeurMarcheInitiale);
    }

    @Override
    public Entreprise projectionFuture(LocalDate tFutur) {
        Argent valeurMarcheFuture = valeurMarcheALaDate(tFutur);
        Entreprise eFuture = new Entreprise(nom, tFutur, valeurComptable, valeurMarcheFuture);
        eFuture.historiqueValeurMarche.putAll(historiqueValeurMarche);
        return eFuture;
    }

    @Override
    public TypeAgregat typeAgregat() {
        return ENTREPRISE;
    }
}
