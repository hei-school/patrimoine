package school.hei.patrimoine.modele.vente;

import java.time.LocalDate;
import school.hei.patrimoine.modele.Argent;

public record ValeurMarchee(LocalDate t, Argent valeur) {
  public ValeurMarchee(Vendable vendable, LocalDate t, Argent valeur) {
    this(t, valeur);
    vendable.addValeurMarche(this);
  }
}
