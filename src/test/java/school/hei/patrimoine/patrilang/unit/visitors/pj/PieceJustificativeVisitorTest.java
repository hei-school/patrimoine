package school.hei.patrimoine.patrilang.unit.visitors.pj;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.IdVisitor;
import school.hei.patrimoine.patrilang.visitors.PatriLangPieceJustificativeVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableDateVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableExpressionVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

public class PieceJustificativeVisitorTest {
  private PatriLangPieceJustificativeVisitor subject;
  private UnitTestVisitor visitor;

  @BeforeEach
  void setup() {
    var variableVisitor = new VariableVisitor();
    var variableScope = variableVisitor.getVariableScope();
    variableVisitor.addToScope("ajd", DATE, LocalDate.of(2025, 12, 24));

    final class Holder<T> {
      T value;
    }
    final Holder<VariableDateVisitor> dateHolder = new Holder<>();
    final Holder<VariableExpressionVisitor> exprHolder = new Holder<>();

    var expressionVisitor = new VariableExpressionVisitor(variableScope, () -> dateHolder.value);
    var dateVisitor = new VariableDateVisitor(variableScope, () -> exprHolder.value);

    dateHolder.value = dateVisitor;
    exprHolder.value = expressionVisitor;

    var idVisitor = new IdVisitor(variableVisitor);

    subject = new PatriLangPieceJustificativeVisitor(idVisitor, dateVisitor);

    visitor =
        new UnitTestVisitor() {
          @Override
          public List<PieceJustificative> visitPiecesJustificatives(
              PatriLangParser.PiecesJustificativesContext ctx) {
            return subject.apply(ctx);
          }
        };
  }

  @Test
  void parse_pieces_justificatives() {
    var input =
        """
# Général
* Spécifier Dates:ajd
* Cas de Taxi

# Pièces Justificatives
* `assuranceChauffeur + Dates:ajd`, le 25 décembre 2025, "https://docs.google.com/document/d/1602Ett7xFepplxuCau_9aRGCRcDNwE5TLx7W3FuXyXI/edit?tab=t.0"
* `assuranceVoiture + Dates:ajd`, le 24 décembre 2025, "https://docs.google.com/document/d/1602Ett7xFepplxuCau_9aRGCRcDNwE5TLx7W3FuXyXI/edit?tab=t.0"
""";

    List<PieceJustificative> result = visitor.visit(input, PatriLangParser::piecesJustificatives);

    assertEquals(2, result.size());

    var pj1 = result.getFirst();

    assertEquals("assuranceChauffeur2025_12_24", pj1.id());
    assertEquals(LocalDate.of(2025, 12, 25), pj1.date());
    assertEquals(
        "https://docs.google.com/document/d/1602Ett7xFepplxuCau_9aRGCRcDNwE5TLx7W3FuXyXI/edit?tab=t.0",
        pj1.link());

    var pj2 = result.get(1);
    assertEquals("assuranceVoiture2025_12_24", pj2.id());
    assertEquals(LocalDate.of(2025, 12, 24), pj2.date());
    assertTrue(pj2.link().contains("https://docs.google.com/document"));
  }

  @Test
  void parse_empty_pieces_justificatives_returns_empty_list() {
    var input =
        """
        # Général
        * Spécifier Dates:ajd
        * Cas de Taxi
        """;

    List<PieceJustificative> result = visitor.visit(input, PatriLangParser::piecesJustificatives);

    assertTrue(result.isEmpty());
  }
}
