package school.hei.patrimoine.patrilang.visitors;

import java.util.List;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.possession.pj.PiecesJustificative;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.PiecesJustificativeItemContext;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.PiecesJustificativesContext;
import school.hei.patrimoine.patrilang.visitors.variable.VariableDateVisitor;

@RequiredArgsConstructor
public class PatriLangPiecesJustificativeVisitor
    implements Function<PiecesJustificativesContext, List<PiecesJustificative>> {
  private final IdVisitor idVisitor;
  private final VariableDateVisitor dateVisitor;

  @Override
  public List<PiecesJustificative> apply(PiecesJustificativesContext context) {
    return context.sectionPiecesJustificatives().piecesJustificativeItem().stream()
        .map(this::visitPieceJustificative)
        .toList();
  }

  public PiecesJustificative visitPieceJustificative(PiecesJustificativeItemContext ctx) {
    var id = this.idVisitor.apply(ctx.id());
    var date = this.dateVisitor.apply(ctx.date());
    var link = ctx.URL_CONTENT().toString();

    return new PiecesJustificative(id, date, link);
  }
}

// Ctrl + Alt + Shift + L = formatter
