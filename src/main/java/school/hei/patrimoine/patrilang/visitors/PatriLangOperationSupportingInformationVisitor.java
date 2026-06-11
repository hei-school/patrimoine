package school.hei.patrimoine.patrilang.visitors;

import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.PiecesJustificativesContext;

@RequiredArgsConstructor
public class PatriLangOperationSupportingInformationVisitor
    implements Function<PiecesJustificativesContext, OperationSupportingInformation> {
  private final PatriLangPieceJustificativeVisitor pjVisitor;
  private final PatriLangCommentOperationVisitor commentVisitor;

  @Override
  public OperationSupportingInformation apply(PiecesJustificativesContext ctx) {
    if (ctx == null) {
      return OperationSupportingInformation.empty();
    }
    return new OperationSupportingInformation(pjVisitor.apply(ctx), commentVisitor.apply(ctx));
  }
}
