package school.hei.patrimoine.modele.possession;

import java.time.LocalDate;
import school.hei.patrimoine.modele.Argent;

public final class Vente {
  private final LocalDate tVente;
  private final Possession possession;
  private final Compte compteBeneficiaire;
  private final Argent prixVente;

  public Vente(
      String nom,
      LocalDate t,
      Argent valeurComptable,
      LocalDate tVente,
      Possession possession,
      Argent prixVente,
      Compte compteBeneficiaire) {
    super();
    this.tVente = tVente;
    this.possession = possession;
    possession.vendre(tVente, prixVente, compteBeneficiaire);
    this.compteBeneficiaire = compteBeneficiaire;
    this.prixVente = prixVente;
  }

  private Vente(
          String nom,
          LocalDate t,
          Argent valeurComptable,
          LocalDate tVente,
          Possession possession,
          Argent prixVente) {
    super();
    this.tVente = tVente;
    this.possession = possession;
    this.prixVente = prixVente;
    this.compteBeneficiaire = null;
  }
}
