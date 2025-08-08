package school.hei.patrimoine.patrilang.visitors.variable;

import static java.lang.Double.parseDouble;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.mapper.DurationMapper.stringToDurationType;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.NOMBRE;
import static school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor.extractVariableName;

import java.time.Period;
import java.util.function.Supplier;
import lombok.AllArgsConstructor;
import school.hei.patrimoine.patrilang.antlr.PatriLangParserBaseVisitor;
import school.hei.patrimoine.patrilang.modele.variable.VariableScope;
import school.hei.patrimoine.patrilang.visitors.SimpleVisitor;

@AllArgsConstructor
public class VariableExpressionVisitor extends PatriLangParserBaseVisitor<Double>
    implements SimpleVisitor<ExpressionContext, Double> {
  private final VariableScope variableScope;
  private Supplier<VariableDateVisitor> variableDateVisitorSupplier;

  @Override
  public Double apply(ExpressionContext ctx) {
    return visit(ctx.additionExpr());
  }

  public Double apply(ExpressionArithmetiqueContext ctx) {
    Double current = visit(ctx.terme(0));

    for (int i = 1; i < ctx.terme().size(); i++) {
      Double next = visit(ctx.terme(i));
      if (ctx.PLUS(i-1) != null) {
        current += next;
      } else if (ctx.MOINS(i-1) != null) {
        current -= next;
      }
    }
    return current;
  }

  @Override
  public Double visitAdditionExpr(AdditionExprContext ctx) {
    // Visit the first operand
    Double result = visitMultiplicationExpr(ctx.multiplicationExpr(0));

    // Iterate over the remaining operands and operators
    for (int i = 1; i < ctx.getChildCount(); i += 2) {
      String operator = ctx.getChild(i).getText();
      Double rightOperand = visit(ctx.getChild(i + 1));

      switch (operator) {
        case "+" -> result += rightOperand;
        case "-" -> result -= rightOperand;
        default -> throw new IllegalStateException("Opérateur d'addition inconnu : " + operator);
      }
    }

    return result;
  }

  @Override
  public Double visitMultiplicationExpr(MultiplicationExprContext ctx) {
    // Visit the first operand
    Double result = visit(ctx.atom(0));

    // Iterate over remaining operands and operators
    for (int i = 1; i < ctx.getChildCount(); i += 2) {
      String operator = ctx.getChild(i).getText();
      Double rightOperand = visit(ctx.getChild(i + 1));

      switch (operator) {
        case "*" -> result *= rightOperand;
        case "/" -> result /= rightOperand;
        default ->
            throw new IllegalStateException("Opérateur de multiplication inconnu : " + operator);
      }
    }

    return result;
  }

  @Override
  public Double visitNegateExpr(NegateExprContext ctx) {
    return -visit(ctx.atom());
  }

  @Override
  public Double visitParenExpr(ParenExprContext ctx) {
    return visit(ctx.expression());
  }

  @Override
  public Double visitNombreExpr(NombreExprContext ctx) {
    return parseDouble(ctx.getText().replaceAll("_", ""));
  }

  @Override
  public Double visitDurationExpr(DurationExprContext ctx) {
    var lhs = this.variableDateVisitorSupplier.get().apply(ctx.duration().lhs);
    var rhs = this.variableDateVisitorSupplier.get().apply(ctx.duration().rhs);
    var type = stringToDurationType(ctx.duration().DUREE_UNITE().getText());

    return switch (type) {
      case YEARS -> (double) Period.between(rhs, lhs).getYears();
      case MONTH -> (double) Period.between(rhs, lhs).getMonths();
      case DAYS -> (double) Period.between(rhs, lhs).getDays();
    };
  }

  @Override
  public Double visitUniteDateDeExpr(UniteDateDeExprContext ctx) {
    var date = this.variableDateVisitorSupplier.get().apply(ctx.uniteDateDe().date());
    var type = stringToDurationType(ctx.uniteDateDe().UNITE_DATE_DE().getText());

    return switch (type) {
      case YEARS -> (double) date.getYear();
      case MONTH -> (double) date.getMonthValue();
      case DAYS -> (double) date.getDayOfMonth();
    };
  }

  @Override
  public Double visitNombreVariableExpr(NombreVariableExprContext ctx) {
    var name = extractVariableName(ctx.getText());
    return (Double) variableScope.get(name, NOMBRE).value();
  }
}
