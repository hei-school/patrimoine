package school.hei.patrimoine.modele;

import school.hei.patrimoine.modele.possession.Possession;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

  public Possession possessionParNom(String nom) {
    return possessions.stream().filter(p -> nom.equals(p.getNom())).findFirst().orElseThrow();
  }

  public double getValeurComptableEnDevise(LocalDate tFutur, String devise, double tauxChange, double tauxAppreciationAnnuelle) {
    double valeurComptable = projectionFuture(tFutur).getValeurComptable();
    long joursEcoules = ChronoUnit.DAYS.between(t, tFutur);
    double tauxAppreciationJournalier = Math.pow(1 + tauxAppreciationAnnuelle, 1.0 / 365) - 1;
    double tauxChangeFutur = tauxChange * Math.pow(1 + tauxAppreciationJournalier, joursEcoules);
    return valeurComptable / tauxChangeFutur;
  }

}
