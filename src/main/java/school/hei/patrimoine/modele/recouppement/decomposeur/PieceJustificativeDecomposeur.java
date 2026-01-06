package school.hei.patrimoine.modele.recouppement.decomposeur;

import static school.hei.patrimoine.modele.recouppement.decomposeur.PieceJustificativeDecomposeurFactory.normalize;

import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

@Slf4j
public class PieceJustificativeDecomposeur
    extends PossessionDecomposeurBase<PieceJustificative, PieceJustificative> {

  private static final String PIECE_JUSTIFICATIVE_DATE_SEPARATEUR = "__du_";

  public PieceJustificativeDecomposeur(LocalDate finSimulation) {
    super(finSimulation);
  }

  private boolean containDate(String id, LocalDate date) {
    var normalizedDate = normalize(date.toString());
    var suffix = PIECE_JUSTIFICATIVE_DATE_SEPARATEUR + normalizedDate;
    return id.contains(suffix);
  }

  @Override
  public List<PieceJustificative> apply(PieceJustificative pieceJustificative) {
    if (pieceJustificative.date().equals(getFinSimulation())) {
      return List.of(pieceJustificative);
    }

    var fin =
        pieceJustificative.date().isBefore(getFinSimulation())
            ? getFinSimulation()
            : pieceJustificative.date();

    if (pieceJustificative.date().isAfter(fin)) {
      log.warn(
          "PieceJustificative incohérent : le début ({}) est après la fin ({}). Nom = '{}'.",
          pieceJustificative.date(),
          fin,
          pieceJustificative.id());
      return List.of(pieceJustificative);
    }

    return pieceJustificative
        .date()
        .datesUntil(fin.plusDays(1))
        .filter(
            date ->
                date.getDayOfMonth()
                    == Math.min(pieceJustificative.date().getDayOfMonth(), date.lengthOfMonth()))
        .map(
            date -> {
              var id = pieceJustificative.id();
              var idWithDate =
                  containDate(id, date) ? id : id + PIECE_JUSTIFICATIVE_DATE_SEPARATEUR + date;
              return new PieceJustificative(normalize(idWithDate), date, pieceJustificative.link());
            })
        .toList();
  }
}
