package school.hei.patrimoine.modele.vente;

import lombok.Getter;
import lombok.Setter;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class InformationDeVente {
  private final List<ValeurMarche> valeursMarches;
  private Argent valeurDeVente;
  private LocalDate dateDeVente;
  private Compte compteBeneficiaire;

  public InformationDeVente() {
    this.valeursMarches = new ArrayList<>();
    this.valeurDeVente = null;
    this.dateDeVente = null;
    this.compteBeneficiaire = null;
  }

  public void addValeurMarche(ValeurMarche v) {
    this.valeursMarches.add(v);
  }

  public Set<ValeurMarche> getValeurMarches() {
    return Set.copyOf(valeursMarches);
  }

  public ValeurMarche getValeurMarche(LocalDate t) {
    return getValeurMarches().stream()
        .filter(v -> v.t().isBefore(t) || v.t().isEqual(t))
        .sorted(Comparator.comparing(ValeurMarche::t))
        .toList().getFirst();
  }

  public boolean estVendue() {
    return valeurDeVente != null && dateDeVente != null && compteBeneficiaire != null;
  }

  public void confirmeVente(Argent valeurDeVente, LocalDate dateDeVente, Compte compteBeneficiaire) {
    this.valeurDeVente = valeurDeVente;
    this.dateDeVente = dateDeVente;
    this.compteBeneficiaire = compteBeneficiaire;
  }
}

