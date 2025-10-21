package school.hei.patrimoine.modele.recouppement.decomposeur;

import static school.hei.patrimoine.modele.recouppement.decomposeur.PossessionDecomposeurFactory.normalize;

import java.time.LocalDate;
import java.util.List;
import school.hei.patrimoine.modele.possession.FluxArgent;

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

    return fluxArgent
        .getDebut()
        .datesUntil(fin.plusDays(1))
        .filter(date -> date.getDayOfMonth() == fluxArgent.getDateOperation())
        .map(
            date ->
                new FluxArgent(
                    normalize(fluxArgent.nom() + FLUX_ARGENT_DATE_SEPARATEUR + date),
                    fluxArgent.getCompte(),
                    date,
                    fluxArgent.getFluxMensuel()))
        .toList();
  }
}
