package school.hei.patrimoine.modele.recouppement.decomposeur;

import java.util.List;
import school.hei.patrimoine.modele.possession.FluxArgent;

public class FluxArgentDecomposeur extends PossessionDecomposeurBase<FluxArgent> {
  public static String FLUX_ARGENT_DATE_SEPARATEUR = "__du_";

  @Override
  public List<FluxArgent> apply(FluxArgent fluxArgent) {
    if (fluxArgent.getDebut().equals(fluxArgent.getFin())) {
      return List.of(fluxArgent);
    }

    return fluxArgent
        .getDebut()
        .datesUntil(fluxArgent.getFin().plusDays(1))
        .filter(date -> date.getDayOfMonth() == fluxArgent.getDateOperation())
        .map(
            date ->
                new FluxArgent(
                    fluxArgent.nom() + FLUX_ARGENT_DATE_SEPARATEUR + date,
                    fluxArgent.getCompte(),
                    date,
                    fluxArgent.getFluxMensuel()))
        .toList();
  }
}
