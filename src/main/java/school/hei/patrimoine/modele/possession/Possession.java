package school.hei.patrimoine.modele.possession;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;

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

  public double convertirValeurComptable(LocalDate dateConversion, String deviseCible, Map<String, Double> tauxDeChange, Map<String, Double> tauxAppreciation) {
    if (devise.equals(deviseCible)) {
      return valeurComptable;
    }

    double tauxConversion = tauxDeChange.get(devise + "_" + deviseCible);
    double tauxAppreciationAnnuelle = tauxAppreciation.get(devise + "_" + deviseCible);
    double joursEcoules = java.time.temporal.ChronoUnit.DAYS.between(t, dateConversion);

    return valeurComptable * tauxConversion * Math.pow(1 + tauxAppreciationAnnuelle / 365, joursEcoules);
  }
}
