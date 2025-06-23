package school.hei.patrimoine.patrilang.unit.visitors.possession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.ArgentContext;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.ExpressionVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.ArgentVisitor;

class ArgentVisitorTest {
  ArgentVisitor subject = new ArgentVisitor(new ExpressionVisitor());

  UnitTestVisitor visitor =
      new UnitTestVisitor() {
        @Override
        public Argent visitArgent(ArgentContext ctx) {
          return subject.apply(ctx);
        }
      };

  @Test
  void parse_argent_without_expression() {
    var input = "300000Ar";
    var expected = ariary(300_000);

    var actual = visitor.visit(input, PatriLangParser::argent);

    assertEquals(expected, actual);
  }

  @Test
  void parse_argent_with_negation() {
    var input = "-300000Ar";
    var expected = ariary(-300_000);

    var actual = visitor.visit(input, PatriLangParser::argent);

    assertEquals(expected, actual);
  }

  @Test
  void parse_argent_with_with_expression() {
    var input = "(300 `/*comment1*/` +  450 `/*comment2*/`)Ar";
    var expected = ariary(750);

    var actual = visitor.visit(input, PatriLangParser::argent);

    assertEquals(expected, actual);
  }
}
