package school.hei.patrimoine.modele;
import school.hei.patrimoine.modele.possession.Possession;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public record Patrimoine(
        String nom, Personne possesseur, LocalDate t, Set<Possession> possessions)
        implements Serializable {

  public int getValeurComptable() {
    return possessions.stream().mapToInt(Possession::getValeurComptable).sum();
  }

  public int getValeurComptableEnDevise(String devise, double tauxChange, double appreciationAnnuelle) {
    int valeurComptableTotale = 0;
    for (Possession possession : possessions) {
      int valeur = possession.getValeurComptable();
      valeurComptableTotale += convertirEnDevise(valeur, devise, tauxChange, appreciationAnnuelle);
    }
    return valeurComptableTotale;
  }

  private int convertirEnDevise(int valeur, String devise, double tauxChange, double appreciationAnnuelle) {
    double tauxJournalier = tauxChange * Math.pow(1 + appreciationAnnuelle / 365, LocalDate.now().until(t).getDays());
    return (int) Math.round(valeur / tauxJournalier);
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
}
