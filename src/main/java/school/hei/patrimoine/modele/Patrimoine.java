package school.hei.patrimoine.modele;

import school.hei.patrimoine.modele.possession.Possession;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public record Patrimoine(
        String nom, Personne possesseur, LocalDate t, Set<Possession> possessions, String deviseActuelle)
        implements Serializable/*note(no-serializable)*/ {

  public int getValeurComptable() {
    return possessions.stream().mapToInt(Possession::getValeurComptable).sum();
  }

  public Patrimoine projectionFuture(LocalDate tFutur) {
    return new Patrimoine(
            nom,
            possesseur,
            tFutur,
            possessions.stream().map(p -> p.projectionFuture(tFutur)).collect(toSet()),
            deviseActuelle);
  }

  public Possession possessionParNom(String nom) {
    return possessions.stream().filter(p -> nom.equals(p.getNom())).findFirst().orElseThrow();
  }

  public double getValeurEnDevise(String deviseCible, LocalDate date, double tauxChange, double tauxAppreciationAnnuel) {
    double valeurEnDevise = 0.0;
    for (Possession possession : possessions) {
      valeurEnDevise += convertirEnDevise(possession.getValeurComptable(), date, tauxChange, tauxAppreciationAnnuel);
    }
    return valeurEnDevise;
  }

  private double convertirEnDevise(int valeur, LocalDate date, double tauxChange, double tauxAppreciationAnnuel) {
    long joursEntre = t.toEpochDay() - date.toEpochDay();
    double appreciation = Math.pow(1 + tauxAppreciationAnnuel, -joursEntre / 365.0);
    return valeur * tauxChange * appreciation;
  }

  public Patrimoine avecPossession(Possession nouvellePossession) {
    Set<Possession> nouvellesPossessions = Set.copyOf(possessions);
    nouvellesPossessions.add(nouvellePossession);
    return new Patrimoine(nom, possesseur, t, nouvellesPossessions, deviseActuelle);
  }
}
