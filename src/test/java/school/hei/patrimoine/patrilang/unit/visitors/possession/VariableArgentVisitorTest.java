package school.hei.patrimoine.patrilang.unit.visitors.possession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.ArgentContext;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.patrilang.antlr.PatriLangLexer;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableArgentVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableDateVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableExpressionVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

public class VariableArgentVisitorTest {

  VariableVisitor variableVisitor;
  VariableArgentVisitor subject;
  UnitTestVisitor visitor;

  @BeforeEach
  void setUp() {
    variableVisitor = new VariableVisitor();

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
  void testAdditionSoustractionAvecDate() throws IOException {
    // Expression : (400 Ar + 300 Ar - 300 Ar) évalué le 21 janvier 2024
    // Adapte la syntaxe exacte à ta grammaire, par exemple avec "évalué le" ou "MOT_EVALUER le"
    String expr = "(400Ar + 300Ar - 300Ar) évalué le 21 janvier 2024";

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
