package school.hei.patrimoine.modele.vente;

import java.io.Serializable;
import java.time.LocalDate;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Possession;

public record ValeurMarche(Possession possession, LocalDate t, Argent valeur)
    implements Serializable {
  public ValeurMarche(Possession possession, LocalDate t, Argent valeur) {
    this.possession = possession;
    this.t = t;
    this.valeur = valeur;
    this.possession.ajouterValeurMarche(this);
  }
}
