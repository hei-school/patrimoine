package school.hei.patrimoine.patrilang.unit.visitors.possession;

import static java.time.Month.JUNE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.CompteContext;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.DetteVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

class DetteVisitorTest {
  private static final LocalDate AJD = LocalDate.of(2025, JUNE, 23);
  private static final DetteVisitor subject = new DetteVisitor(new VariableVisitor());

  UnitTestVisitor visitor =
      new UnitTestVisitor() {
        @Override
        public Dette visitCompte(CompteContext ctx) {
          return subject.apply(ctx);
        }
      };

  @Test
  void parser_dette() {
    var input =
        """
        * detteCompte, valant 200000Ar le 23 juin 2025
        """;

    var expected = new Dette("detteCompte", AJD, ariary(-200_000));
    var actual = visitor.visit(input, PatriLangParser::compte);

    assertEquals(expected, actual);
  }
}
