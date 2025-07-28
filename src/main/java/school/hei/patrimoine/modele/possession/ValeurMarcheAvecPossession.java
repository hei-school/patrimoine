package school.hei.patrimoine.modele.possession;

import java.time.LocalDate;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.vente.ValeurMarche;

public final class ValeurMarcheAvecPossession {
  private final Possession possession;
  private final ValeurMarche valeurMarche;

  public ValeurMarcheAvecPossession(Possession possession, LocalDate t, Argent valeur) {
    this.possession = possession;
    this.valeurMarche = new ValeurMarche(t, valeur);
    this.possession.ajouterValeurMarche(this.valeurMarche);
  }
}
