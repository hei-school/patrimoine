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
  protected final String devise;


  public final int valeurComptableFuture(LocalDate tFutur) {
    return projectionFuture(tFutur).getValeurComptable();
  }

  public abstract Possession projectionFuture(LocalDate tFutur);

  public double convertValeurComptable(String targetDevise, double tauxChange, double tauxAppreciation, long jours) {
    if (this.devise.equals(targetDevise)) {
      return valeurComptable;
    } else {
      double tauxConversion = tauxChange * Math.pow(1 + tauxAppreciation, jours / 365.0);
      return valeurComptable / tauxConversion;
    }
  }
}
