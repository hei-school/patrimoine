package school.hei.patrimoine.modele.recouppement.model;

import static java.time.LocalDate.now;
import static school.hei.patrimoine.modele.Argent.ariary;

import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
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
    var valeurPrevu = prevu.isEmpty() ? ariary(0) : prevu.valeur();
    var tPrevu = prevu.isEmpty() ? now() : prevu.t();
    return valeurRealisee().minus(valeurPrevu, tPrevu);
  }

  public LocalDate dateRealisee() {
    return realises.stream().map(Info::t).min(Comparator.naturalOrder()).orElse(null);
  }

  public Period ecartDateAvecRealises() {
    var tPrevu = prevu.t();
    var tRealisee = dateRealisee();
    if (prevu.isEmpty() || tRealisee == null) {
      return Period.ZERO;
    }
    return Period.between(tPrevu, tRealisee);
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
