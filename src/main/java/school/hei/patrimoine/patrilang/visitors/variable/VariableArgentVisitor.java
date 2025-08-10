package school.hei.patrimoine.patrilang.visitors.variable;

import static java.util.Objects.nonNull;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.ARGENT;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitDevise;
import static school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor.extractVariableName;

import java.time.LocalDate;
import java.util.ArrayList;
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

  public Argent apply(ArgentContext ctx) {
    var defaultDate = getDefaultDateFromArgent(ctx);
    return apply(ctx, defaultDate);
  }

  private Argent apply(ArgentContext ctx, LocalDate evaluationDate) {
    if (nonNull(ctx.dateValue)) {
      evaluationDate = variableDateVisitor.get().apply(ctx.dateValue);
    } else if (evaluationDate == null) {
      evaluationDate = getDefaultDateFromArgent(ctx);
    }

    if (evaluationDate == null) {
      boolean hasPlusOrMinus =
          ctx.children.stream()
              .filter(child -> child instanceof TerminalNode)
              .map(child -> (TerminalNode) child)
              .anyMatch(
                  t ->
                      t.getSymbol().getType() == PatriLangParser.PLUS
                          || t.getSymbol().getType() == PatriLangParser.MOINS);
      if (hasPlusOrMinus) {
        throw new IllegalArgumentException(
            "Une date d'évaluation est obligatoire lorsqu'il y a un '+' ou '-'.");
      }
      evaluationDate = LocalDate.now();
    }

    var terms = ctx.argentMultiplicationExpr();
    Argent result = visitArgentMultiplicationExpr(terms.get(0), evaluationDate);

    var operators = new ArrayList<TerminalNode>();
    for (ParseTree child : ctx.children) {
      if (child instanceof TerminalNode terminal) {
        int type = terminal.getSymbol().getType();
        if (type == PatriLangParser.PLUS || type == PatriLangParser.MOINS) {
          operators.add(terminal);
        }
      }
    }

    for (int i = 1; i < terms.size(); i++) {
      Argent next = visitArgentMultiplicationExpr(terms.get(i), evaluationDate);
      TerminalNode op = operators.get(i - 1);
      if (op.getSymbol().getType() == PatriLangParser.PLUS) {
        result = result.add(next, evaluationDate);
      } else {
        result = result.minus(next, evaluationDate);
      }
    }

    return result;
  }

  private LocalDate getDefaultDateFromArgent(ArgentContext ctx) {
    for (var term : ctx.argentMultiplicationExpr()) {
      var atom = term.atomArgent();
      if (atom != null) {
        DateContext dateCtx = findDateContext(atom);
        if (dateCtx != null) {
          return variableDateVisitor.get().apply(dateCtx);
        }
      }
    }
    return null;
  }

  private DateContext findDateContext(ParseTree node) {
    if (node == null) return null;
    if (node instanceof DateContext dc) return dc;
    for (int i = 0; i < node.getChildCount(); i++) {
      DateContext found = findDateContext(node.getChild(i));
      if (found != null) return found;
    }
    return null;
  }

  private Argent visitArgentMultiplicationExpr(
      ArgentMultiplicationExprContext ctx, LocalDate evaluationDate) {
    Argent base = visitAtomArgent(ctx.atomArgent(), evaluationDate);

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

  private Argent visitAtomArgent(PatriLangParser.AtomArgentContext ctx, LocalDate evaluationDate) {
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
      case PatriLangParser.ParenArgentExprContext parenCtx ->
          apply(parenCtx.argent(), evaluationDate);
      default ->
          throw new UnsupportedOperationException(
              "Type d'atomArgent non supporté : " + ctx.getText());
    };
  }
}
