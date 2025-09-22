package school.hei.patrimoine.modele.recouppement.generateur;

import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.modele.recouppement.RecoupementStatus.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee.Info;

public class FluxArgentRecoupeurDePossession extends RecoupeurDePossessionBase<FluxArgent> {
  @Override
  public PossessionRecoupee imprevu(FluxArgent imprevu) {
    var corrections =
        Set.of(
            new Correction(
                new FluxArgent(
                    nommeAsImprevu(imprevu, imprevu.t()),
                    imprevu.getCompte(),
                    getDate(imprevu),
                    getCorrectionValeur(null, Set.of(imprevu)))));

    return PossessionRecoupee.builder()
        .status(IMPREVU)
        .prevu(Info.empty())
        .corrections(corrections)
        .realises(Set.of(toInfo(imprevu)))
        .build();
  }

  @Override
  public PossessionRecoupee nonExecute(FluxArgent nonExecute) {
    var corrections =
        Set.of(
            new Correction(
                new FluxArgent(
                    nommerAsNonExecute(nonExecute, nonExecute.t()),
                    nonExecute.getCompte(),
                    getDate(nonExecute),
                    getCorrectionValeur(nonExecute, Set.of()))));

    return PossessionRecoupee.builder()
        .status(NON_EXECUTE)
        .realises(Set.of())
        .prevu(toInfo(nonExecute))
        .corrections(corrections)
        .build();
  }

  @Override
  public PossessionRecoupee comparer(FluxArgent prevu, Set<FluxArgent> realises) {
    var groupedByDate = groupByDate(realises);
    if (!groupedByDate.containsKey(getDate(prevu))) {
      groupedByDate.put(getDate(prevu), Set.of());
    }

    var corrections =
        groupedByDate.entrySet().stream()
            .map(entry -> getCorrectionForDateT(entry.getKey(), prevu, entry.getValue()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(toSet());

    if (corrections.isEmpty()) {
      return PossessionRecoupee.builder()
          .status(EXECUTE_SANS_CORRECTION)
          .prevu(toInfo(prevu))
          .realises(toInfo(realises))
          .corrections(corrections)
          .build();
    }

    return PossessionRecoupee.builder()
        .status(EXECUTE_AVEC_CORRECTION)
        .prevu(toInfo(prevu))
        .realises(toInfo(realises))
        .corrections(corrections)
        .build();
  }

  private Optional<Correction> getCorrectionForDateT(
      LocalDate t, FluxArgent prevu, Set<FluxArgent> realises) {
    var correctionValeur = getCorrectionValeur(t.equals(prevu.t()) ? prevu : null, realises);

    if (correctionValeur.equals(new Argent(0, correctionValeur.devise()))) {
      return Optional.empty();
    }

    String nom;
    if (t.isAfter(prevu.t())) {
      nom = nommerAsEnRetard(prevu, t);
    } else if (t.isBefore(prevu.t())) {
      nom = nommerAsEnAvance(prevu, t);
    } else {
      nom = nommerAsValeurDifferentes(prevu, t);
    }

    return Optional.of(new Correction(new FluxArgent(nom, prevu.getCompte(), t, correctionValeur)));
  }

  @Override
  protected Argent getValeur(FluxArgent possession) {
    return possession.getFluxMensuel();
  }
}
