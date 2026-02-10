package school.hei.patrimoine.modele.recouppement;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.modele.recouppement.RecoupementStatus.EXECUTE_AVEC_CORRECTION;
import static school.hei.patrimoine.modele.recouppement.RecoupementStatus.EXECUTE_SANS_CORRECTION;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.decomposeur.PossessionDecomposeurFactory;
import school.hei.patrimoine.modele.possession.CompteCorrection;
import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.recouppement.CompteGetterFactory.CompteGetter;
import school.hei.patrimoine.modele.recouppement.generateur.RecoupeurDePossessionFacade;

public class RecoupeurDePossessions {
  private final LocalDate debut;
  private final LocalDate fin;
  private final Set<Possession> prevus;
  private final Set<Possession> realises;
  private final Set<Possession> decomposedPrevus;
  private final Set<Possession> decomposedRealises;
  private final RecoupeurDePossessionFacade recoupeurFacade;

  private static final Pattern MULTIPLE_REALISATION_PATTERN = Pattern.compile("\\[(.*)\\]__(.*)");

  public static RecoupeurDePossessions of(
      LocalDate debut, LocalDate fin, Patrimoine prevu, Patrimoine realise) {
    return RecoupeurDePossessions.of(debut, fin, prevu, realise, CompteGetterFactory.make(realise));
  }

  public static RecoupeurDePossessions of(
      LocalDate debut,
      LocalDate fin,
      Patrimoine prevu,
      Patrimoine realise,
      CompteGetter compteGetter) {
    return new RecoupeurDePossessions(
        debut, fin, prevu.getPossessions(), realise.getPossessions(), compteGetter);
  }

  public RecoupeurDePossessions(
      LocalDate debut,
      LocalDate fin,
      Set<Possession> prevus,
      Set<Possession> realises,
      CompteGetter compteGetter) {
    this.debut = debut;
    this.fin = fin;
    this.prevus = withoutCompteCorrections(prevus);
    this.realises = withoutCompteCorrections(realises);

    var decomposedPrevus =
        this.prevus.stream()
            .map(
                p -> {
                  var decomposeur = PossessionDecomposeurFactory.make(p, this.debut, this.fin);
                  return decomposeur.apply(p);
                })
            .flatMap(Collection::stream)
            .collect(toSet());

    var decomposedRealises =
        this.realises.stream()
            .map(
                p -> {
                  var decomposeur = PossessionDecomposeurFactory.make(p, this.debut, this.fin);
                  return decomposeur.apply(p);
                })
            .flatMap(Collection::stream)
            .collect(toSet());

    this.decomposedPrevus = new HashSet<>(decomposedPrevus);
    this.decomposedRealises = new HashSet<>(decomposedRealises);
    this.recoupeurFacade = new RecoupeurDePossessionFacade(compteGetter);
  }

  public Set<Possession> getPossessionsExecutes() {
    return decomposedRealises;
  }

  public Set<Possession> getPossessionsNonExecutes() {
    return decomposedPrevus.stream().filter(p -> getRealises(p).isEmpty()).collect(toSet());
  }

  public Set<Possession> getPossessionsNonPrevus() {
    return decomposedRealises.stream().filter(p -> getPrevu(p).isEmpty()).collect(toSet());
  }

  public Set<PossessionRecoupee> getByStatus(RecoupementStatus status){
      return getPossessionsRecoupees()
          .stream()
          .filter(recouped -> recouped.status().equals(status))
          .collect(toSet());
  }

  public Set<PossessionRecoupee> getPossessionsRecoupees() {
    Set<PossessionRecoupee> possessionRecoupees = new HashSet<>();

    getPossessionsNonExecutes()
        .forEach(
            p -> {
              var possessionRecoupeur = recoupeurFacade.getRecoupeur(p);
              possessionRecoupees.add(possessionRecoupeur.nonExecute(p));
            });

    getPossessionsNonPrevus()
        .forEach(
            p -> {
              var possessionRecoupeur = recoupeurFacade.getRecoupeur(p);
              possessionRecoupees.add(possessionRecoupeur.imprevu(p));
            });

    getPossessionsExecutes()
        .forEach(
            prevu -> {
              var realises = getRealises(prevu);
              var possessionRecoupeur = recoupeurFacade.getRecoupeur(prevu);
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
            realise -> {
              var realises = getPrevu(realise);
              var possessionRecoupeur = recoupeurFacade.getRecoupeur(realise);
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
              var possessionRecoupeur = recoupeurFacade.getRecoupeur(prevu);
              var possessionRecoupee = possessionRecoupeur.comparer(prevu, realises);
              return possessionRecoupee.status().equals(EXECUTE_SANS_CORRECTION);
            })
        .collect(toSet());
  }

  private Optional<Possession> getPrevu(Possession realise) {
    return decomposedPrevus.stream()
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
    return decomposedRealises.stream()
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

  static Set<Possession> withoutCompteCorrections(Set<Possession> possessions) {
    return possessions.stream().filter(not(p -> p instanceof CompteCorrection)).collect(toSet());
  }
}
