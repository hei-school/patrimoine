package school.hei.patrimoine.modele.ValeurMarche;

import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Possession;

import java.time.LocalDate;

public record ValeurMarche(Possession possession, LocalDate t, Argent valeur) {

    public ValeurMarche(Possession possession, LocalDate t, Argent valeur) {
        this.possession = possession;
        this.t = t;
        this.valeur = valeur;

        possession.addValeurMarche(t, valeur);
    }

}
