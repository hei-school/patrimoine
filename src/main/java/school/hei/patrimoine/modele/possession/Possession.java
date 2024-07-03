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

  public final int valeurComptableFuture(LocalDate tFutur) {
    return projectionFuture(tFutur).getValeurComptable();
  }

  public abstract Possession projectionFuture(LocalDate tFutur);

  public double tauxDeChange(String deviseSource, String deviseCible) {
    if (deviseCible == deviseSource) {
      return 1.0;
    }
    if (deviseSource.equals(Devise.EURO) && deviseCible.equals(Devise.ARIARY)) {
      return 4821;
    } else if (deviseSource.equals(Devise.ARIARY) && deviseCible.equals(Devise.EURO)) {
      return 1 / 4821;
    }
      return 0;
  }
}
