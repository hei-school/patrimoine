package school.hei.patrimoine.patrilang.unit.visitors.pj;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.pj.PiecesJustificative;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.modele.variable.VariableScope;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.IdVisitor;
import school.hei.patrimoine.patrilang.visitors.PatriLangPiecesJustificativeVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableDateVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableExpressionVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

public class PieceJustfificativeVisitorTest {
  private static final VariableVisitor variableVisitor = new VariableVisitor();
  private static final VariableScope variableScope = variableVisitor.getVariableScope();

  private static final AtomicReference<VariableDateVisitor> dateRef = new AtomicReference<>();
  private static final AtomicReference<VariableExpressionVisitor> exprRef = new AtomicReference<>();

  private static final VariableExpressionVisitor expressionVisitor =
      new VariableExpressionVisitor(variableScope, dateRef::get);

  private static final VariableDateVisitor dateVisitor =
      new VariableDateVisitor(variableScope, exprRef::get);

  static {
    exprRef.set(expressionVisitor);
    dateRef.set(dateVisitor);

    variableVisitor.addToScope("ajd", DATE, LocalDate.of(2025, 12, 24));
  }

  private static final IdVisitor idVisitor = new IdVisitor(variableVisitor);

  private final PatriLangPiecesJustificativeVisitor subject =
      new PatriLangPiecesJustificativeVisitor(idVisitor, dateVisitor);

  private final UnitTestVisitor visitor =
      new UnitTestVisitor() {
        @Override
        public List<PiecesJustificative> visitPiecesJustificatives(
            PatriLangParser.PiecesJustificativesContext ctx) {
          return subject.apply(ctx);
        }
      };

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

    List<PiecesJustificative> result = visitor.visit(input, PatriLangParser::piecesJustificatives);

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
}
