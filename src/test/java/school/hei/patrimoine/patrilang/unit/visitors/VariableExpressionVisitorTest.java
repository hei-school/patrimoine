package school.hei.patrimoine.patrilang.unit.visitors;

import static java.time.Month.FEBRUARY;
import static java.time.Month.JANUARY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.ExpressionContext;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.NOMBRE;

import java.time.LocalDate;
import java.time.Period;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableExpressionVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

class VariableExpressionVisitorTest {
  VariableVisitor variableVisitor = new VariableVisitor();

  VariableExpressionVisitor subject;

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
    subject =
            new VariableExpressionVisitor(
                    variableVisitor.getVariableScope(), variableVisitor::getVariableDateVisitor);
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
    variableVisitor.addToScope("myValeur", NOMBRE, 4_000d);
    var input = "-Nombres:myValeur";
    double expected = -1 * 4_000;
    var actual = visitor.visit(input, PatriLangParser::expression);
    assertEquals(expected, actual);
  }

  @Test
  void parse_date_expression_as_nombre() {
    variableVisitor.addToScope("myValeur", NOMBRE, 4_000d);
    var input = "(le 3 janvier 2025 - le 1 janvier 2000 en années) + Nombres:myValeur";
    double expected =
            Period.between(LocalDate.of(2000, JANUARY, 1), LocalDate.of(2025, JANUARY, 3)).getYears()
                    + 4_000d;
    var actual = visitor.visit(input, PatriLangParser::expression);
    assertEquals(expected, actual);
  }

  @Test
  void parse_date_variable_expression_as_nombre() {
    var au1Janvier2000 = LocalDate.of(2000, JANUARY, 1);
    variableVisitor.addToScope("myDate", DATE, au1Janvier2000);
    var input = "(le 1 janvier 2025 - Dates:myDate en années)";
    double expected = Period.between(au1Janvier2000, LocalDate.of(2025, JANUARY, 1)).getYears();
    var actual = visitor.visit(input, PatriLangParser::expression);
    assertEquals(expected, actual);
  }

  @Test
  void parse_nombre_from_date_using_date_de() {
    var au1Fevrier2000 = LocalDate.of(2000, FEBRUARY, 1);
    variableVisitor.addToScope("au1Janvier2025", DATE, au1Fevrier2000);
    var input = "(mois de Dates:au1Janvier2025)";
    double expected = au1Fevrier2000.getMonthValue();
    var actual = visitor.visit(input, PatriLangParser::expression);
    assertEquals(expected, actual);
  }

  // Nouveaux tests supplémentaires
  @Test
  void should_throw_for_undefined_variable() {
    var input = "Nombres:inconnue";
    assertThrows(IllegalArgumentException.class, () -> visitor.visit(input, PatriLangParser::expression));
  }

  @Test
  void should_handle_nested_parentheses() {
    var input = "((2 + 3) * (4 - 1)) / 5";
    double expected = ((2 + 3) * (4 - 1)) / 5.0;
    var actual = visitor.visit(input, PatriLangParser::expression);
    assertEquals(expected, actual);
  }

  @Test
  void should_handle_negative_numbers() {
    var input = "-(5 + 3)";
    double expected = -8;
    var actual = visitor.visit(input, PatriLangParser::expression);
    assertEquals(expected, actual);
  }
}