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
  private static final LocalDate AJD = LocalDate.of(2025, JUNE, 23);

  static {
    // Ajout de la date "ajd" dans le scope avec la date fixe
    variableVisitor.addToScope("ajd", DATE, AJD);
  }

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
    // Utilisation de la variable "ajd" dans l'expression pour date fixe 2025-06-23
    String expr = "(400Ar + 300Ar - 300Ar) évalué Dates:ajd";

    Argent result = parseAndVisit(expr);

    assertEquals(ariary(400), result);
  }

  @Test
  void testDivisionSimple() throws IOException {
    String expr = "(100Ar / 2)* 200Ar";

    Argent result = parseAndVisit(expr);

    assertEquals(ariary(10000), result);
  }
}
