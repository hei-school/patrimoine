package school.hei.patrimoine.modele.recouppement;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.modele.decomposeur.IdRetriever.getPlannedIdFromRealisationId;
import static school.hei.patrimoine.modele.recouppement.model.RecoupementStatus.*;

import java.time.LocalDate;
import java.util.*;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.modele.decomposeur.PossessionDecomposeurFacade;
import school.hei.patrimoine.modele.possession.CompteCorrection;
import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.recouppement.generateur.correction.RecoupeurDePossessionFacade;
import school.hei.patrimoine.modele.recouppement.generateur.info.InfoGetterFacade;
import school.hei.patrimoine.modele.recouppement.model.CompteGetter;
import school.hei.patrimoine.modele.recouppement.model.Info;
import school.hei.patrimoine.modele.recouppement.model.PossessionRecoupee;
import school.hei.patrimoine.modele.recouppement.model.RecoupementStatus;

public class RecoupeurDePossessions {
  private final InfoGetterFacade infoGetterFacade;
  private final Map<String, PossessionRecoupee<Possession>> recouped;

  public static RecoupeurDePossessions of(LocalDate debut, LocalDate fin, Cas prevu, Cas realise) {
    return RecoupeurDePossessions.of(debut, fin, prevu, realise, CompteGetter.make(realise));
  }

  private static LocalDate getMinDate(LocalDate a, LocalDate b) {
    return a.isBefore(b) ? a : b;
  }

  public static RecoupeurDePossessions of(
      LocalDate debut, LocalDate fin, Cas prevu, Cas realise, CompteGetter compteGetter) {
    return new RecoupeurDePossessions(
        debut,
        getMinDate(fin, prevu.getFinSimulation()),
        prevu.patrimoine().getPossessions(),
        realise.patrimoine().getPossessions(),
        compteGetter);
  }

  public RecoupeurDePossessions(
      LocalDate debut,
      LocalDate fin,
      Set<Possession> prevus,
      Set<Possession> realises,
      CompteGetter compteGetter) {
    this.infoGetterFacade = new InfoGetterFacade(compteGetter);
    this.recouped = createRecouped(prevus, realises, debut, fin);
  }

  public Set<PossessionRecoupee<Possession>> getPossessionsExecutes() {
    return getByStatus(Set.of(EXECUTE_AVEC_CORRECTION, EXECUTE_SANS_CORRECTION, IMPREVU));
  }

  public Set<PossessionRecoupee<Possession>> getByStatus(Set<RecoupementStatus> status) {
    return recouped.values().stream()
        .filter(possessionRecoupee -> status.contains(possessionRecoupee.status()))
        .collect(toSet());
  }

  public Set<PossessionRecoupee<Possession>> getByStatus(RecoupementStatus status) {
    return getByStatus(Set.of(status));
  }

  public Set<PossessionRecoupee<Possession>> getPossessionsNonExecutes() {
    return getByStatus(NON_EXECUTE);
  }

  public Set<PossessionRecoupee<Possession>> getPossessionsImprevus() {
    return getByStatus(IMPREVU);
  }

  public Set<PossessionRecoupee<Possession>> getPossessionsRecoupees() {
    return new HashSet<>(recouped.values());
  }

  public Set<Correction> getCorrections() {
    return getByStatus(Set.of(EXECUTE_AVEC_CORRECTION, IMPREVU, NON_EXECUTE)).stream()
        .map(PossessionRecoupee::corrections)
        .flatMap(Collection::stream)
        .collect(toSet());
  }

  public Set<PossessionRecoupee<Possession>> getPossessionsExecutesAvecCorrections() {
    return getByStatus(EXECUTE_AVEC_CORRECTION);
  }

  public Set<PossessionRecoupee<Possession>> getPossessionsExecutesSansCorrections() {
    return getByStatus(EXECUTE_SANS_CORRECTION);
  }

  private static void validateId(Possession possession) {
    if (possession.nom().contains("[") || possession.nom().contains("]")) {
      throw new IllegalArgumentException(
          "Une possession avec un ID contenant des crochets n'est pas autorisée : "
              + possession.nom());
    }
  }

  private static List<Possession> decomposePlanned(
      Set<Possession> possessions, LocalDate debut, LocalDate fin) {
    return possessions.stream()
        .peek(RecoupeurDePossessions::validateId)
        .map(p -> PossessionDecomposeurFacade.decompose(p, debut, fin))
        .flatMap(List::stream)
        .toList();
  }

  private static List<Possession> decomposeDone(
      Set<Possession> possessions, LocalDate debut, LocalDate fin) {
    return possessions.stream()
        .map(p -> PossessionDecomposeurFacade.decompose(p, debut, fin))
        .flatMap(List::stream)
        .toList();
  }

  private List<Info<Possession>> toInfos(Collection<Possession> possessions) {
    return possessions.stream().map(this.infoGetterFacade::get).toList();
  }

  private Map<String, PossessionRecoupee<Possession>> createRecouped(
      Set<Possession> prevus, Set<Possession> realises, LocalDate debut, LocalDate fin) {
    var map = createInfosMap(prevus, realises, debut, fin);
    for (var entry : map.entrySet()) {
      var afterRecoupement =
          RecoupeurDePossessionFacade.recouper(
              entry.getValue().prevu(), entry.getValue().realises());
      entry.setValue(afterRecoupement);
    }
    return map;
  }

  private Map<String, PossessionRecoupee<Possession>> createInfosMap(
      Set<Possession> prevus, Set<Possession> realises, LocalDate debut, LocalDate fin) {
    var decomposedPrevus = decomposePlanned(withoutCompteCorrections(prevus), debut, fin);
    var decomposedRealises = decomposeDone(withoutCompteCorrections(realises), debut, fin);
    var prevusAsInfos = toInfos(decomposedPrevus);
    var realisesAsInfos = toInfos(decomposedRealises);

    Map<String, PossessionRecoupee<Possession>> map = new HashMap<>();

    prevusAsInfos.forEach(
        prevu -> {
          map.putIfAbsent(
              prevu.nom(),
              PossessionRecoupee.builder()
                  .prevu(prevu)
                  .corrections(new HashSet<>())
                  .realises(new HashSet<>())
                  .build());
        });

    realisesAsInfos.forEach(
        realise -> {
          var plannedId = getPlannedIdFromRealisationId(realise.nom());
          if (map.containsKey(plannedId)) {
            map.get(plannedId).realises().add(realise);
            return;
          }

          var newRealises = new HashSet<>(Set.of(realise));
          map.put(
              plannedId,
              PossessionRecoupee.builder()
                  .prevu(Info.empty())
                  .corrections(new HashSet<>())
                  .realises(newRealises)
                  .build());
        });

    return map;
  }

  static Set<Possession> withoutCompteCorrections(Set<Possession> possessions) {
    return possessions.stream().filter(not(p -> p instanceof CompteCorrection)).collect(toSet());
  }
}
