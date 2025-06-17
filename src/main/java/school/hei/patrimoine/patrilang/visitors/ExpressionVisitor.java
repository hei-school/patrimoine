package school.hei.patrimoine.patrilang.visitors;

import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.antlr.PatriLangParserBaseVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.SimpleVisitor;

public class ExpressionVisitor extends PatriLangParserBaseVisitor<Double>
    implements SimpleVisitor<PatriLangParser.ExpressionContext, Double> {

  @Override
  public Double apply(PatriLangParser.ExpressionContext ctx) {
    return visitAdditionExpr(ctx.additionExpr());
  }

  @Override
  public Double visitAdditionExpr(PatriLangParser.AdditionExprContext ctx) {
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
  public Double visitMultiplicationExpr(PatriLangParser.MultiplicationExprContext ctx) {
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
  public Double visitNegateExpr(PatriLangParser.NegateExprContext ctx) {
    return -visit(ctx.atom());
  }

  @Override
  public Double visitParenExpr(PatriLangParser.ParenExprContext ctx) {
    return visit(ctx.expression());
  }

  @Override
  public Double visitNombreExpr(PatriLangParser.NombreExprContext ctx) {
    return BaseVisitor.visitNombre(ctx.nombre());
  }
}
