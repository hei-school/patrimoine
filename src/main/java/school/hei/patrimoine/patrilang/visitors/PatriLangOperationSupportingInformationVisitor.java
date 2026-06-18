package school.hei.patrimoine.patrilang.visitors;

import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.SupportingInfosContext;

@RequiredArgsConstructor
public class PatriLangOperationSupportingInformationVisitor
    implements Function<SupportingInfosContext, OperationSupportingInformation> {
  private final PatriLangPieceJustificativeVisitor pjVisitor;
  private final PatriLangCommentOperationVisitor commentVisitor;

  @Override
  public OperationSupportingInformation apply(SupportingInfosContext ctx) {
    if (ctx == null) {
      return OperationSupportingInformation.empty();
    }
    return new OperationSupportingInformation(pjVisitor.apply(ctx), commentVisitor.apply(ctx));
  }
}
