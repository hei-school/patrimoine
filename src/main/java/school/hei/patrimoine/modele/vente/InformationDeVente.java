package school.hei.patrimoine.modele.vente;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;

@Getter
@Setter
@EqualsAndHashCode
public class InformationDeVente {
  private Argent valeurDeVente;
  private LocalDate dateDeVente;
  private Compte compteBeneficiaire;
  private final Set<ValeurMarche> valeurMarches;

  public InformationDeVente() {
    this.dateDeVente = null;
    this.valeurDeVente = null;
    this.compteBeneficiaire = null;
    this.valeurMarches = new HashSet<>();
  }

  public void addValeurMarche(ValeurMarche v) {
    this.valeurMarches.add(v);
  }

  public ValeurMarche getValeurMarche(LocalDate t) {
    return getValeurMarches().stream()
        .filter(v -> v.t().isBefore(t) || v.t().isEqual(t))
        .sorted(Comparator.comparing(ValeurMarche::t))
        .findFirst()
        .orElse(new ValeurMarche(t, Argent.ariary(0)));
  }

  public boolean estVendue() {
    return valeurDeVente != null && dateDeVente != null && compteBeneficiaire != null;
  }
}
