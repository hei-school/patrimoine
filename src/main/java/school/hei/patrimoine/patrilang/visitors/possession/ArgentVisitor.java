package school.hei.patrimoine.patrilang.visitors.possession;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.ArgentContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitDevise;

import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.patrilang.visitors.ExpressionVisitor;

@RequiredArgsConstructor
public class ArgentVisitor implements SimpleVisitor<ArgentContext, Argent> {
  private final ExpressionVisitor expressionVisitor;

  @Override
  public Argent apply(ArgentContext ctx) {
    var valeurComptable = this.expressionVisitor.apply(ctx.expression());
    var devise = visitDevise(ctx.devise());

    return new Argent(valeurComptable, devise);
  }
}
