package school.hei.patrimoine.modele.recouppement;

import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Devise.MGA;

import java.time.LocalDate;
import java.util.Set;
import lombok.Builder;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.Possession;

@Builder
public record PossessionRecoupee(
    Info prevu, Set<Info> realises, RecoupementStatus status, Set<Correction> corrections) {
  public Argent valeurRealisee() {
    var somme = ariary(0);
    for (var realise : realises) {
      somme = realise.valeur().add(somme, realise.t());
    }

    return somme;
  }

  public Argent ecartValeurAvecRealises() {
    return valeurRealisee().minus(prevu.valeur(), prevu.t());
  }

  public Possession possession() {
    return info().possession();
  }

  public Argent valeur() {
    return info().valeur();
  }

  public Info info() {
    if (prevu.possession() != null) {
      return prevu;
    }

    return realises.stream().findFirst().orElseThrow();
  }

  public record Info(LocalDate t, Argent valeur, Possession possession) {
    public static Info of(Possession possession) {
      return new Info(possession.t(), possession.valeurComptable(), possession);
    }

    public static Info empty() {
      return new Info(LocalDate.MIN, new Argent(0, MGA), null);
    }
  }
}
