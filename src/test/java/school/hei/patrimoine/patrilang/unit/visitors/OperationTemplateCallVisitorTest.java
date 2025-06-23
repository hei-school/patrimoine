package school.hei.patrimoine.patrilang.unit.visitors;

import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.OperationTemplateCallContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.OperationsContext;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.*;
import static school.hei.patrimoine.patrilang.utils.Comparator.assertFluxArgentEquals;
import static school.hei.patrimoine.patrilang.utils.UnitTestVisitor.createParser;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.modele.template.OperationTemplate;
import school.hei.patrimoine.patrilang.modele.template.OperationTemplateParam;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.OperationTemplateCallVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

class OperationTemplateCallVisitorTest {
  private static final VariableVisitor variableVisitor = new VariableVisitor();
  private static final LocalDate AJD = LocalDate.of(2025, 6, 23);
  private static final LocalDate DATE_FIN = LocalDate.of(2025, 12, 23);
  private static final Compte COMPTE_PERSONNEL = new Compte("comptePersonnel", AJD, ariary(4_000));

  OperationTemplateCallVisitor subject = new OperationTemplateCallVisitor(variableVisitor);

  UnitTestVisitor visitor =
      new UnitTestVisitor() {
        @Override
        public Set<Possession> visitOperationTemplateCall(OperationTemplateCallContext ctx) {
          return subject.apply(ctx);
        }
      };

  static {
    variableVisitor.addToScope("ajd", DATE, AJD);
    variableVisitor.addToScope("comptePersonnel", TRESORERIES, COMPTE_PERSONNEL);
  }

  @Test
  void parse_call_without_args() {
    var templateContent =
        createContent(
            """
    * `salaireMensuel` Dates:ajd, entrer 4000Ar vers Trésoreries:comptePersonnel, jusqu'à date indéterminée tous les 31 du mois,
""");
    var operationTemplate = new OperationTemplate("myTemplate", List.of(), templateContent);
    variableVisitor.addToScope("myTemplate", OPERATION_TEMPLATE, operationTemplate);

    var input = "* myTemplate()";
    var expected =
        new FluxArgent("salaireMensuel", COMPTE_PERSONNEL, AJD, LocalDate.MAX, 31, ariary(4_000));
    Set<FluxArgent> operations = visitor.visit(input, PatriLangParser::operationTemplateCall);

    var actual = operations.iterator().next();
    assertFluxArgentEquals(expected, actual);
  }

  @Test
  void parse_call_with_args() {
    var templateContent =
        createContent(
            """
    * `salaireMensuel` Dates:ajd, entrer 4000Ar vers Trésoreries:compte, jusqu'à Dates:dateFin tous les 31 du mois,
""");
    var params =
        List.of(
            new OperationTemplateParam("compte", TRESORERIES),
            new OperationTemplateParam("dateFin", DATE));
    var operationTemplate = new OperationTemplate("charges", params, templateContent);
    variableVisitor.addToScope("charges", OPERATION_TEMPLATE, operationTemplate);

    var input = "* charges(Trésoreries:comptePersonnel, le 23 du 12-2025)";
    var expected =
        new FluxArgent("salaireMensuel", COMPTE_PERSONNEL, AJD, DATE_FIN, 31, ariary(4_000));
    Set<FluxArgent> operations = visitor.visit(input, PatriLangParser::operationTemplateCall);

    var actual = operations.iterator().next();
    assertFluxArgentEquals(expected, actual);
  }

  private static List<OperationsContext> createContent(String input) {
    var parser = createParser(input);
    return List.of(parser.operations());
  }
}
