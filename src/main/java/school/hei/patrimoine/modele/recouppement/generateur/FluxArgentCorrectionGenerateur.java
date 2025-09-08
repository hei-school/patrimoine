package school.hei.patrimoine.modele.recouppement.generateur;

import java.util.Set;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Correction;
import school.hei.patrimoine.modele.possession.FluxArgent;

public class FluxArgentCorrectionGenerateur extends CorrectionGenerateurBase<FluxArgent> {
  @Override
  public Set<Correction> nonPrévu(FluxArgent nonPrévu) {
    var compte = nonPrévu.getCompte();
    return Set.of(
        new Correction(
            new FluxArgent(
                nonPrévu.nom(), compte, nonPrévu.t(), nonPrévu.getFluxMensuel().mult(-1))));
  }

  @Override
  public Set<Correction> nonÉxecuté(FluxArgent nonÉxecuté) {
    var compte = nonÉxecuté.getCompte();
    return Set.of(
        new Correction(
            new FluxArgent(nonÉxecuté.nom(), compte, nonÉxecuté.t(), nonÉxecuté.getFluxMensuel())));
  }

  @Override
  public Set<Correction> comparer(FluxArgent prévu, FluxArgent réalité) {
    var compte = prévu.getCompte();
    if (memeDate(prévu, réalité)) {
      if (memeValeur(prévu, réalité)) {
        return Set.of();
      }

      var fluxMensuelDiff = réalité.getFluxMensuel().minus(prévu.getFluxMensuel(), prévu.t());
      return Set.of(
          new Correction(
              new FluxArgent(
                  prévu.nom(),
                  compte,
                  prévu.t(),
                  prévu.getFin(),
                  prévu.getDateOperation(),
                  fluxMensuelDiff)));
    }

    return Set.of(
        new Correction(new FluxArgent(prévu.nom(), compte, prévu.t(), prévu.getFluxMensuel())),
        new Correction(
            new FluxArgent(prévu.nom(), compte, prévu.t(), réalité.getFluxMensuel().mult(-1))));
  }

  @Override
  protected Argent getValeur(FluxArgent possession) {
    return possession.getFluxMensuel();
  }
}
