package school.hei.patrimoine.modele.vente;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;

@Getter
@Setter
public class InformationDeVente implements Serializable {
  private final Set<ValeurMarchee> valeurMarches;
  private Argent valeurDeVente;
  private LocalDate dateDeVente;
  private Compte compteBeneficiaire;

  public InformationDeVente() {
    this.valeurMarches = new HashSet<>();
    this.valeurDeVente = null;
    this.dateDeVente = null;
    this.compteBeneficiaire = null;
  }

  public void addValeurMarche(ValeurMarchee v) {
    this.valeurMarches.add(v);
  }

  public ValeurMarchee getValeurMarche(LocalDate t) {
    return getValeurMarches().stream()
        .filter(v -> v.t().isBefore(t) || v.t().isEqual(t))
        .sorted(Comparator.comparing(ValeurMarchee::t))
        .findFirst()
        .orElse(new ValeurMarchee(t, Argent.euro(0)));
  }

  public boolean estVendue() {
    return valeurDeVente != null && dateDeVente != null && compteBeneficiaire != null;
  }

  public void confirmeVente(
      Possession possessionVendue,
      Argent valeurDeVente,
      LocalDate dateDeVente,
      Compte compteBeneficiaire) {
    this.valeurDeVente = valeurDeVente;
    this.dateDeVente = dateDeVente;
    this.compteBeneficiaire = compteBeneficiaire;
    new FluxArgent(
        String.format("Vente de %s", possessionVendue.nom()),
        compteBeneficiaire,
        dateDeVente,
        valeurDeVente);
  }
}
