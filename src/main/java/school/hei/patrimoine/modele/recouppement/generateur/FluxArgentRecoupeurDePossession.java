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
  public PossessionRecoupee imprevu(FluxArgent imprevu) {
    var compte = imprevu.getCompte();
    var corrections =
        Set.of(
            new Correction(
                new FluxArgent(
                    imprevuNom(imprevu), compte, getDate(imprevu), getValeur(imprevu).mult(-1))));

    return PossessionRecoupee.builder()
        .status(IMPREVU)
        .possession(imprevu)
        .corrections(corrections)
        .datePrevu(LocalDate.MIN)
        .valeurPrevu(new Argent(0, imprevu.devise()))
        .dateRealise(getDate(imprevu))
        .valeurRealise(getValeur(imprevu))
        .build();
  }

  @Override
  public PossessionRecoupee nonExecute(FluxArgent nonExecute) {
    var compte = nonExecute.getCompte();
    var corrections =
        Set.of(
            new Correction(
                new FluxArgent(
                    nonExecuteNom(nonExecute),
                    compte,
                    getDate(nonExecute),
                    getValeur(nonExecute))));

    return PossessionRecoupee.builder()
        .status(NON_EXECUTE)
        .possession(nonExecute)
        .corrections(corrections)
        .dateRealise(LocalDate.MIN)
        .valeurRealise(new Argent(0, nonExecute.devise()))
        .datePrevu(getDate(nonExecute))
        .valeurPrevu(getValeur(nonExecute))
        .build();
  }

  @Override
  public PossessionRecoupee comparer(FluxArgent prevu, FluxArgent realise) {
    var compte = prevu.getCompte();
    var valeurDiff = getValeur(realise).minus(getValeur(prevu), getDate(realise));

    if (memeDate(prevu, realise)) {
      if (memeValeur(prevu, realise)) {
        return PossessionRecoupee.builder()
            .possession(prevu)
            .status(EXECUTE_SANS_CORRECTION)
            .corrections(Set.of())
            .valeurPrevu(getValeur(prevu))
            .valeurRealise(getValeur(realise))
            .datePrevu(getDate(prevu))
            .dateRealise(getDate(realise))
            .build();
      }

      var corrections =
          Set.of(
              new Correction(
                  new FluxArgent(
                      valeurDifferenteNom(prevu), compte, getDate(realise), valeurDiff)));
      return PossessionRecoupee.builder()
          .possession(prevu)
          .corrections(corrections)
          .status(EXECUTE_AVEC_CORRECTION)
          .valeurPrevu(getValeur(prevu))
          .valeurRealise(getValeur(realise))
          .datePrevu(getDate(prevu))
          .dateRealise(getDate(realise))
          .build();
    }

    if (memeValeur(prevu, realise)) {
      var corrections =
          Set.of(
              new Correction(
                  new FluxArgent(
                      dateDifferenteNom(prevu, realise), compte, getDate(prevu), getValeur(prevu))),
              new Correction(
                  new FluxArgent(
                      dateDifferenteNom(prevu, realise),
                      compte,
                      getDate(realise),
                      getValeur(realise).mult(-1))));

      return PossessionRecoupee.builder()
          .possession(prevu)
          .status(EXECUTE_AVEC_CORRECTION)
          .corrections(corrections)
          .dateRealise(getDate(realise))
          .valeurRealise(getValeur(realise))
          .datePrevu(getDate(prevu))
          .valeurPrevu(getValeur(prevu))
          .build();
    }

    var corrections =
        Set.of(
            new Correction(
                new FluxArgent(valeurDifferenteNom(prevu), compte, getDate(realise), valeurDiff)),
            new Correction(
                new FluxArgent(
                    dateDifferenteNom(prevu, realise), compte, getDate(prevu), getValeur(prevu))),
            new Correction(
                new FluxArgent(
                    dateDifferenteNom(prevu, realise),
                    compte,
                    getDate(realise),
                    getValeur(realise).mult(-1))));

    return PossessionRecoupee.builder()
        .possession(prevu)
        .status(EXECUTE_AVEC_CORRECTION)
        .corrections(corrections)
        .dateRealise(getDate(realise))
        .valeurRealise(getValeur(realise))
        .datePrevu(getDate(prevu))
        .valeurPrevu(getValeur(prevu))
        .build();
  }

  @Override
  protected Argent getValeur(FluxArgent possession) {
    return possession.getFluxMensuel();
  }
}
