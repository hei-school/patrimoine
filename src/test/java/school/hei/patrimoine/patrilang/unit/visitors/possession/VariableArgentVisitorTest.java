package school.hei.patrimoine.patrilang.unit.visitors.possession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.ArgentContext;

import java.io.IOException;

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

  private Argent parse_and_visit(String input) throws IOException {
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
    String expr = "(100Ar / 2)* 200Ar";
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

}
