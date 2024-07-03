package school.hei.patrimoine.modele.possession;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;

@EqualsAndHashCode
@Getter
public sealed abstract class Possession implements Serializable /*note(no-serializable)*/ permits
        Argent, FluxArgent, TransfertArgent,
        Materiel, AchatMaterielAuComptant,
        GroupePossession {

  protected final String nom;
  protected final LocalDate t;
  protected final int valeurComptable;

  protected Possession(String nom, LocalDate t, int valeurComptable) {
    this.nom = nom;
    this.t = t;
    this.valeurComptable = valeurComptable;
  }

  public final int valeurComptableFuture(LocalDate tFutur) {
    return projectionFuture(tFutur).getValeurComptable();
  }

  public abstract Possession projectionFuture(LocalDate tFutur);

  public String getNom() {
    return nom;
  }

  public int getValeurComptable() {
    return valeurComptable;
  }
}
