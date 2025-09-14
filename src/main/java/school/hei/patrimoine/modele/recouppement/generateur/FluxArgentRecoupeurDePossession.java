package school.hei.patrimoine.modele.recouppement.generateur;

import static school.hei.patrimoine.modele.recouppement.PossessionRecoupee.PossessionRecoupeeStatus.*;

import java.time.LocalDate;
import java.util.Set;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;

public class FluxArgentRecoupeurDePossession extends RecoupeurDePossessionBase<FluxArgent> {
  @Override
  public PossessionRecoupee imprévu(FluxArgent imprévu) {
    var compte = imprévu.getCompte();
    var corrections =
        Set.of(
            new Correction(
                new FluxArgent(
                    imprevuNom(imprévu), compte, getDate(imprévu), getValeur(imprévu).mult(-1))));

    return PossessionRecoupee.builder()
        .status(IMPREVU)
        .possession(imprévu)
        .corrections(corrections)
        .datePrévu(LocalDate.MIN)
        .valeurPrévu(new Argent(0, imprévu.devise()))
        .dateRéalisé(getDate(imprévu))
        .valeurRéalisé(getValeur(imprévu))
        .build();
  }

  @Override
  public PossessionRecoupee nonÉxecuté(FluxArgent nonÉxecuté) {
    var compte = nonÉxecuté.getCompte();
    var corrections =
        Set.of(
            new Correction(
                new FluxArgent(
                    nonExecuteNom(nonÉxecuté),
                    compte,
                    getDate(nonÉxecuté),
                    getValeur(nonÉxecuté))));

    return PossessionRecoupee.builder()
        .status(NON_EXECUTE)
        .possession(nonÉxecuté)
        .corrections(corrections)
        .dateRéalisé(LocalDate.MIN)
        .valeurRéalisé(new Argent(0, nonÉxecuté.devise()))
        .datePrévu(getDate(nonÉxecuté))
        .valeurPrévu(getValeur(nonÉxecuté))
        .build();
  }

  @Override
  public PossessionRecoupee comparer(FluxArgent prévu, FluxArgent réalisé) {
    var compte = prévu.getCompte();
    var valeurDiff = getValeur(réalisé).minus(getValeur(prévu), getDate(réalisé));

    if (memeDate(prévu, réalisé)) {
      if (memeValeur(prévu, réalisé)) {
        return PossessionRecoupee.builder()
            .possession(prévu)
            .status(EXECUTE_SANS_CORRECTION)
            .corrections(Set.of())
            .valeurPrévu(getValeur(prévu))
            .valeurRéalisé(getValeur(réalisé))
            .datePrévu(getDate(prévu))
            .dateRéalisé(getDate(réalisé))
            .build();
      }

      var corrections =
          Set.of(
              new Correction(
                  new FluxArgent(
                      valeurDifferenteNom(prévu), compte, getDate(réalisé), valeurDiff)));
      return PossessionRecoupee.builder()
          .possession(prévu)
          .corrections(corrections)
          .status(EXECUTE_AVEC_CORRECTION)
          .valeurPrévu(getValeur(prévu))
          .valeurRéalisé(getValeur(réalisé))
          .datePrévu(getDate(prévu))
          .dateRéalisé(getDate(réalisé))
          .build();
    }

    if (memeValeur(prévu, réalisé)) {
      var corrections =
          Set.of(
              new Correction(
                  new FluxArgent(
                      dateDifferenteNom(prévu, réalisé), compte, getDate(prévu), getValeur(prévu))),
              new Correction(
                  new FluxArgent(
                      dateDifferenteNom(prévu, réalisé),
                      compte,
                      getDate(réalisé),
                      getValeur(réalisé).mult(-1))));

      return PossessionRecoupee.builder()
          .possession(prévu)
          .status(EXECUTE_AVEC_CORRECTION)
          .corrections(corrections)
          .dateRéalisé(getDate(réalisé))
          .valeurRéalisé(getValeur(réalisé))
          .datePrévu(getDate(prévu))
          .valeurPrévu(getValeur(prévu))
          .build();
    }

    var corrections =
        Set.of(
            new Correction(
                new FluxArgent(valeurDifferenteNom(prévu), compte, getDate(réalisé), valeurDiff)),
            new Correction(
                new FluxArgent(
                    dateDifferenteNom(prévu, réalisé), compte, getDate(prévu), getValeur(prévu))),
            new Correction(
                new FluxArgent(
                    dateDifferenteNom(prévu, réalisé),
                    compte,
                    getDate(réalisé),
                    getValeur(réalisé).mult(-1))));

    return PossessionRecoupee.builder()
        .possession(prévu)
        .status(EXECUTE_AVEC_CORRECTION)
        .corrections(corrections)
        .dateRéalisé(getDate(réalisé))
        .valeurRéalisé(getValeur(réalisé))
        .datePrévu(getDate(prévu))
        .valeurPrévu(getValeur(prévu))
        .build();
  }

  @Override
  protected Argent getValeur(FluxArgent possession) {
    return possession.getFluxMensuel();
  }
}
