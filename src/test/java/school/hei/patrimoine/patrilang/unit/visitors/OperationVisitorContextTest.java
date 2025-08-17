package school.hei.patrimoine.patrilang.unit.visitors;

import static java.time.Month.FEBRUARY;
import static java.time.Month.MARCH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;

import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.factory.OperationVisitorFactory;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.OperationVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

class OperationVisitorContextTest {
    VariableVisitor variableVisitor = new VariableVisitor();
    OperationVisitor subject = OperationVisitorFactory.make(variableVisitor);

    UnitTestVisitor visitor =
            new UnitTestVisitor() {
                @Override
                public Set<Possession> visitSectionOperations(PatriLangParser.SectionOperationsContext ctx) {
                    return subject.apply(ctx.operations(), variableVisitor);
                }
            };

    @Test
    void add_date_declaration_in_scope() {
        var input =
                """
                    # Opérations
                    * Dates:t1 : le 24 février 2025
                    * Dates:t2 : Dates:t1 + 1mois
                """;

        var expected1 = LocalDate.of(2025, FEBRUARY, 24);
        var expected2 = expected1.plusMonths(1);

        visitor.visit(input, PatriLangParser::sectionOperations);

        var actual1 = variableVisitor.getVariableScope().get("t1", DATE).value();
        var actual2 = variableVisitor.getVariableScope().get("t2", DATE).value();

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    @Test
    void add_date_with_days_should_work() {
        var input =
                """
                    # Opérations
                    * Dates:t3 : le 28 février 2025
                    * Dates:t4 : Dates:t3 + 3jours
                """;

        var expected3 = LocalDate.of(2025, FEBRUARY, 28);
        var expected4 = expected3.plusDays(3);

        visitor.visit(input, PatriLangParser::sectionOperations);

        var actual3 = variableVisitor.getVariableScope().get("t3", DATE).value();
        var actual4 = variableVisitor.getVariableScope().get("t4", DATE).value();

        assertEquals(expected3, actual3);
        assertEquals(expected4, actual4);
    }

    @Test
    void add_date_across_month_should_handle_correctly() {
        var input =
                """
                    # Opérations
                    * Dates:t5 : le 28 février 2025
                    * Dates:t6 : Dates:t5 + 3jours
                """;

        var expected5 = LocalDate.of(2025, FEBRUARY, 28);
        var expected6 = LocalDate.of(2025, MARCH, 3);

        visitor.visit(input, PatriLangParser::sectionOperations);

        var actual5 = variableVisitor.getVariableScope().get("t5", DATE).value();
        var actual6 = variableVisitor.getVariableScope().get("t6", DATE).value();

        assertEquals(expected5, actual5);
        assertEquals(expected6, actual6);
    }

    @Test
    void redeclare_date_variable_should_throw() {
        var input =
                """
                    # Opérations
                    * Dates:t1 : le 1 février 2025
                    * Dates:t1 : le 2 février 2025
                """;

        assertThrows(
                IllegalArgumentException.class,
                () -> visitor.visit(input, PatriLangParser::sectionOperations));
    }
}
