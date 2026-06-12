package school.hei.patrimoine.modele.decomposeur;

import static school.hei.patrimoine.modele.decomposeur.IdRetriever.getDecomposedId;

import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.modele.possession.TransfertArgent;
import school.hei.patrimoine.modele.series.DateSeries;

@Slf4j
public class TransfertArgentDecomposeur
    extends PossessionDecomposeurBase<TransfertArgent, TransfertArgent> {
  public TransfertArgentDecomposeur(LocalDate debut, LocalDate fin) {
    super(debut, fin);
  }

  @Override
  protected boolean isOutOfRange(TransfertArgent transfert) {
    return transfert.getDebut().isAfter(getFin()) || transfert.getFin().isBefore(getDebut());
  }

  @Override
  public List<TransfertArgent> apply(TransfertArgent transfert) {
    if (isOutOfRange(transfert)) {
      return List.of();
    }

    if (transfert.getDebut().equals(transfert.getFin())) {
      return List.of(transfert);
    }

    var debut = transfert.getDebut().isAfter(getDebut()) ? transfert.getDebut() : getDebut();
    var fin = transfert.getFin().isBefore(getFin()) ? transfert.getFin() : getFin();
    if (transfert.getDebut().isAfter(fin)) {
      log.warn(
          "transfert incohérent : la date de début ({}) est après la date de fin ({}). Nom ="
              + " '{}'.",
          transfert.getDebut(),
          fin,
          transfert.nom());
      return List.of(transfert);
    }

    var depuisCompte = transfert.getDepuisCompte();
    var versCompte = transfert.getVersCompte();
    return DateSeries.byDayOfMonth(debut, fin, transfert.getDateOperation()).stream()
        .map(
            date ->
                new TransfertArgent(
                    getDecomposedId(transfert.nom(), date),
                    copyCompte(depuisCompte, date),
                    copyCompte(versCompte, date),
                    date,
                    transfert.getFluxMensuel()))
        .toList();
  }
}
