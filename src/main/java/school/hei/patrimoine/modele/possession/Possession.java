package school.hei.patrimoine.modele.possession;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Currency;

@EqualsAndHashCode
@Getter

public abstract sealed class Possession implements Serializable permits
        Argent, FluxArgent, TransfertArgent,
        Materiel, AchatMaterielAuComptant,
        GroupePossession {

  protected final String nom;
  protected final LocalDate t;
  protected final int valeurComptable;
  protected final String devise;

  public Possession(String nom, LocalDate t, int valeurComptable, String devise) {
    this.nom = nom;
    this.t = t;
    this.valeurComptable = valeurComptable;
    this.devise = devise;
  }

  public int getValeurComptable() {
    return valeurComptable;
  }

  public int getValeurComptableEnDevise(String codeDevise, double tauxChange, double tauxAppreciationAnnuel) {
    double tauxChangeLisse = Math.pow(1 + tauxAppreciationAnnuel, ChronoUnit.DAYS.between(LocalDate.now(), t));
    return (int) Math.round(valeurComptable * tauxChange * tauxChangeLisse);
  }

  public abstract Possession projectionFuture(LocalDate tFutur);

  public final int valeurComptableFuture(LocalDate tFutur) {
    return projectionFuture(tFutur).getValeurComptable();
  }

}
