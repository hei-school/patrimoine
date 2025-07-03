package school.hei.patrimoine.patrilang.unit.visitors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.OperationTemplateContext;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.modele.template.OperationTemplate;
import school.hei.patrimoine.patrilang.modele.template.OperationTemplateParam;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.OperationTemplateVisitor;

class OperationTemplateVisitorTest {
  OperationTemplateVisitor subject = new OperationTemplateVisitor();

  UnitTestVisitor visitor =
      new UnitTestVisitor() {
        @Override
        public OperationTemplate visitOperationTemplate(OperationTemplateContext ctx) {
          return subject.apply(ctx);
        }
      };

  @Test
  void parse_template_without_params() {
    var input = "## charges()";
    var expected = new OperationTemplate("charges", List.of(), List.of());

    var actual = visitor.visit(input, PatriLangParser::operationTemplate);

    assertEquals(expected, actual);
  }

  @Test
  void parse_template_with_params() {
    var input = "## charges(Dates:date,Créances:creance, Trésoreries:compte)";
    var expectedParams =
        List.of(
            new OperationTemplateParam("date", DATE),
            new OperationTemplateParam("creance", CREANCE),
            new OperationTemplateParam("compte", TRESORERIES));

    var expected = new OperationTemplate("charges", expectedParams, List.of());

    var actual = visitor.visit(input, PatriLangParser::operationTemplate);

    assertEquals(expected, actual);
  }
}
