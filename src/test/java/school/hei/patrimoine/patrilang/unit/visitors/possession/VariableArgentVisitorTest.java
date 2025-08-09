package school.hei.patrimoine.patrilang.unit.visitors.possession;

import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.ArgentContext;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.NOMBRE;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableArgentVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableDateVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableExpressionVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

class VariableArgentVisitorTest {
  VariableVisitor variableVisitor = new VariableVisitor();
  VariableArgentVisitor subject =
      new VariableArgentVisitor(
          variableVisitor.getVariableScope(),
          new VariableExpressionVisitor(
              variableVisitor.getVariableScope(), variableVisitor::getVariableDateVisitor),
          new VariableDateVisitor(
              variableVisitor.getVariableScope(), variableVisitor::getVariableExpressionVisitor));

  UnitTestVisitor visitor =
      new UnitTestVisitor() {
        @Override
        public Object visitArgent(ArgentContext ctx) {
          return subject.apply(ctx);
        }
      };

  @BeforeEach
  void setUp() {
    variableVisitor = new VariableVisitor();
    subject =
        new VariableArgentVisitor(
            variableVisitor.getVariableScope(),
            new VariableExpressionVisitor(
                variableVisitor.getVariableScope(), variableVisitor::getVariableDateVisitor),
            new VariableDateVisitor(
                variableVisitor.getVariableScope(), variableVisitor::getVariableExpressionVisitor));
  }

  @Test
  void parse_argent_without_expression() {
    var input = "300000Ar";
    var expected = ariary(300_000);

    var actual = visitor.visit(input, PatriLangParser::argent);
    System.out.println(actual);
    assertEquals(expected, actual);
  }

  @Test
  void parse_argent_with_negation() {
    var input = "-300_000Ar";
    var expected = ariary(-300_000);

    var actual = visitor.visit(input, PatriLangParser::argent);

    assertEquals(expected, actual);
  }

  @Test
  void parse_argent_with_expression() {
    var input = "(300 `/*comment1*/` +  450 `/*comment2*/`)Ar";
    var expected = ariary(750);

    var actual = visitor.visit(input, PatriLangParser::argent);

    assertEquals(expected, actual);
  }

  @Test
  void parse_int_argent_with_underscore() {
    var input = "300_000_000Ar";
    var expected = ariary(300_000_000);

    var actual = visitor.visit(input, PatriLangParser::argent);

    assertEquals(expected, actual);
  }

  @Test
  void parse_int_argent_with_variable() {
    var input = "(Nombres:valeurOrdinateur)Ar";
    var expected = ariary(300_000_000);

    variableVisitor.addToScope("valeurOrdinateur", NOMBRE, 300_000_000d);
    var actual = visitor.visit(input, PatriLangParser::argent);

    assertEquals(expected, actual);
  }

  @Test
  void parse_argent_with_all_specification() {
    var valeurOrdinateur = 300_000_000;
    var input = "(-Nombres:valeurOrdinateur * 2 / 4)Ar";
    var expected = ariary(-1 * valeurOrdinateur * 2 / 4);

    variableVisitor.addToScope("valeurOrdinateur", NOMBRE, (double) valeurOrdinateur);
    var actual = visitor.visit(input, PatriLangParser::argent);

    assertEquals(expected, actual);
  }

  @Test
  void parse_argent_with_addition() {
    var input = "(300000Ar + 450000Ar) évalué le 01 Juin 2025";
    var expected = ariary(750_000);

    var actual = visitor.visit(input, PatriLangParser::argent);
    assertEquals(expected, actual);
  }

  @Test
  void parse_argent_with_subtraction() {
    var input = "(500000Ar - 200000Ar) évalué le 01 Mai 2025";
    var expected = ariary(300_000);

    var actual = visitor.visit(input, PatriLangParser::argent);
    assertEquals(expected, actual);
  }

  @Test
  void parse_argent_with_multiplication_by_number() {
    var input = "(300000Ar * 2)";
    var expected = ariary(600_000);

    var actual = visitor.visit(input, PatriLangParser::argent);
    assertEquals(expected, actual);
  }

  @Test
  void parse_argent_with_division_by_number() {
    var input = "(600000Ar / 2)";
    var expected = ariary(300_000);

    var actual = visitor.visit(input, PatriLangParser::argent);
    assertEquals(expected, actual);
  }

  @Test
  void parse_argent_all_complexe_operation() {
    var input = "((((50000Ar + 50000Ar) / 2) * 5) * 5) évalué le 01 Juin 2025";
    var expected = ariary(1_250_000);

    var actual = visitor.visit(input, PatriLangParser::argent);
    assertEquals(expected, actual);
  }
}
