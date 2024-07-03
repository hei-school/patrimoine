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
  protected String devise;

  public Possession(String nom, LocalDate t, int valeurComptable) {
    this.nom = nom;
    this.t = t;
    this.valeurComptable = valeurComptable;
  }

  public final int valeurComptableFuture(LocalDate tFutur) {
    return projectionFuture(tFutur).getValeurComptable();
  }

  public final int valeurComptableFuture(LocalDate tFutur, TauxDeChange tauxDeChange, String deviseCible) {
    int valeurFutur = projectionFuture(tFutur).getValeurComptable();
    double tauxSource = tauxDeChange.obtenirTauxDeChange(devise);
    double tauxCible = tauxDeChange.obtenirTauxDeChange(deviseCible);
    return (int) (valeurFutur * (tauxCible / tauxSource));
  }

  public abstract Possession projectionFuture(LocalDate tFutur);

  public abstract Possession projectionFuture(LocalDate tFutur, TauxDeChange tauxDeChange, String deviseCible);
}
