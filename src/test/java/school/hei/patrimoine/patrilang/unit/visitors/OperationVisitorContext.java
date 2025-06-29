package school.hei.patrimoine.patrilang.unit.visitors;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.factory.OperationVisitorFactory;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.OperationVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.FEBRUARY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.SectionOperationsContext;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.DATE;

class OperationVisitorContext {
    VariableVisitor variableVisitor = new VariableVisitor();

    OperationVisitor subject = OperationVisitorFactory.make(variableVisitor);

    UnitTestVisitor visitor = new UnitTestVisitor(){
        @Override
        public Set<Possession> visitSectionOperations(SectionOperationsContext ctx) {
            return subject.apply(ctx.operations(), variableVisitor);
        }
    };

    @Test
    void add_date_declaration_in_scope(){
        var input = """
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
}
