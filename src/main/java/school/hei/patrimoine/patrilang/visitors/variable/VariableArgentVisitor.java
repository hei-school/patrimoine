package school.hei.patrimoine.patrilang.visitors.variable;

import static java.util.Objects.nonNull;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.ArgentContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.ArgentValueContext;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.ARGENT;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitDevise;
import static school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor.extractVariableName;

import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.patrilang.modele.variable.VariableScope;
import school.hei.patrimoine.patrilang.visitors.SimpleVisitor;

@RequiredArgsConstructor
@SuppressWarnings("all")
public class VariableArgentVisitor implements SimpleVisitor<ArgentContext, Argent> {
  private final VariableScope variableScope;
  private final VariableExpressionVisitor variableExpressionVisitor;
  private final VariableDateVisitor variableDateVisitor;

  @Override
  public Argent apply(ArgentContext ctx) {
    var lhs = this.visitArgentValue(ctx.lhs);

    if (nonNull(ctx.rhs)) {
      return applyDelta(lhs, ctx);
    }

    return lhs;
  }

  private Argent visitArgentValue(ArgentValueContext ctx) {
    if (nonNull(ctx.ARGENTS_VARIABLE())) {
      var name = extractVariableName(ctx.ARGENTS_VARIABLE().getText());
      var variable = (Argent) this.variableScope.get(name, ARGENT).value();
      return variable;
    }

    var valeurComptable = this.variableExpressionVisitor.apply(ctx.expression());
    var devise = visitDevise(ctx.devise());
    return new Argent(valeurComptable, devise);
  }

  private Argent applyDelta(Argent lhs, ArgentContext ctx) {
    var rhs = this.visitArgentValue(ctx.rhs);
    var date = this.variableDateVisitor.apply(ctx.date());

    if (nonNull(ctx.MOINS())) {
      return lhs.minus(rhs, date);
    }

    return lhs.add(rhs, date);
  }
}
