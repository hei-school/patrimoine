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
  protected int valeurComptable;

  public final int valeurComptableFuture(LocalDate tFutur) {
    return projectionFuture(tFutur).getValeurComptable();
  }

  public abstract Possession projectionFuture(LocalDate tFutur);

  public Possession convertirEnDevise(String nouvelleDevise) {
    throw new UnsupportedOperationException("Conversion de devise non supportée pour cette possession.");
  }
}

