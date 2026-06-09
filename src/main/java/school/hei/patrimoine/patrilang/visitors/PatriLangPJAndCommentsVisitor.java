package school.hei.patrimoine.patrilang.visitors;

import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.PiecesJustificativesContext;

@RequiredArgsConstructor
public class PatriLangPJAndCommentsVisitor
    implements Function<PiecesJustificativesContext, PJAndComments> {
  private final PatriLangPieceJustificativeVisitor pjVisitor;
  private final PatriLangCommentOperationVisitor commentVisitor;

  @Override
  public PJAndComments apply(PiecesJustificativesContext ctx) {
    if (ctx == null) {
      return PJAndComments.empty();
    }
    return new PJAndComments(pjVisitor.apply(ctx), commentVisitor.apply(ctx));
  }
}
