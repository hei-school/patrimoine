package school.hei.patrimoine.modele.possession;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
public sealed abstract class Possession implements Serializable /*note(no-serializable)*/ permits
    Argent, FluxArgent, TransfertArgent,
    Materiel, AchatMaterielAuComptant,
    GroupePossession {
  protected final String nom;
  protected final LocalDate t;
  protected final int valeurComptable;
  protected final Devise devise;

  public final int valeurComptableFuture(LocalDate tFutur, Devise devise) {
    return projectionFuture(tFutur, devise).getValeurComptable();
  }

  public abstract Possession projectionFuture(LocalDate tFutur, Devise devise);
}
