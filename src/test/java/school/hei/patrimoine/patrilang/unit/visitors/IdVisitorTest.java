package school.hei.patrimoine.patrilang.unit.visitors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.IdContext;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.IdVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

class IdVisitorTest {
  VariableVisitor variableVisitor = new VariableVisitor();
  IdVisitor subject = new IdVisitor(variableVisitor);
  UnitTestVisitor visitor =
      new UnitTestVisitor() {
        @Override
        public String visitId(IdContext ctx) {
          return subject.apply(ctx);
        }
      };

  @Test
  void parse_id_without_variable() {
    var input = "`id_value`";
    var expected = "id_value";

    var actual = visitor.visit(input, PatriLangParser::id);

    assertEquals(expected, actual);
  }

  @Test
  void parse_id_with_variable() {
    var formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");
    var ajd = LocalDate.of(2025, 6, 23);
    var input = "`id_value + Dates:ajd`";
    var expected = "id_value" + ajd.format(formatter);

    variableVisitor.addToScope("ajd", DATE, ajd);
    var actual = visitor.visit(input, PatriLangParser::id);

    assertEquals(expected, actual);
  }
}
