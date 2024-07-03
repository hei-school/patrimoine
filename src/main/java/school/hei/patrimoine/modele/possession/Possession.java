package school.hei.patrimoine.modele.possession;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import java.util.Map;

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

  public final int valeurComptableFuture(LocalDate tFutur) {
    return projectionFuture(tFutur).getValeurComptable();
  }

  public abstract Possession projectionFuture(LocalDate tFutur);

  // New method to get value in a specific currency
  public int getValeurComptable(String devise, LocalDate date, Map<String, Double> tauxChange, Map<String, Double> tauxAppreciation) {
    return getValeurComptable(); // Default implementation, override in subclasses that handle different currencies
  }
}
