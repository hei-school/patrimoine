package school.hei.patrimoine.modele.recouppement.decomposeur;

import static school.hei.patrimoine.modele.recouppement.decomposeur.PossessionDecomposeurFactory.normalize;

import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;

@Slf4j
public class FluxArgentDecomposeur extends PossessionDecomposeurBase<FluxArgent, FluxArgent> {
  public static final String FLUX_ARGENT_DATE_SEPARATEUR = "__du_";

  public FluxArgentDecomposeur(LocalDate finProjection) {
    super(finProjection);
  }

  @Override
  public List<FluxArgent> apply(FluxArgent fluxArgent) {
    if (fluxArgent.getDebut().equals(fluxArgent.getFin())) {
      return List.of(fluxArgent);
    }

    var fin =
        fluxArgent.getFin().isBefore(getFinSimulation()) ? fluxArgent.getFin() : getFinSimulation();

    if (fluxArgent.getDebut().isAfter(fin)) {
      log.warn(
          "FluxArgent incohérent : le début ({}) est après la fin ({}). Nom = '{}'.",
          fluxArgent.getDebut(),
          fin,
          fluxArgent.nom());
      return List.of(fluxArgent);
    }

    var compte = fluxArgent.getCompte();
    return fluxArgent
        .getDebut()
        .datesUntil(fin.plusDays(1))
        .filter(
            date ->
                date.getDayOfMonth()
                    == Math.min(fluxArgent.getDateOperation(), date.lengthOfMonth()))
        .map(
            date ->
                new FluxArgent(
                    normalize(fluxArgent.nom() + FLUX_ARGENT_DATE_SEPARATEUR + date),
                    new Compte(
                        compte.nom(), date, compte.valeurComptable()), // to avoid side effect
                    date,
                    fluxArgent.getFluxMensuel()))
        .toList();
  }
}
