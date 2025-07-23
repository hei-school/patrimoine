package school.hei.patrimoine.modele.vente;

import java.time.LocalDate;
import java.util.Set;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.objectif.Objectif;
import school.hei.patrimoine.modele.possession.Compte;

public interface Vendable {
  public Set<Objectif> getObjectifs();

  public Set<ValeurMarchee> getValeurMarches();

  public ValeurMarchee getValeurMarche(LocalDate t);

  public void addValeurMarche(ValeurMarchee v);

  public Argent getValeurDeVente();

  public LocalDate getDateDeVente();

  public Compte getCompteBeneficiaire();

  public boolean estVendue();

  public void vendre(Argent valeurDeVente, LocalDate dateDeVente, Compte compteBeneficiaire);
}
