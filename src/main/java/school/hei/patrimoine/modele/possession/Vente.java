package school.hei.patrimoine.modele.possession;

import java.time.LocalDate;
import lombok.Getter;
import school.hei.patrimoine.modele.Argent;

@Getter
public final class Vente extends Possession {
  private final Possession possessionAVendre;

  public Vente(
      LocalDate t,
      Possession possessionAVendre,
      Argent valeurDeVente,
      LocalDate dateDeVente,
      Compte compteBeneficiaire) {
    this(t, possessionAVendre, Argent.ariary(0));
    this.possessionAVendre.vendre(valeurDeVente, dateDeVente, compteBeneficiaire);
  }

  private Vente(LocalDate t, Possession possessionAVendre, Argent valeurComptable) {
    super(String.format("Vente de %s", possessionAVendre.nom()), t, valeurComptable);
    this.possessionAVendre = possessionAVendre;
  }

  @Override
  public Possession projectionFuture(LocalDate tFutur) {
    return new Vente(tFutur, getPossessionAVendre(), Argent.ariary(0));
  }

  @Override
  public TypeAgregat typeAgregat() {
    return TypeAgregat.FLUX;
  }
}
