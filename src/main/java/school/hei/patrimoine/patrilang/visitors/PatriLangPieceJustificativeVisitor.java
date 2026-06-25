package school.hei.patrimoine.patrilang.visitors;

import java.util.List;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.PiecesJustificativesItemContext;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.SupportingInfosContext;
import school.hei.patrimoine.patrilang.visitors.variable.VariableDateVisitor;

@RequiredArgsConstructor
public class PatriLangPieceJustificativeVisitor
    implements Function<SupportingInfosContext, List<PieceJustificative>> {
  private final IdVisitor idVisitor;
  private final VariableDateVisitor dateVisitor;

  @Override
  public List<PieceJustificative> apply(SupportingInfosContext context) {
    if (context == null || context.sectionPiecesJustificatives() == null) {
      return List.of();
    }

    return context.sectionPiecesJustificatives().piecesJustificativesItem().stream()
        .map(this::visitPieceJustificative)
        .toList();
  }

  public PieceJustificative visitPieceJustificative(PiecesJustificativesItemContext ctx) {
    var id = this.idVisitor.apply(ctx.id());
    var date = this.dateVisitor.apply(ctx.date());
    var reference = ctx.reference.getText().trim();
    var link = ctx.STRING_CONTENT().getText().trim().replaceAll("\\s+", "");

    return new PieceJustificative(id, date, reference, link);
  }
}
