package school.hei.patrimoine.patrilang.unit.visitors.possession;


import org.junit.jupiter.api.Test;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.ValeurMarcheeVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ValeurMarcheeVisitorTest {
    VariableVisitor variableVisitor = new VariableVisitor();
    ValeurMarcheeVisitor subject = new ValeurMarcheeVisitor(variableVisitor);

    UnitTestVisitor visitor = new UnitTestVisitor() {
      @Override
      public Object visitAjoutValeurMarchee(PatriLangParser.AjoutValeurMarcheeContext ctx){
          return subject.apply(ctx);
      }
    };

    @Test
    void throws_exception_when_possession_type_is_not_supported() {
        var input = "*valeurMarchée, Dates:ajd, valeur marchée de 300000Ar pour Entreprise:maPossession";

        var exception = assertThrows(IllegalStateException.class, () ->
                visitor.visit(input, PatriLangParser::ajoutValeurMarchee)
        );
    }
}

