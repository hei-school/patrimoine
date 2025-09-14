package school.hei.patrimoine.modele.recouppement;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.modele.recouppement.PossessionRecoupee.PossessionRecoupeeStatus.*;

import java.util.*;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.CompteCorrection;
import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.recouppement.decomposeur.PossessionDecomposeurFactory;
import school.hei.patrimoine.modele.recouppement.generateur.RecoupeurDepossessionFactory;

public record RecoupeurDePossessions(Set<Possession> prévus, Set<Possession> réalisés) {
  public static RecoupeurDePossessions of(Patrimoine prévu, Patrimoine réalisé) {
    return new RecoupeurDePossessions(prévu.getPossessions(), réalisé.getPossessions());
  }

  public RecoupeurDePossessions(Set<Possession> prévus, Set<Possession> réalisés) {
    this.prévus =
        withoutCompteCorrections(prévus).stream()
            .map(
                p -> {
                  var decomposeur = PossessionDecomposeurFactory.make(p);
                  return decomposeur.apply(p);
                })
            .flatMap(Collection::stream)
            .collect(toSet());

    this.réalisés =
        withoutCompteCorrections(réalisés).stream()
            .map(
                p -> {
                  var decomposeur = PossessionDecomposeurFactory.make(p);
                  return decomposeur.apply(p);
                })
            .flatMap(Collection::stream)
            .collect(toSet());
  }

  public Set<Possession> getPossessionsÉxecutés() {
    return prévus.stream().filter(p -> getEquivalent(réalisés, p).isPresent()).collect(toSet());
  }

  public Set<Possession> getPossessionsNonExecutés() {
    return prévus.stream().filter(p -> getEquivalent(réalisés, p).isEmpty()).collect(toSet());
  }

  public Set<Possession> getPossessionsNonPrévus() {
    return réalisés.stream().filter(p -> getEquivalent(prévus, p).isEmpty()).collect(toSet());
  }

  public Set<PossessionRecoupee> getPossessionsRecoupees() {
    Set<PossessionRecoupee> possessionRecoupees = new HashSet<>();

    getPossessionsNonExecutés()
        .forEach(
            p -> {
              var possessionRecoupeur = RecoupeurDepossessionFactory.make(p);
              possessionRecoupees.add(possessionRecoupeur.nonÉxecuté(p));
            });

    getPossessionsNonPrévus()
        .forEach(
            p -> {
              var possessionRecoupeur = RecoupeurDepossessionFactory.make(p);
              possessionRecoupees.add(possessionRecoupeur.imprévu(p));
            });

    getPossessionsÉxecutés()
        .forEach(
            prévu -> {
              var réalisé = getEquivalent(réalisés, prévu).get();
              var possessionRecoupeur = RecoupeurDepossessionFactory.make(prévu);
              possessionRecoupees.add(possessionRecoupeur.comparer(prévu, réalisé));
            });

    return possessionRecoupees;
  }

  public Set<Correction> getCorrections() {
    return getPossessionsRecoupees().stream()
        .map(PossessionRecoupee::corrections)
        .flatMap(Collection::stream)
        .collect(toSet());
  }

  public Set<Possession> getPossessionsÉxecutésAvecCorrections() {
    return getPossessionsÉxecutés().stream()
        .filter(
            not(
                prévu -> {
                  var réalisé = getEquivalent(réalisés, prévu).get();
                  var possessionRecoupeur = RecoupeurDepossessionFactory.make(prévu);
                  return possessionRecoupeur.comparer(prévu, réalisé).corrections().isEmpty();
                }))
        .collect(toSet());
  }

  public Set<Possession> getPossessionsÉxecutésSansCorrections() {
    return getPossessionsÉxecutés().stream()
        .filter(
            prévu -> {
              var réalisé = getEquivalent(réalisés, prévu).get();
              var possessionRecoupeur = RecoupeurDepossessionFactory.make(prévu);
              return possessionRecoupeur.comparer(prévu, réalisé).corrections().isEmpty();
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
