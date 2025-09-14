package school.hei.patrimoine.modele.recouppement.generateur;

import java.time.LocalDate;
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
                imprevuNom(nonPrévu), compte, nonPrévu.getDebut(), nonPrévu.getFluxMensuel().mult(-1))));
  }

  @Override
  public Set<Correction> nonÉxecuté(FluxArgent nonÉxecuté) {
    var compte = nonÉxecuté.getCompte();
    return Set.of(
        new Correction(
            new FluxArgent(
                nonExecuteNom(nonÉxecuté), compte, nonÉxecuté.getDebut(), nonÉxecuté.getFluxMensuel())));
  }

  @Override
  public Set<Correction> comparer(FluxArgent prévu, FluxArgent réalité) {
    var compte = prévu.getCompte();
    var valeurDiff = getValeur(réalité).minus(getValeur(prévu), prévu.getDebut());

    if (memeDate(prévu, réalité)) {
      if (memeValeur(prévu, réalité)) {
        return Set.of();
      }

      return Set.of(
          new Correction(
              new FluxArgent(valeurDifferenteNom(prévu), compte, prévu.getDebut(), valeurDiff)));
    }

    if (memeValeur(prévu, réalité)) {
      return Set.of(
          new Correction(
              new FluxArgent(
                  dateDifferenteNom(prévu, réalité), compte, prévu.getDebut(), prévu.getFluxMensuel())),
          new Correction(
              new FluxArgent(
                  dateDifferenteNom(prévu, réalité),
                  compte,
                  réalité.getDebut(),
                  réalité.getFluxMensuel().mult(-1))));
    }

    return Set.of(
        new Correction(new FluxArgent(valeurDifferenteNom(prévu), compte, réalité.getDebut(), valeurDiff)),
        new Correction(
            new FluxArgent(
                dateDifferenteNom(prévu, réalité), compte, prévu.getDebut(), prévu.getFluxMensuel())),
        new Correction(
            new FluxArgent(
                dateDifferenteNom(prévu, réalité),
                compte,
                réalité.getDebut(),
                réalité.getFluxMensuel().mult(-1))));
  }

  @Override
  protected Argent getValeur(FluxArgent possession) {
    return possession.getFluxMensuel();
  }

  @Override
  protected LocalDate getDate(FluxArgent possession){
    return possession.getDebut();
  }
}
