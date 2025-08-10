package school.hei.patrimoine.patrilang.unit.visitors;

import static java.time.Month.JANUARY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Argent.euro;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.ArgentContext;

import java.time.LocalDate;
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
    subject =
        new VariableArgentVisitor(
            variableVisitor.getVariableScope(),
            () ->
                new VariableExpressionVisitor(
                    variableVisitor.getVariableScope(),
                    () -> new VariableDateVisitor(variableVisitor.getVariableScope(), () -> null)),
            () ->
                new VariableDateVisitor(
                    variableVisitor.getVariableScope(),
                    () ->
                        new VariableExpressionVisitor(
                            variableVisitor.getVariableScope(), () -> null)));

    visitor =
        new UnitTestVisitor() {
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
  void addition_and_subtraction_with_date_variable_should_return_correct_value() {
    var input = "(400Ar - 300Ar + 300Ar)*2 évalué le 7 juillet 2024";
    var actual = parse_and_visit(input);

    var dateEvaluation = LocalDate.of(2024, 7, 7);
    var expected =
        ariary(400).minus(ariary(300), dateEvaluation).add(ariary(300), dateEvaluation).mult(2);
    assertEquals(expected, actual);
  }

  @Test
  void division_simple_should_return_correct_value() {
    var input = "(100Ar / 2)* 200";
    var actual = parse_and_visit(input);

    var expected = ariary(100).div(2).mult(200);
    assertEquals(expected, actual);
  }

  @Test
  void multiplication_simple_should_return_correct_value() {
    var input = "(100Ar * 2)";
    var actual = parse_and_visit(input);

    var expected = ariary(100).mult(2);
    assertEquals(expected, actual);
  }

  @Test
  void addition_simple_with_date_should_return_correct_value() {
    var input = "(100Ar + 100Ar) évalué le 7 juillet 2024";
    var actual = parse_and_visit(input);

    var dateEvaluation = LocalDate.of(2024, 7, 7);
    var expected = ariary(100).add(ariary(100), dateEvaluation);
    assertEquals(expected, actual);
  }

  @Test
  void amount_with_currency_should_be_recognized() {
    var input = "100Ar";
    var actual = parse_and_visit(input);

    var expected = ariary(100);
    assertEquals(expected, actual);
  }

  @Test
  void subtraction_without_date_should_throw_exception() {
    var input = "300Ar - 100Ar";
    assertThrows(IllegalArgumentException.class, () -> parse_and_visit(input));
  }

  @Test
  void parentheses_precedence_should_be_respected() {
    var input = "100Ar + (200Ar * 2) évalué le 7 juillet 2024";
    var actual = parse_and_visit(input);

    var dateEvaluation = LocalDate.of(2024, 7, 7);
    var expected = ariary(100).add(ariary(200).mult(2), dateEvaluation);
    assertEquals(expected, actual);
  }

  @Test
  void nested_parentheses_should_be_evaluated_correctly() {
    var input = "((50Ar + 50Ar) * 2) évalué le 7 juillet 2024";
    var actual = parse_and_visit(input);

    var dateEvaluation = LocalDate.of(2024, 7, 7);
    var expected = ariary(50).add(ariary(50), dateEvaluation).mult(2);
    assertEquals(expected, actual);
  }

  @Test
  void division_with_parentheses_should_return_correct_value() {
    var input = "200Ar / (2)";
    var actual = parse_and_visit(input);

    var expected = ariary(200).div(2);
    assertEquals(expected, actual);
  }

  @Test
  void complex_expression_should_return_correct_value() {
    var input = "((100Ar + 200Ar) / 3) * 2 évalué le 7 juillet 2024";
    var actual = parse_and_visit(input);

    var dateEvaluation = LocalDate.of(2024, 7, 7);
    var expected = ariary(100).add(ariary(200), dateEvaluation).div(3).mult(2);
    assertEquals(expected, actual);
  }

  @Test
  void evaluation_with_addition_date_should_return_correct_value() {
    var input = "100Ar + 150Ar évalué le 6 juin 2025";
    var actual = parse_and_visit(input);

    var dateEvaluation = LocalDate.of(2025, 6, 6);
    var expected = ariary(100).add(ariary(150), dateEvaluation);
    assertEquals(expected, actual);
  }

  @Test
  void evaluation_with_multiplication_date_should_return_correct_value() {
    var input = "(50Ar * 4) évalué le 15 mars 2023";
    var actual = parse_and_visit(input);

    var dateEvaluation = LocalDate.of(2023, 3, 15);
    var expected = ariary(50).mult(4);
    assertEquals(expected, actual);
  }

  @Test
  void evaluation_with_complex_expression_and_date_should_return_correct_value() {
    var input = "(100Ar + 200Ar - 50Ar) * 2 évalué le 1 janvier 2024";
    var actual = parse_and_visit(input);

    var dateEvaluation = LocalDate.of(2024, 1, 1);
    var expected =
        ariary(100).add(ariary(200), dateEvaluation).minus(ariary(50), dateEvaluation).mult(2);
    assertEquals(expected, actual);
  }

  @Test
  void evaluation_with_explicit_date_keyword_should_return_correct_value() {
    var input = "100Ar + 5€ évalué le 1 janvier 2024";
    var actual = parse_and_visit(input);

    var dateEvaluation = LocalDate.of(2024, 1, 1);
    var expected = ariary(100).add(euro(5), dateEvaluation);
    assertEquals(expected, actual);
  }

  @Test
  void invalid_expression_should_throw_unsupported_operation_exception() {
    var input = "valeur_marche_compte";
    assertThrows(UnsupportedOperationException.class, () -> parse_and_visit(input));
  }

  @Test
  void mixed_currency_with_parentheses_and_date_should_return_correct_value() {
    var input = "100Ar + (5€ * 2) évalué le 10 octobre 2024";
    var actual = parse_and_visit(input);

    var dateEvaluation = LocalDate.of(2024, 10, 10);
    var expected = ariary(100).add(euro(5).mult(2), dateEvaluation);
    assertEquals(expected, actual);
  }

  @Test
  void complex_nested_operations_with_multiple_parentheses_and_date() {
    var input = "((50Ar + (25Ar * 3)) / 5) - (10Ar + 5Ar) évalué le 12 décembre 2024";
    var actual = parse_and_visit(input);

    var dateEvaluation = LocalDate.of(2024, 12, 12);
    var expected =
        ariary(50)
            .add(ariary(25).mult(3), dateEvaluation)
            .div(5)
            .minus(ariary(10).add(ariary(5), dateEvaluation), dateEvaluation);
    assertEquals(expected, actual);
  }

  @Test
  void division_resulting_in_decimal_should_truncate_or_round_correctly() {
    var input = "(100Ar / 4) * 3 évalué le 5 mai 2024";
    var actual = parse_and_visit(input);

    var dateEvaluation = LocalDate.of(2024, 5, 5);
    var expected = ariary(100).div(4).mult(3);
    assertEquals(expected, actual);
  }

  @Test
  void chained_multiplication_and_division_with_currency_and_date_should_return_correct_value() {
    var input = "2€ + 3€ - 3€ + 4€/ 2 évalué le 8 août 2024";
    var actual = parse_and_visit(input);

    var dateEvaluation = LocalDate.of(2024, 8, 8);
    var expected =
        euro(2)
            .add(euro(3), dateEvaluation)
            .minus(euro(3), dateEvaluation)
            .add(euro(4).div(2), dateEvaluation);
    assertEquals(expected, actual);
  }

  @Test
  void undefined_variable_in_expression_should_throw_exception() {
    var input = "inconnu + 100Ar évalué le 1 janvier 2024";
    assertThrows(UnsupportedOperationException.class, () -> parse_and_visit(input));
  }

  @Test
  void very_complex_long_expression_with_argent() {
    var input =
        "(4000Ar + (-(-8000Ar)) - 4000Ar * 2 + 4000Ar) * 3 / 3 * 2 / 2 évalué le 12 Janvier 2025";
    var actual = parse_and_visit(input);

    var dateEvaluation = LocalDate.of(2025, JANUARY, 12);
    var expected =
        ariary(4_000).add(ariary(8000), dateEvaluation).minus(ariary(4000).mult(2), dateEvaluation);
    assertEquals(expected, actual);
  }
}
