package school.hei.patrimoine.modele.possession;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

  public final int valeurComptableFuture(LocalDate tFutur) {
    return projectionFuture(tFutur).getValeurComptable();
  }

  public abstract Possession projectionFuture(LocalDate tFutur);

  public double convertirValeurComptable(LocalDate dateConversion, String deviseCible, Map<String, Double> tauxDeChange, Map<String, Double> tauxAppreciation) {
    String devise = ""; // You need to initialize this variable with the correct devise
    if (devise.equals(deviseCible)) {
      return valeurComptable;
    }

    String key = devise + "_" + deviseCible;
    Double tauxConversion = tauxDeChange.get(key);
    Double tauxAppreciationAnnuelle = tauxAppreciation.get(key);

    if (tauxConversion == null || tauxAppreciationAnnuelle == null) {

      throw new RuntimeException("Taux de change or taux d'appreciation not found for " + key);
    }

    double joursEcoules = ChronoUnit.DAYS.between(t, dateConversion);

    return valeurComptable * tauxConversion * Math.pow(1 + tauxAppreciationAnnuelle / 365, joursEcoules);
  }
}
