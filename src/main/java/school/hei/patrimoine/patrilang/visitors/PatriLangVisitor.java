package school.hei.patrimoine.patrilang.visitors;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.CasContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.ToutCasContext;

import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.cas.CasSet;
import school.hei.patrimoine.patrilang.antlr.PatriLangParserBaseVisitor;

@RequiredArgsConstructor
public class PatriLangVisitor extends PatriLangParserBaseVisitor<Object> {
  private final PatriLangToutCasVisitor toutCasVisitor;
  private final PatriLangCasVisitor casVisitor;

  @Override
  public CasSet visitToutCas(ToutCasContext ctx) {
    return this.toutCasVisitor.apply(ctx);
  }

  @Override
  public Cas visitCas(CasContext ctx) {
    return this.casVisitor.apply(ctx);
  }
}
