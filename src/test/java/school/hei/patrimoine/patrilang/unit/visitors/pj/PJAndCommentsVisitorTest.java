package school.hei.patrimoine.patrilang.unit.visitors.pj;

import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.*;
import school.hei.patrimoine.patrilang.visitors.variable.VariableDateVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableExpressionVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

import java.time.LocalDate;

import static java.time.Month.DECEMBER;
import static lombok.AccessLevel.PRIVATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;

public class PJAndCommentsVisitorTest {
    private PatriLangPJAndCommentsVisitor subject;
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
        subject =
                new PatriLangPJAndCommentsVisitor(
                        new PatriLangPieceJustificativeVisitor(idVisitor, this.getVariableDateVisitor()),
                        new PatriLangCommentOperationVisitor(idVisitor, this.getVariableDateVisitor()));
        visitor =
                new UnitTestVisitor() {
                    @Override
                    public PJAndComments visitPiecesJustificatives(
                            PatriLangParser.PiecesJustificativesContext ctx) {
                        return subject.apply(ctx);
                    }
                };
    }

    @Test
    void parse_pj_and_comments_together() {
        var input =
                """
                # Général
                * Spécifier Dates:ajd
                * Cas de Taxi
                # Pièces Justificatives
                * `assuranceChauffeur + Dates:ajd`, le 25 décembre 2025, FAC102-156, "https://docs.google.com/document/d/abc"
                # Commentaires
                * `paiementChauffeur + Dates:ajd`, le 25 décembre 2025, Paiement en espèces
                """;

        PJAndComments result = visitor.visit(input, PatriLangParser::piecesJustificatives);

        // PJ
        assertEquals(1, result.piecesJustificatives().size());
        var pj = result.piecesJustificatives().getFirst();
        assertEquals("assuranceChauffeur2025_12_24", pj.id());
        assertEquals(LocalDate.of(2025, DECEMBER, 25), pj.date());

        // Comments
        assertEquals(1, result.operationComments().size());
        var comment = result.operationComments().getFirst();
        assertEquals("paiementChauffeur2025_12_24", comment.id());
        assertEquals(LocalDate.of(2025, DECEMBER, 25), comment.date());
        assertEquals("Paiement en espèces", comment.content());
    }

    @Test
    void parse_only_pj_no_comments() {
        var input =
                """
                # Général
                * Spécifier Dates:ajd
                * Cas de Taxi
                # Pièces Justificatives
                * `assuranceChauffeur + Dates:ajd`, le 25 décembre 2025, FAC102-156, "https://docs.google.com/document/d/abc"
                """;

        PJAndComments result = visitor.visit(input, PatriLangParser::piecesJustificatives);

        assertEquals(1, result.piecesJustificatives().size());
        assertTrue(result.operationComments().isEmpty());
    }

    @Test
    void parse_only_comments_no_pj() {
        var input =
                """
                # Général
                * Spécifier Dates:ajd
                * Cas de Taxi
                # Commentaires
                * `paiementChauffeur + Dates:ajd`, le 25 décembre 2025, Paiement en espèces
                """;

        PJAndComments result = visitor.visit(input, PatriLangParser::piecesJustificatives);

        assertTrue(result.piecesJustificatives().isEmpty());
        assertEquals(1, result.operationComments().size());
    }

    @Test
    void parse_empty_returns_empty() {
        var input =
                """
                # Général
                * Spécifier Dates:ajd
                * Cas de Taxi
                """;

        PJAndComments result = visitor.visit(input, PatriLangParser::piecesJustificatives);

        assertTrue(result.piecesJustificatives().isEmpty());
        assertTrue(result.operationComments().isEmpty());
    }
}
