package school.hei.patrimoine.patrilang.unit.visitors.pj;

import static java.time.Month.DECEMBER;
import static lombok.AccessLevel.PRIVATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.pj.OperationComment;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.IdVisitor;
import school.hei.patrimoine.patrilang.visitors.PatriLangCommentOperationVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableDateVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableExpressionVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

class CommentOperationVisitorTest {
  private PatriLangCommentOperationVisitor subject;
  private UnitTestVisitor visitor;

  @Getter(PRIVATE)
  private VariableDateVisitor variableDateVisitor;

  @Getter(PRIVATE)
  private VariableExpressionVisitor expressionVisitor;

  @BeforeEach
  void setup() {
    var variableVisitor = new VariableVisitor();
    var variableScope = variableVisitor.getVariableScope();
    variableVisitor.addToScope("ajd", DATE, LocalDate.of(2025, DECEMBER, 24));
    var idVisitor = new IdVisitor(variableVisitor);
    expressionVisitor = new VariableExpressionVisitor(variableScope, this::getVariableDateVisitor);
    variableDateVisitor = new VariableDateVisitor(variableScope, this::getExpressionVisitor);
    subject = new PatriLangCommentOperationVisitor(idVisitor, this.getVariableDateVisitor());
    visitor =
        new UnitTestVisitor() {
          @Override
          public List<OperationComment> visitPiecesJustificatives(
              PatriLangParser.PiecesJustificativesContext ctx) {
            return subject.apply(ctx);
          }
        };
  }

  @Test
  void parse_comments() {
    var input =
        """
        # Général
        * Spécifier Dates:ajd
        * Cas de Taxi
        # Commentaires
        * `paiementChauffeur + Dates:ajd`, le 25 décembre 2025, "Paiement effectué en espèces"
        * `paiementEssence + Dates:ajd`, le 24 décembre 2025, "Facture non disponible"
        """;

    List<OperationComment> result = visitor.visit(input, PatriLangParser::piecesJustificatives);

    assertEquals(2, result.size());

    var c1 = result.getFirst();
    assertEquals("paiementChauffeur2025_12_24", c1.id());
    assertEquals(LocalDate.of(2025, DECEMBER, 25), c1.date());
    assertEquals("Paiement effectué en espèces", c1.content());

    var c2 = result.get(1);
    assertEquals("paiementEssence2025_12_24", c2.id());
    assertEquals(LocalDate.of(2025, DECEMBER, 24), c2.date());
    assertEquals("Facture non disponible", c2.content());
  }

  @Test
  void parse_empty_comments_returns_empty_list() {
    var input =
        """
        # Général
        * Spécifier Dates:ajd
        * Cas de Taxi
        """;

    List<OperationComment> result = visitor.visit(input, PatriLangParser::piecesJustificatives);

    assertTrue(result.isEmpty());
  }
}
