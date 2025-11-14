package school.hei.patrimoine.modele.recouppement.generateur;

import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.modele.recouppement.RecoupementStatus.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee.Info;

public class FluxArgentRecoupeurDePossession extends RecoupeurDePossessionBase<FluxArgent> {
  @Override
  public PossessionRecoupee imprevu(FluxArgent imprevu) {
    var correctionValeur = getCorrectionValeur(null, Set.of(imprevu));
    var corrections =
        Set.of(
            new Correction(
                imprevu.getCompte(),
                nommeAsImprevu(imprevu, imprevu.t(), correctionValeur),
                getDate(imprevu),
                getCorrectionValeur(null, Set.of(imprevu))));

    return PossessionRecoupee.builder()
        .status(IMPREVU)
        .prevu(Info.empty())
        .corrections(corrections)
        .realises(Set.of(toInfo(imprevu)))
        .build();
  }

  @Override
  public PossessionRecoupee nonExecute(FluxArgent nonExecute, Set<Possession> possessions) {
    var correctionValeur = getCorrectionValeur(nonExecute, Set.of());
    var corrections =
        Set.of(
            new Correction(
                getNonExecuteCompte(nonExecute.getCompte().nom(), possessions),
                nommerAsNonExecute(nonExecute, nonExecute.t(), correctionValeur),
                getDate(nonExecute),
                correctionValeur));

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

    var compte = realises.stream().findFirst().orElseThrow().getCompte();
    var corrections =
        groupedByDate.entrySet().stream()
            .map(entry -> getCorrectionForDateT(entry.getKey(), prevu, entry.getValue(), compte))
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
      LocalDate t, FluxArgent prevu, Set<FluxArgent> realises, Compte compte) {
    var correctionValeur = getCorrectionValeur(t.equals(prevu.t()) ? prevu : null, realises);

    if (correctionValeur.equals(new Argent(0, correctionValeur.devise()))) {
      return Optional.empty();
    }

    String nom;
    if (t.isAfter(prevu.t())) {
      nom = nommerAsEnRetard(prevu, t, correctionValeur);
    } else if (t.isBefore(prevu.t())) {
      nom = nommerAsEnAvance(prevu, t, correctionValeur);
    } else {
      nom = nommerAsValeurDifferentes(prevu, t, correctionValeur);
    }

    return Optional.of(new Correction(compte, nom, t, correctionValeur));
  }

  @Override
  protected Argent getValeur(FluxArgent possession) {
    return possession.getFluxMensuel();
  }

  private static Compte getNonExecuteCompte(String compteNom, Set<Possession> possessions) {
    return (Compte)
        possessions.stream()
            .filter(p -> p instanceof Compte && p.nom().equals(compteNom))
            .findFirst()
            .orElseThrow();
  }
}
