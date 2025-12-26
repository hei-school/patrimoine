package school.hei.patrimoine.patrilang.visitors.possession.pj;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.possession.pj.PiecesJustificative;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.PiecesJustificativeItemContext;
import school.hei.patrimoine.patrilang.visitors.IdVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableDateVisitor;

@RequiredArgsConstructor
public class PieceJustfificativeLinkVisitor {
  private final VariableDateVisitor variableDateVisitor;
  private final IdVisitor idVisitor;

  public List<PiecesJustificative> apply(List<PiecesJustificativeItemContext> ctx) {
    String idFlux = this.idVisitor.apply(ctx.getFirst().id());
    LocalDate dateEmission = this.variableDateVisitor.apply(ctx.getFirst().dateValue);
    String driveLink = ctx.getFirst().URL_CONTENT().toString();

    return List.of(new PiecesJustificative(idFlux, dateEmission, driveLink));
  }
}
