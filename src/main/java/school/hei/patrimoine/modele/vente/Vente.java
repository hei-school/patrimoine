package school.hei.patrimoine.modele.vente;

import java.time.LocalDate;

import lombok.Getter;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Possession;

@Getter
public final class Vente {
  private final Possession possessionAVendre;

  public Vente(
      Possession possessionAVendre,
      Argent valeurDeVente,
      LocalDate dateDeVente,
      Compte compteBeneficiaire) {
    this.possessionAVendre = possessionAVendre;
    this.possessionAVendre.vendre(valeurDeVente, dateDeVente, compteBeneficiaire);
  }
}
