package school.hei.patrimoine.modele.vente;

import java.time.LocalDate;
import school.hei.patrimoine.modele.Argent;

public record ValeurMarche(LocalDate t, Argent valeur) {
  public ValeurMarche(Vendable vendable, LocalDate t, Argent valeur) {
    this(t, valeur);
    vendable.addValeurMarche(this);
  }
}
