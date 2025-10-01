package school.hei.patrimoine.modele.recouppement;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.modele.recouppement.RecoupementStatus.EXECUTE_AVEC_CORRECTION;
import static school.hei.patrimoine.modele.recouppement.RecoupementStatus.EXECUTE_SANS_CORRECTION;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.CompteCorrection;
import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.recouppement.decomposeur.PossessionDecomposeurFactory;
import school.hei.patrimoine.modele.recouppement.generateur.RecoupeurDePossessionFactory;

public record RecoupeurDePossessions(
    LocalDate finSimulation, Set<Possession> prevus, Set<Possession> realises) {
  public static final Pattern MULTIPLE_REALISATION_PATTERN = Pattern.compile("\\[(.*)\\]__(.*)");

  public static RecoupeurDePossessions of(
      LocalDate finSimulation, Patrimoine prevu, Patrimoine realise) {
    return new RecoupeurDePossessions(
        finSimulation, prevu.getPossessions(), realise.getPossessions());
  }

  public RecoupeurDePossessions(
      LocalDate finSimulation, Set<Possession> prevus, Set<Possession> realises) {
    this.finSimulation = finSimulation;
    this.prevus =
        withoutCompteCorrections(prevus).stream()
            .map(
                p -> {
                  var decomposeur = PossessionDecomposeurFactory.make(p, finSimulation);
                  return decomposeur.apply(p);
                })
            .flatMap(Collection::stream)
            .collect(toSet());

    this.realises =
        withoutCompteCorrections(realises).stream()
            .map(
                p -> {
                  var decomposeur = PossessionDecomposeurFactory.make(p, finSimulation);
                  return decomposeur.apply(p);
                })
            .flatMap(Collection::stream)
            .collect(toSet());
  }

  public Set<Possession> getPossessionsExecutes() {
    return prevus.stream().filter(not(p -> getRealises(p).isEmpty())).collect(toSet());
  }

  public Set<Possession> getPossessionsNonExecutes() {
    return prevus.stream().filter(p -> getRealises(p).isEmpty()).collect(toSet());
  }

  public Set<Possession> getPossessionsNonPrevus() {
    return realises.stream().filter(p -> getPrevu(p).isEmpty()).collect(toSet());
  }

  public Set<PossessionRecoupee> getPossessionsRecoupees() {
    Set<PossessionRecoupee> possessionRecoupees = new HashSet<>();

    getPossessionsNonExecutes()
        .forEach(
            p -> {
              var possessionRecoupeur = RecoupeurDePossessionFactory.make(p);
              possessionRecoupees.add(possessionRecoupeur.nonExecute(p, realises));
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
              var realises = getRealises(prevu);
              var possessionRecoupeur = RecoupeurDePossessionFactory.make(prevu);
              var possessionRecoupee = possessionRecoupeur.comparer(prevu, realises);
              possessionRecoupees.add(possessionRecoupee);
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
            prevu -> {
              var realises = getRealises(prevu);
              var possessionRecoupeur = RecoupeurDePossessionFactory.make(prevu);
              var possessionRecoupee = possessionRecoupeur.comparer(prevu, realises);
              return possessionRecoupee.status().equals(EXECUTE_AVEC_CORRECTION);
            })
        .collect(toSet());
  }

  public Set<Possession> getPossessionsExecutesSansCorrections() {
    return getPossessionsExecutes().stream()
        .filter(
            prevu -> {
              var realises = getRealises(prevu);
              var possessionRecoupeur = RecoupeurDePossessionFactory.make(prevu);
              var possessionRecoupee = possessionRecoupeur.comparer(prevu, realises);
              return possessionRecoupee.status().equals(EXECUTE_SANS_CORRECTION);
            })
        .collect(toSet());
  }

  private Optional<Possession> getPrevu(Possession realise) {
    return prevus.stream()
        .filter(
            p -> {
              var prevuNom = realise.nom();
              var matcher = MULTIPLE_REALISATION_PATTERN.matcher(realise.nom());
              if (matcher.matches()) {
                prevuNom = matcher.group(1);
              }

              if (!prevuNom.equals(p.nom())) {
                return false;
              }

              return realise.getClass().equals(p.getClass());
            })
        .findFirst();
  }

  private Set<Possession> getRealises(Possession prevu) {
    return realises.stream()
        .filter(
            realise -> {
              var realiseBaseName = realise.nom();
              var matcher = MULTIPLE_REALISATION_PATTERN.matcher(realise.nom());
              if (matcher.matches()) {
                realiseBaseName = matcher.group(1);
              }

              if (!prevu.nom().equals(realiseBaseName)) {
                return false;
              }

              return prevu.getClass().equals(realise.getClass());
            })
        .collect(toSet());
  }

  private static Set<Possession> withoutCompteCorrections(Set<Possession> possessions) {
    return possessions.stream().filter(not(p -> p instanceof CompteCorrection)).collect(toSet());
  }
}
