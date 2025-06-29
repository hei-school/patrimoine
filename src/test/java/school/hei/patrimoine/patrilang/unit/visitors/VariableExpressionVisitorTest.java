package school.hei.patrimoine.patrilang.unit.visitors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.ExpressionContext;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.NOMBRE;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableExpressionVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

class VariableExpressionVisitorTest {
  VariableVisitor variableVisitor = new VariableVisitor();

  VariableExpressionVisitor subject =
      new VariableExpressionVisitor(variableVisitor.getVariableScope());

  UnitTestVisitor visitor =
      new UnitTestVisitor() {
        @Override
        public Double visitExpression(ExpressionContext ctx) {
          return subject.apply(ctx);
        }
      };

  @BeforeEach
  void setUp() {
    variableVisitor = new VariableVisitor();
    subject = new VariableExpressionVisitor(variableVisitor.getVariableScope());
  }

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

  @Test
  void parse_expression_with_variable() {
    var input = "-Nombres:myValeur";
    double expected = -1 * 4_000;

    variableVisitor.addToScope("myValeur", NOMBRE, 4_000d);
    var actual = visitor.visit(input, PatriLangParser::expression);

    assertEquals(expected, actual);
  }
}
