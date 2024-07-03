package school.hei.patrimoine.modele;

import school.hei.patrimoine.modele.devise.Devise;
import school.hei.patrimoine.modele.possession.Possession;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public record Patrimoine(
    String nom, Personne possesseur, LocalDate t, Set<Possession> possessions)
    implements Serializable/*note(no-serializable)*/ {

  public int getValeurComptable() {
    return possessions.stream().mapToInt(Possession::getValeurComptable).sum();
  }

  public Patrimoine projectionFuture(LocalDate tFutur) {
    return new Patrimoine(
        nom,
        possesseur,
        tFutur,
        possessions.stream().map(p -> p.projectionFuture(tFutur)).collect(toSet()));
  }

  public double valeurComptableFuture(
          LocalDate tFutur,
          Devise deviseSource,
          Devise deviseDestination,
          double tauxDAppreciationAnnuelle ) {
    return possessions.stream().mapToDouble(possession -> possession.valeurComptableFuture(
            tFutur,
            deviseSource,
            deviseDestination,
            tauxDAppreciationAnnuelle
    )).sum();
  }

  public Possession possessionParNom(String nom) {
    return possessions.stream().filter(p -> nom.equals(p.getNom())).findFirst().orElseThrow();
  }
}
