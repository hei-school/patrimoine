package school.hei.patrimoine.patrilang.visitors.possession.pj;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.PiecesJustificativesItemContext;
import school.hei.patrimoine.patrilang.visitors.IdVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableDateVisitor;

@RequiredArgsConstructor
public class PieceJustificativeLinkVisitor {
  private final VariableDateVisitor variableDateVisitor;
  private final IdVisitor idVisitor;

  public List<PieceJustificative> apply(List<PiecesJustificativesItemContext> ctx) {
    String idFlux = this.idVisitor.apply(ctx.getFirst().id());
    LocalDate dateEmission = this.variableDateVisitor.apply(ctx.getFirst().dateValue);
    String driveLink = ctx.getFirst().URL_CONTENT().toString();

    return List.of(new PieceJustificative(idFlux, dateEmission, driveLink));
  }
}
