package school.hei.patrimoine.modele.recouppement.model;

import static school.hei.patrimoine.modele.Argent.ariary;

import java.util.Set;
import lombok.Builder;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.Possession;

@Builder(toBuilder = true)
public record PossessionRecoupee<T extends Possession>(
    Info<T> prevu, Set<Info<T>> realises, Set<Correction> corrections, RecoupementStatus status) {
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

  public Info<T> info() {
    if (prevu.isEmpty()) {
      return realises.stream().findFirst().orElseThrow();
    }
    return prevu;
  }
}
