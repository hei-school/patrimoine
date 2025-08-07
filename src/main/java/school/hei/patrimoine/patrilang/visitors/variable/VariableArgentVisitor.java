package school.hei.patrimoine.patrilang.visitors.variable;

import static java.util.Objects.nonNull;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.ARGENT;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitDevise;
import static school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor.extractVariableName;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.modele.variable.VariableScope;
import school.hei.patrimoine.patrilang.visitors.SimpleVisitor;

@RequiredArgsConstructor
@SuppressWarnings("all")
public class VariableArgentVisitor implements SimpleVisitor<ArgentContext, Argent> {

  private final VariableScope variableScope;
  private final Supplier<VariableExpressionVisitor> variableExpressionVisitor;
  private final Supplier<VariableDateVisitor> variableDateVisitor;

  @Override
  public Argent apply(ArgentContext ctx) {
    List<ArgentMultiplicationExprContext> terms = ctx.argentMultiplicationExpr();

    LocalDate evaluationDate = nonNull(ctx.dateValue)
            ? variableDateVisitor.get().apply(ctx.dateValue)
            : LocalDate.now();

    Argent result = visitArgentMultiplicationExpr(terms.get(0));

    List<? extends ParseTree> children = ctx.children;
    List<TerminalNode> operators = new ArrayList<>();

    for (ParseTree child : children) {
      if (child instanceof TerminalNode terminal) {
        if (terminal.getSymbol().getType() == PatriLangParser.PLUS ||
                terminal.getSymbol().getType() == PatriLangParser.MOINS) {
          operators.add(terminal);
        }
      }
    }

    for (int i = 1; i < terms.size(); i++) {
      Argent next = visitArgentMultiplicationExpr(terms.get(i));
      TerminalNode operator = operators.get(i - 1);

      if (operator.getSymbol().getType() == PatriLangParser.PLUS) {
        result = result.add(next, evaluationDate);
      } else if (operator.getSymbol().getType() == PatriLangParser.MOINS) {
        result = result.minus(next, evaluationDate);
      }
    }

    return result;
  }

  private Argent visitArgentMultiplicationExpr(ArgentMultiplicationExprContext ctx) {
    Argent base = visitAtomArgent(ctx.atomArgent());

    for (int i = 0; i < ctx.expression().size(); i++) {
      var operand = variableExpressionVisitor.get().apply(ctx.expression(i));

      if (nonNull(ctx.MUL(i))) {
        base = base.mult(operand);
      } else if (nonNull(ctx.DIV(i))) {
        base = base.div(operand);
      }
    }

    return base;
  }

  private Argent visitAtomArgent(PatriLangParser.AtomArgentContext ctx) {
    return switch (ctx) {
      case PatriLangParser.VariableArgentExprContext varCtx -> {
        String name = extractVariableName(varCtx.getText());
        yield (Argent) variableScope.get(name, ARGENT).value();
      }
      case PatriLangParser.MontantArgentExprContext montantCtx -> {
        var valeur = variableExpressionVisitor.get().apply(montantCtx.expression());
        var devise = visitDevise(montantCtx.devise());
        yield new Argent(valeur, devise);
      }
      case PatriLangParser.ParenArgentExprContext parenCtx -> apply(parenCtx.argent());
      default -> throw new UnsupportedOperationException("Type d'atomArgent non supporté : " + ctx.getText());
    };
  }
}
