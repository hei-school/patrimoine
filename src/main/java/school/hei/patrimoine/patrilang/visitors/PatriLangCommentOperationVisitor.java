package school.hei.patrimoine.patrilang.visitors;

import java.util.List;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.possession.pj.OperationComments;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.CommentItemContext;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.PiecesJustificativesContext;
import school.hei.patrimoine.patrilang.visitors.variable.VariableDateVisitor;

@RequiredArgsConstructor
public class PatriLangCommentOperationVisitor
    implements Function<PiecesJustificativesContext, List<OperationComments>> {
  private final IdVisitor idVisitor;
  private final VariableDateVisitor dateVisitor;

  @Override
  public List<OperationComments> apply(PiecesJustificativesContext context) {
    if (context == null || context.sectionComments() == null) {
      return List.of();
    }

    return context.sectionComments().commentItem().stream().map(this::visitComments).toList();
  }

  public OperationComments visitComments(CommentItemContext ctx) {
    var id = this.idVisitor.apply(ctx.id());
    var date = this.dateVisitor.apply(ctx.date());
    var content = ctx.TEXT_CONTENT().getText().trim();

    return new OperationComments(id, date, content);
  }
}
