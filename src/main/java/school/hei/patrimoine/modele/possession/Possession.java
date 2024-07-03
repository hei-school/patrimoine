package school.hei.patrimoine.modele.possession;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import school.hei.patrimoine.modele.Devise;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
public abstract sealed class Possession implements Serializable /*note(no-serializable)*/
    permits Argent,
        FluxArgent,
        TransfertArgent,
        Materiel,
        AchatMaterielAuComptant,
        GroupePossession {
  protected final String nom;
  protected final LocalDate t;
  protected final int valeurComptable;

  public final int valeurComptableFuture(LocalDate tFutur) {
    return projectionFuture(tFutur).getValeurComptable();
  }

  public final double valeurComptableFuture(
      LocalDate tFutur,
      Devise deviseSource,
      Devise deviseDestination,
      double tauxDAppreciationAnnuelle) {
    return deviseDestination.from(
        projectionFuture(tFutur).getValeurComptable(),
        deviseSource,
        tFutur,
        tauxDAppreciationAnnuelle);
  }

  public abstract Possession projectionFuture(LocalDate tFutur);
}
