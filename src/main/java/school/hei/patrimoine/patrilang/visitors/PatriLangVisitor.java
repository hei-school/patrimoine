package school.hei.patrimoine.patrilang.visitors;

import static java.util.Objects.isNull;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;

import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.patrilang.antlr.PatriLangParserBaseVisitor;

@RequiredArgsConstructor
public class PatriLangVisitor extends PatriLangParserBaseVisitor<Object> {
  private final PatriLangToutCasVisitor toutCasVisitor;
  private final PatriLangCasVisitor casVisitor;

  @Override
  public Object visitDocument(DocumentContext ctx) {
    if (isNull(ctx.toutCas())) {
      return this.casVisitor.apply(ctx.cas());
    }

    return this.toutCasVisitor.apply(ctx.toutCas());
  }

  public static PatriLangVisitor create(SectionVisitor sectionVisitor) {
    var toutCasVisitor = new PatriLangToutCasVisitor(sectionVisitor);
    var casVisitor = new PatriLangCasVisitor(sectionVisitor);
    return new PatriLangVisitor(toutCasVisitor, casVisitor);
  }
}
