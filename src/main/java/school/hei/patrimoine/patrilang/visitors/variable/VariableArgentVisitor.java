package school.hei.patrimoine.patrilang.visitors.variable;

import static java.util.Objects.nonNull;
import static org.reflections.Reflections.log;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.ArgentContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.ArgentValueContext;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.ARGENT;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitDevise;
import static school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor.extractVariableName;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.ParserRuleContext;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import school.hei.patrimoine.patrilang.modele.variable.VariableScope;
import school.hei.patrimoine.patrilang.visitors.SimpleVisitor;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("all")
public class VariableArgentVisitor implements SimpleVisitor<ArgentContext, Argent> {
  private final VariableScope variableScope;
  private final VariableExpressionVisitor variableExpressionVisitor;
  private final VariableDateVisitor variableDateVisitor;

  @Override
  public Argent apply(ArgentContext ctx) {
    try {
      if (ctx.expressionArithmetique() != null) {
        if (ctx.date() == null) {
          throw new IllegalArgumentException("Une date d'évaluation est requise pour les expressions arithmétiques");
        }
        Argent result = evaluateExpression(ctx.expressionArithmetique());
        return applyTemporalEvaluation(result, ctx.date());
      }
      else if (ctx.argentSimple() != null) {
        Argent result = handleSimple(ctx.argentSimple());
        requireEvaluationDateIfNeeded(result, ctx.date());
        return applyTemporalEvaluation(result, ctx.date());
      }
      else {
        return handleLegacyFormat(ctx);
      }
    } catch (Exception e) {
      log.error("Erreur lors de l'évaluation de l'expression: {}", ctx.getText(), e);
      throw e;
    }
  }

  private void requireEvaluationDateIfNeeded(Argent amount, DateContext dateCtx) {
    if (amount != null && amount.devise() != null
            && !amount.devise().equals(Devise.MGA)
            && dateCtx == null) {
      throw new IllegalArgumentException("Une date d'évaluation est requise pour les opérations en " + amount.devise());
    }
  }

  private Argent handleSimple(ArgentSimpleContext ctx) {
    Argent lhs = visitArgentValue(ctx.lhs);
    if (ctx.rhs != null) {
      Argent rhs = visitArgentValue(ctx.rhs);
      LocalDate evaluationDate = ctx.getParent() instanceof ArgentContext ?
              variableDateVisitor.apply(((ArgentContext)ctx.getParent()).date()) :
              null;

      if (ctx.PLUS() != null) {
        return lhs.add(rhs, evaluationDate);
      } else if (ctx.MOINS() != null) {
        return lhs.minus(rhs, evaluationDate);
      }
    }
    return lhs;
  }

  private Argent applyTemporalEvaluation(Argent amount, DateContext dateCtx) {
    if (dateCtx == null || amount == null || amount.devise() == null) {
      return amount;
    }
    LocalDate date = variableDateVisitor.apply(dateCtx);
    return amount.devise().equals(Devise.MGA) ? amount : amount.convertir(amount.devise(), date);
  }

  private Argent evaluateExpression(ExpressionArithmetiqueContext expr) {
    Argent current = evaluateTerme(expr.terme(0));
    LocalDate evaluationDate = getEvaluationDate(expr);

    for (int i = 1; i < expr.terme().size(); i++) {
      Argent next = evaluateTerme(expr.terme(i));

      if (expr.PLUS(i-1) != null) {
        current = current.add(next, evaluationDate);
      } else if (expr.MOINS(i-1) != null) {
        current = current.minus(next, evaluationDate);
      }
    }
    return current;
  }

  private LocalDate getEvaluationDate(ParserRuleContext ctx) {
    while (ctx != null && !(ctx instanceof ArgentContext)) {
      ctx = ctx.getParent();
    }

    if (ctx != null) {
      ArgentContext argentCtx = (ArgentContext) ctx;
      if (argentCtx.date() != null) {
        return variableDateVisitor.apply(argentCtx.date());
      }
    }
    return null;
  }

  private Argent applyConversion(Argent amount, LocalDate evaluationDate) {
    if (evaluationDate == null || amount == null || amount.devise() == null) {
      return amount;
    }
    return amount.devise().equals(Devise.MGA) ? amount : amount.convertir(amount.devise(), evaluationDate);
  }

  private Argent evaluateTerme(TermeContext terme) {
    Argent result = evaluateFacteur(terme.facteur());
    LocalDate evaluationDate = getEvaluationDate(terme);

    for (int i = 0; i < terme.scalar().size(); i++) {
      ScalarContext scalar = terme.scalar(i);
      double valeur;

      if (scalar.nombre() != null) {
        valeur = Double.parseDouble(scalar.nombre().getText().replace("_", ""));
      } else {
        valeur = variableExpressionVisitor.apply(scalar.expressionArithmetique());
      }

      if (terme.MUL(i) != null) {
        result = applyConversion(result.mult(valeur), evaluationDate);
      } else if (terme.DIV(i) != null) {
        if (valeur == 0) throw new ArithmeticException("Division par zéro");
        result = applyConversion(result.div(valeur), evaluationDate);
      }
    }
    return result;
  }

  private Argent handleLegacyFormat(ArgentContext ctx) {
    for (int i = 0; i < ctx.getChildCount(); i++) {
      if (ctx.getChild(i) instanceof ArgentValueContext) {
        return visitArgentValue((ArgentValueContext) ctx.getChild(i));
      }
    }
    throw new IllegalArgumentException("Format non supporté: " + ctx.getText());
  }

  private Argent evaluateFacteur(FacteurContext facteur) {
    if (facteur.argentValue() != null) {
      return visitArgentValue(facteur.argentValue());
    }
    if (facteur.expressionArithmetique() != null) {
      return evaluateExpression(facteur.expressionArithmetique());
    }
    throw new IllegalArgumentException("Facteur non reconnu: " + facteur.getText());
  }

  private Argent visitArgentValue(ArgentValueContext ctx) {
    log.info("Visite de ArgentValue: {}", ctx.getText());

    if (ctx.ARGENTS_VARIABLE() != null) {
      String name = extractVariableName(ctx.ARGENTS_VARIABLE().getText());
      return (Argent) variableScope.get(name, ARGENT).value();
    }

    double montant = variableExpressionVisitor.apply(ctx.expression());
    Devise devise = ctx.devise() != null ? visitDevise(ctx.devise()) : null;

    if (devise == null) {
      throw new IllegalArgumentException("Une devise est requise pour une valeur d'argent explicite");
    }

    return new Argent(montant, devise);
  }

  // RickaPrincy/patrimoine/pull
}
