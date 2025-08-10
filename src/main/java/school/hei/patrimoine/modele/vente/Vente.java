package school.hei.patrimoine.modele.vente;

import java.time.LocalDate;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Possession;

public record Vente(Possession possession, LocalDate date, Argent prix, Compte compteBeneficiaire) {

  public Vente(Possession possession, LocalDate date, Argent prix, Compte compteBeneficiaire) {
    this.possession = possession;
    this.date = date;
    this.prix = prix;
    this.compteBeneficiaire = compteBeneficiaire;

    possession.vendre(date, prix, compteBeneficiaire);
  }
}
