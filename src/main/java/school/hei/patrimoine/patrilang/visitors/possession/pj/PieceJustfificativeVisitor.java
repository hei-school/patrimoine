package school.hei.patrimoine.patrilang.visitors.possession.pj;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.possession.pj.PiecesJustificative;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.PieceJustificativeContext;
import school.hei.patrimoine.patrilang.visitors.IdVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

@RequiredArgsConstructor
public class PieceJustfificativeVisitor {
  private final VariableVisitor variableVisitor;
  private final IdVisitor idVisitor;

  public PiecesJustificative apply(PieceJustificativeContext ctx) {
    String idFlux = this.idVisitor.apply(ctx.id());
    LocalDate dateEmission = this.variableVisitor.asDate(ctx.dateValue);
    String driveLink = ctx.pjUrl.getText();

    return new PiecesJustificative(idFlux, dateEmission, driveLink);
  }
}
