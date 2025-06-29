package school.hei.patrimoine.patrilang.unit.visitors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.ExpressionContext;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.ExpressionVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

class ExpressionVisitorTest {
  ExpressionVisitor subject = new ExpressionVisitor(new VariableVisitor());

  UnitTestVisitor visitor =
      new UnitTestVisitor() {
        @Override
        public Double visitExpression(ExpressionContext ctx) {
          return subject.apply(ctx);
        }
      };

  @Test
  void should_visit_simple_expression_ok() {
    var input = "6 * (2 + 1)";
    double expected = 6 * (2 + 1);

    var actual = visitor.visit(input, PatriLangParser::expression);

    assertEquals(expected, actual);
  }

  @Test
  void should_visit_expression_with_all_operators() {
    var input = "10 + 2 * 3 - 4 / 2";
    double expected = 10 + 2 * 3 - 4.0 / 2;

    var actual = visitor.visit(input, PatriLangParser::expression);

    assertEquals(expected, actual);
  }
}
