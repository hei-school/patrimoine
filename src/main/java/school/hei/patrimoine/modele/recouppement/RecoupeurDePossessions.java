package school.hei.patrimoine.modele.recouppement;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toSet;

import java.util.*;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.CompteCorrection;
import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.recouppement.decomposeur.PossessionDecomposeurFactory;
import school.hei.patrimoine.modele.recouppement.generateur.RecoupeurDePossessionFactory;

public record RecoupeurDePossessions(Set<Possession> prevus, Set<Possession> realises) {
  public static RecoupeurDePossessions of(Patrimoine prevu, Patrimoine realise) {
    return new RecoupeurDePossessions(prevu.getPossessions(), realise.getPossessions());
  }

  public RecoupeurDePossessions(Set<Possession> prevus, Set<Possession> realises) {
    this.prevus =
        withoutCompteCorrections(prevus).stream()
            .map(
                p -> {
                  var decomposeur = PossessionDecomposeurFactory.make(p);
                  return decomposeur.apply(p);
                })
            .flatMap(Collection::stream)
            .collect(toSet());

    this.realises =
        withoutCompteCorrections(realises).stream()
            .map(
                p -> {
                  var decomposeur = PossessionDecomposeurFactory.make(p);
                  return decomposeur.apply(p);
                })
            .flatMap(Collection::stream)
            .collect(toSet());
  }

  public Set<Possession> getPossessionsExecutes() {
    return prevus.stream().filter(p -> getEquivalent(realises, p).isPresent()).collect(toSet());
  }

  public Set<Possession> getPossessionsNonExecutes() {
    return prevus.stream().filter(p -> getEquivalent(realises, p).isEmpty()).collect(toSet());
  }

  public Set<Possession> getPossessionsNonPrevus() {
    return realises.stream().filter(p -> getEquivalent(prevus, p).isEmpty()).collect(toSet());
  }

  public Set<PossessionRecoupee> getPossessionsRecoupees() {
    Set<PossessionRecoupee> possessionRecoupees = new HashSet<>();

    getPossessionsNonExecutes()
        .forEach(
            p -> {
              var possessionRecoupeur = RecoupeurDePossessionFactory.make(p);
              possessionRecoupees.add(possessionRecoupeur.nonExecute(p));
            });

    getPossessionsNonPrevus()
        .forEach(
            p -> {
              var possessionRecoupeur = RecoupeurDePossessionFactory.make(p);
              possessionRecoupees.add(possessionRecoupeur.imprevu(p));
            });

    getPossessionsExecutes()
        .forEach(
            prevu -> {
              var realise = getEquivalent(realises, prevu).get();
              var possessionRecoupeur = RecoupeurDePossessionFactory.make(prevu);
              possessionRecoupees.add(possessionRecoupeur.comparer(prevu, realise));
            });

    return possessionRecoupees;
  }

  public Set<Correction> getCorrections() {
    return getPossessionsRecoupees().stream()
        .map(PossessionRecoupee::corrections)
        .flatMap(Collection::stream)
        .collect(toSet());
  }

  public Set<Possession> getPossessionsExecutesAvecCorrections() {
    return getPossessionsExecutes().stream()
        .filter(
            not(
                prevu -> {
                  var realise = getEquivalent(realises, prevu).get();
                  var possessionRecoupeur = RecoupeurDePossessionFactory.make(prevu);
                  return possessionRecoupeur.comparer(prevu, realise).corrections().isEmpty();
                }))
        .collect(toSet());
  }

  public Set<Possession> getPossessionsExecutesSansCorrections() {
    return getPossessionsExecutes().stream()
        .filter(
            prevu -> {
              var realise = getEquivalent(realises, prevu).get();
              var possessionRecoupeur = RecoupeurDePossessionFactory.make(prevu);
              return possessionRecoupeur.comparer(prevu, realise).corrections().isEmpty();
            })
        .collect(toSet());
  }

  private static Optional<Possession> getEquivalent(
      Collection<Possession> possessions, Possession possession) {
    return possessions.stream()
        .filter(
            p -> {
              if (!p.nom().equals(possession.nom())) {
                return false;
              }

              return possession.getClass().equals(p.getClass());
            })
        .findFirst();
  }

  private static Set<Possession> withoutCompteCorrections(Set<Possession> possessions) {
    return possessions.stream().filter(not(p -> p instanceof CompteCorrection)).collect(toSet());
  }
}
