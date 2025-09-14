package school.hei.patrimoine.modele.recouppement.generateur;

import java.util.Set;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.FluxArgent;

public class FluxArgentCorrectionGenerateur extends CorrectionGenerateurBase<FluxArgent> {
  @Override
  public Set<Correction> imprévu(FluxArgent nonPrévu) {
    var compte = nonPrévu.getCompte();
    return Set.of(
        new Correction(
            new FluxArgent(
                imprevuNom(nonPrévu), compte, nonPrévu.t(), nonPrévu.getFluxMensuel().mult(-1))));
  }

  @Override
  public Set<Correction> nonÉxecuté(FluxArgent nonÉxecuté) {
    var compte = nonÉxecuté.getCompte();
    return Set.of(
        new Correction(
            new FluxArgent(
                nonExecuteNom(nonÉxecuté), compte, nonÉxecuté.t(), nonÉxecuté.getFluxMensuel())));
  }

  @Override
  public Set<Correction> comparer(FluxArgent prévu, FluxArgent réalité) {
    var compte = prévu.getCompte();
    var diff = getValeur(réalité).minus(getValeur(prévu), prévu.t());

    if (memeDate(prévu, réalité)) {
      if (memeValeur(prévu, réalité)) {
        return Set.of();
      }

      return Set.of(
          new Correction(
              new FluxArgent(
                  valeurDifferenteNom(prévu),
                  compte,
                  prévu.t(),
                  prévu.getFin(),
                  prévu.getDateOperation(),
                  diff)));
    }

    if (memeValeur(prévu, réalité)) {
      return Set.of(
          new Correction(
              new FluxArgent(
                  dateDifferenteNom(prévu, réalité), compte, prévu.t(), prévu.getFluxMensuel())),
          new Correction(
              new FluxArgent(
                  dateDifferenteNom(prévu, réalité),
                  compte,
                  réalité.t(),
                  réalité.getFluxMensuel().mult(-1))));
    }

    return Set.of(
        new Correction(new FluxArgent(valeurDifferenteNom(prévu), compte, réalité.t(), diff)),
        new Correction(
            new FluxArgent(
                dateDifferenteNom(prévu, réalité), compte, prévu.t(), prévu.getFluxMensuel())),
        new Correction(
            new FluxArgent(
                dateDifferenteNom(prévu, réalité),
                compte,
                réalité.t(),
                réalité.getFluxMensuel().mult(-1))));
  }

  @Override
  protected Argent getValeur(FluxArgent possession) {
    return possession.getFluxMensuel();
  }
}
