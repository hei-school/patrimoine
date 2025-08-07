package school.hei.patrimoine.patrilang.unit.visitors.possession;

import static java.time.Month.JUNE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.ArgentContext;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;

import java.io.IOException;
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
  void setUp() {
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

  private Argent parseAndVisit(String input) throws IOException {
    return visitor.visit(input, PatriLangParser::argent);
  }

  @Test
  void testAdditionSoustractionAvecDateVariableAjd() throws IOException {
    String expr = "(400Ar - 300Ar +300Ar)*2 évalué le 7 juillet 2024";
    Argent result = parseAndVisit(expr);
    assertEquals(ariary(800), result);
  }

  @Test
  void testDivisionSimple() throws IOException {
    String expr = "(100Ar / 2)* 200Ar";
    Argent result = parseAndVisit(expr);
    assertEquals(ariary(10000), result);
  }

  @Test
  void testMultiSimple() throws IOException {
    String expr = "(100Ar * 2)";
    Argent result = parseAndVisit(expr);
    assertEquals(ariary(200), result);
  }

  @Test
  void testAdditionSimple() throws IOException {
    String expr = "(100Ar + 100Ar)";
    Argent result = parseAndVisit(expr);
    assertEquals(ariary(200), result);
  }

  @Test
  void testMontantDeviseRecognition() throws IOException {
    String expr = "100Ar";
    var result = parseAndVisit(expr);
    assertEquals(ariary(100), result);
  }

  @Test
  void testSubtractionSimple() throws IOException {
    String expr = "300Ar - 100Ar";
    Argent result = parseAndVisit(expr);
    assertEquals(ariary(200), result);
  }

  @Test
  void testParenthesesPrecedence() throws IOException {
    String expr = "100Ar + (200Ar * 2)";
    Argent result = parseAndVisit(expr);
    assertEquals(ariary(500), result);
  }

  @Test
  void testNestedParentheses() throws IOException {
    String expr = "((50Ar + 50Ar) * 2)";
    Argent result = parseAndVisit(expr);
    assertEquals(ariary(200), result);
  }

  @Test
  void testDivisionWithParentheses() throws IOException {
    String expr = "200Ar / (2)";
    Argent result = parseAndVisit(expr);
    assertEquals(ariary(100), result);
  }

  @Test
  void testComplexExpression() throws IOException {
    String expr = "((100Ar + 200Ar) / 3) * 2";
    Argent result = parseAndVisit(expr);
    assertEquals(ariary(200), result);
  }

  @Test
  void testEvaluationDateAvecAddition() throws IOException {
    String expr = "100Ar + 150Ar évalué le 6 juin 2025";
    Argent result = parseAndVisit(expr);
    assertEquals(ariary(250), result);
  }

  @Test
  void testEvaluationDateAvecMultiplication() throws IOException {
    String expr = "(50Ar * 4) évalué le 15 mars 2023";
    Argent result = parseAndVisit(expr);
    assertEquals(ariary(200), result);
  }

  @Test
  void testEvaluationDateAvecComplexe() throws IOException {
    String expr = "(100Ar + 200Ar - 50Ar) * 2 évalué le 1 janvier 2024";
    Argent result = parseAndVisit(expr);
    assertEquals(ariary(500), result);
  }

  @Test
  void testEvaluationAvecAjdMotCle() throws IOException {
    String expr = "100Ar + 5$ évalué le ajd";
    Argent result = parseAndVisit(expr);
    assertEquals(ariary(17060), result);
  }

  @Test
  void testEvaluationAvecDateRelative() throws IOException {
    String expr = "200Ar + 100Ar évalué le dans 3 jours";
    Argent result = parseAndVisit(expr);
    assertEquals(ariary(300), result);
  }
}


