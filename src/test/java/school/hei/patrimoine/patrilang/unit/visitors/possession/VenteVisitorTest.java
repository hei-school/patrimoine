package school.hei.patrimoine.patrilang.unit.visitors.possession;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.VenteVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class VenteVisitorTest {
    VariableVisitor variableVisitor = new VariableVisitor();
    VenteVisitor subject = new VenteVisitor(variableVisitor);
    
    UnitTestVisitor visitor = new UnitTestVisitor(){
        @Override
        public Object visitVentePossession(PatriLangParser.VentePossessionContext ctx){
            return subject.apply(ctx);
        }
    };

    @Test
    void throws_exception_when_possession_type_not_supported(){
        var input =
                "*ventePossession, Dates:ajd, vente de Entreprise:monEntreprise pour 300000Ar vers Trésoreries:monCompte";

        var exception = assertThrows(UnsupportedOperationException.class, () ->
                visitor.visit(input, PatriLangParser::ventePossession)
        );
    }
}
