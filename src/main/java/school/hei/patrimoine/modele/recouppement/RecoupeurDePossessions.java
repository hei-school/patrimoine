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
import school.hei.patrimoine.modele.recouppement.generateur.CorrectionGenerateurFactory;

public record RecoupeurDePossessions(Set<Possession> prévus, Set<Possession> réalités) {
  public static RecoupeurDePossessions of(Patrimoine prévu, Patrimoine réalité) {
    return new RecoupeurDePossessions(prévu.getPossessions(), réalité.getPossessions());
  }

  public RecoupeurDePossessions(Set<Possession> prévus, Set<Possession> réalités) {
    this.prévus =
        withoutCompteCorrections(prévus).stream()
            .map(
                p -> {
                  var decomposeur = PossessionDecomposeurFactory.make(p);
                  return decomposeur.apply(p);
                })
            .flatMap(Collection::stream)
            .collect(toSet());

    this.réalités =
        withoutCompteCorrections(réalités).stream()
            .map(
                p -> {
                  var decomposeur = PossessionDecomposeurFactory.make(p);
                  return decomposeur.apply(p);
                })
            .flatMap(Collection::stream)
            .collect(toSet());
  }

  public Set<Possession> getPossessionsÉxecutés() {
    return prévus.stream().filter(p -> getEquivalent(réalités, p).isPresent()).collect(toSet());
  }

  public Set<Possession> getPossessionsNonExecutés() {
    return prévus.stream().filter(p -> getEquivalent(réalités, p).isEmpty()).collect(toSet());
  }

  public Set<Possession> getPossessionsNonPrévus() {
    return réalités.stream().filter(p -> getEquivalent(prévus, p).isEmpty()).collect(toSet());
  }

  public Set<PossessionRecoupee> getPossessionsRecoupees() {
    Set<PossessionRecoupee> possessionRecoupees = new HashSet<>();

    getPossessionsNonExecutés()
        .forEach(
            p -> {
              var correctionGenerateur = CorrectionGenerateurFactory.make(p);
              var corrections = correctionGenerateur.nonÉxecuté(p);
              possessionRecoupees.add(
                  PossessionRecoupee.builder()
                      .status(NON_EXECUTE)
                      .possession(p)
                      .corrections(corrections)
                      .build());
            });

    getPossessionsNonPrévus()
        .forEach(
            p -> {
              var correctionGenerateur = CorrectionGenerateurFactory.make(p);
              var corrections = correctionGenerateur.nonPrévu(p);
              possessionRecoupees.add(
                  PossessionRecoupee.builder()
                      .status(IMPREVU)
                      .possession(p)
                      .corrections(corrections)
                      .build());
            });

    getPossessionsÉxecutés()
        .forEach(
            prévu -> {
              var réalité = getEquivalent(réalités, prévu).get();
              var correctionGenerateur = CorrectionGenerateurFactory.make(prévu);
              var corrections = correctionGenerateur.comparer(prévu, réalité);
              var status =
                  corrections.isEmpty() ? EXECUTE_SANS_CORRECTION : EXECUTE_AVEC_CORRECTION;
              possessionRecoupees.add(
                  PossessionRecoupee.builder()
                      .status(status)
                      .possession(prévu)
                      .corrections(corrections)
                      .build());
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
                  var réalité = getEquivalent(réalités, prévu).get();
                  var correctionGenerateur = CorrectionGenerateurFactory.make(prévu);
                  return correctionGenerateur.comparer(prévu, réalité).isEmpty();
                }))
        .collect(toSet());
  }

  public Set<Possession> getPossessionsÉxecutésSansCorrections() {
    return getPossessionsÉxecutés().stream()
        .filter(
            prévu -> {
              var réalité = getEquivalent(réalités, prévu).get();
              var correctionGenerateur = CorrectionGenerateurFactory.make(prévu);
              return correctionGenerateur.comparer(prévu, réalité).isEmpty();
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
