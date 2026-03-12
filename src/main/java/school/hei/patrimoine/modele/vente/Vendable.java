package school.hei.patrimoine.modele.vente;

import java.time.LocalDate;
import java.util.Set;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;

public interface Vendable {
  boolean estVendue();

  Argent getValeurDeVente();

  LocalDate getDateDeVente();

  Compte getCompteBeneficiaire();

  void addValeurMarche(ValeurMarche v);

  Set<ValeurMarche> getValeurMarches();

  ValeurMarche getValeurMarche(LocalDate t);

  void vendre(Argent valeurDeVente, LocalDate dateDeVente, Compte compteBeneficiaire);
}
