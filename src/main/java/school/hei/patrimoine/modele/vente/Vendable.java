package school.hei.patrimoine.modele.vente;

import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.objectif.Objectif;
import school.hei.patrimoine.modele.possession.Compte;

import java.time.LocalDate;
import java.util.Set;

public interface Vendable {
  public Set<Objectif> getObjectifs();

  public Set<ValeurMarche> getValeurMarches();

  public ValeurMarche getValeurMarche(LocalDate t);

  public void addValeurMarche(ValeurMarche v);

  public Argent getValeurDeVente();

  public LocalDate getDateDeVente();

  public Compte getCompteBeneficiaire();

  public boolean estVendue();

  public void vendre(Argent valeurDeVente, LocalDate dateDeVente);
}
