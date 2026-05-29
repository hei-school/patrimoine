package school.hei.patrimoine.modele.decomposeur;

import static school.hei.patrimoine.modele.decomposeur.IdRetriever.getDecomposedId;

import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.series.DateSeries;

@Slf4j
public class FluxArgentDecomposeur extends PossessionDecomposeurBase<FluxArgent, FluxArgent> {
  public FluxArgentDecomposeur(LocalDate debut, LocalDate fin) {
    super(debut, fin);
  }

  @Override
  protected boolean isOutOfRange(FluxArgent fluxArgent) {
    return fluxArgent.getDebut().isAfter(getFin()) || fluxArgent.getFin().isBefore(getDebut());
  }

  @Override
  public List<FluxArgent> apply(FluxArgent fluxArgent) {
    if (isOutOfRange(fluxArgent)) {
      return List.of();
    }

    if (fluxArgent.getDebut().equals(fluxArgent.getFin())) {
      return List.of(fluxArgent);
    }

    var debut = fluxArgent.getDebut().isAfter(getDebut()) ? fluxArgent.getDebut() : getDebut();
    var fin = fluxArgent.getFin().isBefore(getFin()) ? fluxArgent.getFin() : getFin();
    if (fluxArgent.getDebut().isAfter(fin)) {
      log.warn(
          "FluxArgent incohérent : la date de début ({}) est après la date de fin ({}). Nom ="
              + " '{}'.",
          fluxArgent.getDebut(),
          fin,
          fluxArgent.nom());
      return List.of(fluxArgent);
    }

    var compte = fluxArgent.getCompte();
    return DateSeries.byDayOfMonth(debut, fin, fluxArgent.getDateOperation()).stream()
        .map(
            date ->
                new FluxArgent(
                    getDecomposedId(fluxArgent.nom(), date),
                    new Compte(
                        compte.nom(), date, compte.valeurComptable()), // to avoid side effect
                    date,
                    fluxArgent.getFluxMensuel()))
        .toList();
  }
}
