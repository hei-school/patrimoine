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
      if (ctx.expressionArithmetique() != null && ctx.date() == null) {
        throw new IllegalArgumentException("Une date d'évaluation est requise pour les conversions de devise");
      }

      if (ctx.expressionArithmetique() != null) {
        Argent result = evaluateExpression(ctx.expressionArithmetique());
        requireEvaluationDateIfNeeded(result, ctx.date());
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
      return ctx.PLUS() != null ? lhs.add(rhs, null) : lhs.minus(rhs, null);
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
    log.info("evaluateExpression: {}", expr.getText());
    Argent current = evaluateTerme(expr.terme(0));

    for (int i = 1; i < expr.terme().size(); i++) {
      TermeContext terme = expr.terme(i);
      Argent next = evaluateTerme(terme);

      if (expr.PLUS(i-1) != null) {
        current = current.add(next, null);
      } else if (expr.MOINS(i-1) != null) {
        current = current.minus(next, null);
      } else {
        throw new IllegalArgumentException("Seules les opérations d'addition et soustraction sont autorisées entre valeurs d'argent");
      }
    }
    return current;
  }

  private Argent evaluateTerme(TermeContext terme) {
    log.info("evaluateTerme: {}", terme.getText());

    Argent result = evaluateFacteur(terme.facteur());

    for (int i = 0; i < terme.scalar().size(); i++) {
      ScalarContext scalar = terme.scalar(i);
      double valeur;

      if (scalar.nombre() != null) {
        valeur = Double.parseDouble(scalar.nombre().getText());
      } else if (scalar.expressionArithmetique() != null) {
        throw new IllegalArgumentException("Multiplication ou division entre montants d'argent interdite.");
      } else {
        throw new IllegalArgumentException("Opération invalide : facteur non numérique.");
      }

      if (terme.MUL(i) != null) {
        result = result.mult(valeur);
      } else if (terme.DIV(i) != null) {
        if (valeur == 0) throw new ArithmeticException("Division par zéro");
        result = result.div(valeur);
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
    log.info("evaluateFacteur: {}", facteur.getText());
    if (facteur.argentValue() != null) {
      return visitArgentValue(facteur.argentValue());
    }

    if (facteur.expressionArithmetique() != null) {
      throw new IllegalArgumentException(
              "Multiplication ou division entre montants d'argent interdite");
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
