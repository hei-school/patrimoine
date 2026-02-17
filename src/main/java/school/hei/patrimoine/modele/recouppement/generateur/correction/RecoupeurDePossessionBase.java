package school.hei.patrimoine.modele.recouppement.generateur.correction;

import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.recouppement.generateur.correction.CorrectionNomGenerateur.make;
import static school.hei.patrimoine.modele.recouppement.model.RecoupementStatus.*;

import java.time.LocalDate;
import java.util.*;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.recouppement.generateur.correction.CorrectionNomGenerateur.CorrectionNomType;
import school.hei.patrimoine.modele.recouppement.model.Info;
import school.hei.patrimoine.modele.recouppement.model.PossessionRecoupee;

@RequiredArgsConstructor
public class RecoupeurDePossessionBase<T extends Possession> implements RecoupeurDePossession<T> {
  protected Argent getValeur(Collection<Info<T>> possessions) {
    var somme = ariary(0);
    for (var possession : possessions) {
      somme = possession.valeur().add(somme, possession.t());
    }

    return somme;
  }

  // Valeur de correction = la somme des valeurs réalisées - Valeur prévue
  protected Argent getCorrectionValeur(Info<T> prevu, Set<Info<T>> realises) {
    if (prevu.isEmpty()) {
      return getValeur(realises);
    }

    return getValeur(realises).minus(prevu.valeur(), prevu.t());
  }

  protected Map<LocalDate, Set<Info<T>>> groupByDate(Collection<Info<T>> possessions) {
    Map<LocalDate, Set<Info<T>>> grouped = new HashMap<>();

    for (var possession : possessions) {
      grouped.computeIfAbsent(possession.t(), t -> new HashSet<>()).add(possession);
    }

    return grouped;
  }

  @Override
  public PossessionRecoupee<T> recouper(Info<T> prevu, Set<Info<T>> realises) {
    if (realises.isEmpty()) {
      return nonExecute(prevu);
    }

    if (prevu.isEmpty()) {
      return imprevu(realises);
    }

    var groupedByDate = groupByDate(realises);
    if (!groupedByDate.containsKey(prevu.t())) {
      groupedByDate.put(prevu.t(), Set.of());
    }

    var corrections =
        groupedByDate.entrySet().stream()
            .map(entry -> getCorrectionForDateT(entry.getKey(), prevu, entry.getValue()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(toSet());

    if (corrections.isEmpty()) {
      return PossessionRecoupee.<T>builder()
          .status(EXECUTE_SANS_CORRECTION)
          .prevu(prevu)
          .realises(realises)
          .corrections(corrections)
          .build();
    }

    return PossessionRecoupee.<T>builder()
        .status(EXECUTE_AVEC_CORRECTION)
        .prevu(prevu)
        .realises(realises)
        .corrections(corrections)
        .build();
  }

  protected Optional<Correction> getCorrectionForDateT(
      LocalDate t, Info<T> prevu, Set<Info<T>> realises) {
    var correctionValeur =
        getCorrectionValeur(t.equals(prevu.t()) ? prevu : Info.empty(), realises);

    if (correctionValeur.equals(new Argent(0, correctionValeur.devise()))) {
      return Optional.empty();
    }

    String nom;
    if (t.isAfter(prevu.t())) {
      nom = make(CorrectionNomType.EN_RETARD, t, prevu.nom(), correctionValeur);
    } else if (t.isBefore(prevu.t())) {
      nom = make(CorrectionNomType.EN_AVANCE, t, prevu.nom(), correctionValeur);
    } else {
      nom = make(CorrectionNomType.VALEUR_DIFFERENTES, t, prevu.nom(), correctionValeur);
    }

    return Optional.of(new Correction(prevu.possessionACorriger(), nom, t, correctionValeur));
  }

  private Correction nonExecuteCorrection(Info<T> prevu) {
    return new Correction(
        prevu.possessionACorriger(),
        make(CorrectionNomGenerateur.CorrectionNomType.NON_EXECUTE, prevu),
        prevu.t(),
        prevu.valeur());
  }

  protected PossessionRecoupee<T> nonExecute(Info<T> prevu) {
    return PossessionRecoupee.<T>builder()
        .prevu(prevu)
        .realises(Set.of())
        .status(NON_EXECUTE)
        .corrections(Set.of(nonExecuteCorrection(prevu)))
        .build();
  }

  protected Correction imprevuCorrection(Info<T> imprevu) {
    return new Correction(
        imprevu.possessionACorriger(),
        make(CorrectionNomGenerateur.CorrectionNomType.IMPREVU, imprevu),
        imprevu.t(),
        getCorrectionValeur(Info.empty(), Set.of(imprevu)));
  }

  protected PossessionRecoupee<T> imprevu(Set<Info<T>> imprevus) {
    if (imprevus.size() != 1) {
      var first = imprevus.stream().findFirst().orElseThrow();
      throw new IllegalArgumentException(
            String.format("Opération.nom=%s. Une opération non prévue doit être rattachée qu'à une seule réalisation", first.nom()));
    }

    var imprevu = imprevus.stream().findFirst().orElseThrow();
    return PossessionRecoupee.<T>builder()
        .prevu(Info.empty())
        .realises(Set.of(imprevu))
        .status(IMPREVU)
        .corrections(Set.of(imprevuCorrection(imprevu)))
        .build();
  }
}
