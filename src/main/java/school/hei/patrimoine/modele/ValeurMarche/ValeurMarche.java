package school.hei.patrimoine.modele.ValeurMarche;

import java.time.LocalDate;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Possession;

public record ValeurMarche(Possession possession, LocalDate t, Argent valeur) {

  public ValeurMarche(Possession possession, LocalDate t, Argent valeur) {
    this.possession = possession;
    this.t = t;
    this.valeur = valeur;

    possession.addValeurMarche(t, valeur);
  }
}
