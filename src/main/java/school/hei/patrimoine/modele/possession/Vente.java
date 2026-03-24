package school.hei.patrimoine.modele.possession;

import java.time.LocalDate;
import lombok.Getter;
import school.hei.patrimoine.modele.Argent;

@Getter
public final class Vente {
  private final LocalDate tVente;
  private final Possession possession;
  private final Compte compteBeneficiaire;
  private final Argent prixVente;

  public Vente(
      LocalDate tVente, Possession possession, Compte compteBeneficiaire, Argent prixVente) {
    this.tVente = tVente;
    this.possession = possession;
    possession.vendre(tVente, prixVente, compteBeneficiaire);
    this.compteBeneficiaire = compteBeneficiaire;
    this.prixVente = prixVente;
  }
}
