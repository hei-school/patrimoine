package school.hei.patrimoine.patrilang.unit.visitors.possession;

import static java.time.Month.JANUARY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Argent.euro;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.ArgentContext;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableArgentVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableDateVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableExpressionVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

public class VariableArgentVisitorTest {
  private static final VariableVisitor variableVisitor = new VariableVisitor();
  VariableArgentVisitor subject;
  UnitTestVisitor visitor;

  @BeforeEach
  void set_up() {
    subject = new VariableArgentVisitor(
            variableVisitor.getVariableScope(),
            () -> new VariableExpressionVisitor(variableVisitor.getVariableScope(), () -> new VariableDateVisitor(variableVisitor.getVariableScope(), () -> null)),
            () -> new VariableDateVisitor(variableVisitor.getVariableScope(), () -> new VariableExpressionVisitor(variableVisitor.getVariableScope(), () -> null))
    );

    visitor = new UnitTestVisitor() {
      @Override
      public Argent visitArgent(ArgentContext ctx) {
        return subject.apply(ctx);
      }
    };
  }

  private Argent parse_and_visit(String input) {
    return visitor.visit(input, PatriLangParser::argent);
  }

  @Test
  void addition_and_subtraction_with_date_variable_should_return_correct_value() throws IOException {
    String expr = "(400Ar - 300Ar + 300Ar)*2 évalué le 7 juillet 2024";
    Argent result = parse_and_visit(expr);
    assertEquals(ariary(800), result);
  }

  @Test
  void division_simple_should_return_correct_value() throws IOException {
    String expr = "(100Ar / 2)* 200";
    Argent result = parse_and_visit(expr);
    assertEquals(ariary(10000), result);
  }

  @Test
  void multiplication_simple_should_return_correct_value() throws IOException {
    String expr = "(100Ar * 2)";
    Argent result = parse_and_visit(expr);
    assertEquals(ariary(200), result);
  }

  @Test
  void addition_simple_with_date_should_return_correct_value() throws IOException {
    String expr = "(100Ar + 100Ar) évalué le 7 juillet 2024 ";
    Argent result = parse_and_visit(expr);
    assertEquals(ariary(200), result);
  }

  @Test
  void amount_with_currency_should_be_recognized() throws IOException {
    String expr = "100Ar";
    var result = parse_and_visit(expr);
    assertEquals(ariary(100), result);
  }

  @Test
  void subtraction_without_date_should_throw_exception() {
    String expr = "300Ar - 100Ar";
    assertThrows(IllegalArgumentException.class, () -> parse_and_visit(expr));
  }

  @Test
  void parentheses_precedence_should_be_respected() throws IOException {
    String expr = "100Ar + (200Ar * 2) évalué le 7 juillet 2024";
    Argent result = parse_and_visit(expr);
    assertEquals(ariary(500), result);
  }

  @Test
  void nested_parentheses_should_be_evaluated_correctly() throws IOException {
    String expr = "((50Ar + 50Ar) * 2) évalué le 7 juillet 2024";
    Argent result = parse_and_visit(expr);
    assertEquals(ariary(200), result);
  }

  @Test
  void division_with_parentheses_should_return_correct_value() throws IOException {
    String expr = "200Ar / (2)";
    Argent result = parse_and_visit(expr);
    assertEquals(ariary(100), result);
  }

  @Test
  void complex_expression_should_return_correct_value() throws IOException {
    String expr = "((100Ar + 200Ar) / 3) * 2 évalué le 7 juillet 2024";
    Argent result = parse_and_visit(expr);
    assertEquals(ariary(200), result);
  }

  @Test
  void evaluation_with_addition_date_should_return_correct_value() throws IOException {
    String expr = "100Ar + 150Ar évalué le 6 juin 2025";
    Argent result = parse_and_visit(expr);
    assertEquals(ariary(250), result);
  }

  @Test
  void evaluation_with_multiplication_date_should_return_correct_value() throws IOException {
    String expr = "(50Ar * 4) évalué le 15 mars 2023";
    Argent result = parse_and_visit(expr);
    assertEquals(ariary(200), result);
  }

  @Test
  void evaluation_with_complex_expression_and_date_should_return_correct_value() throws IOException {
    String expr = "(100Ar + 200Ar - 50Ar) * 2 évalué le 1 janvier 2024";
    Argent result = parse_and_visit(expr);
    assertEquals(ariary(500), result);
  }

  @Test
  void evaluation_with_explicit_date_keyword_should_return_correct_value() throws IOException {
    String expr = "100Ar + 5$ évalué le 1 janvier 2024";
    Argent result = parse_and_visit(expr);
    assertEquals(ariary(16270), result);
  }

  @Test
  void invalid_expression_should_throw_unsupported_operation_exception() {
    String expr = "valeur_marche_compte";
    assertThrows(UnsupportedOperationException.class, () -> parse_and_visit(expr));
  }

  @Test
  void mixed_currency_with_parentheses_and_date_should_return_correct_value() throws IOException {
    String expr = "100Ar + (5$ * 2) évalué le 10 octobre 2024";
    Argent result = parse_and_visit(expr);
    assertEquals(ariary(33210), result);
  }

  @Test
  void complex_nested_operations_with_multiple_parentheses_and_date() throws IOException {
    String expr = "((50Ar + (25Ar * 3)) / 5) - (10Ar + 5Ar) évalué le 12 décembre 2024";
    Argent result = parse_and_visit(expr);
    assertEquals(ariary(10), result);
  }

  @Test
  void division_resulting_in_decimal_should_truncate_or_round_correctly() {
    String expr = "(100Ar / 4) * 3 évalué le 5 mai 2024";
    Argent result = parse_and_visit(expr);
    assertEquals(ariary(75), result);
  }

  @Test
  void chained_multiplication_and_division_with_currency_and_date_should_return_correct_value() throws IOException {
    String expr = "2€ + 3€ - 3€ + 4€/ 2 évalué le 8 août 2024";
    Argent result = parse_and_visit(expr);
    assertEquals(euro(4), result);
  }

  @Test
  void undefined_variable_in_expression_should_throw_exception() {
    String expr = "inconnu + 100Ar évalué le 1 janvier 2024";
    assertThrows(UnsupportedOperationException.class, () -> parse_and_visit(expr));
  }

  @Test
  void very_complex_long_expression_with_argent() throws IOException {
    var input = "(4000Ar + 8000Ar - 4000Ar * 2 + 4000Ar) * 3 / 3 * 2 / 2 évalué le 12 Janvier 2025";
    var actual = parse_and_visit(input);

    var dateEvaluation = LocalDate.of(2025, JANUARY, 12);
    var expected = ariary(4_000).add(ariary(8000), dateEvaluation).minus(ariary(4000), dateEvaluation);
    assertEquals(expected, actual);
  }



}
