package school.hei.patrimoine.patrilang.visitors.variable;

import school.hei.patrimoine.patrilang.modele.variable.Variable;

import static java.lang.Double.parseDouble;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.VariableContext;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.NOMBRE;
import static school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor.R_VALUE_VARIABLE_NAME;

public class RValueNombreVisitor implements RValueVariableVisitor<Double> {
    @Override
    public Variable<Double> apply(VariableContext ctx, VariableVisitor variableVisitor) {
        return new Variable<>(R_VALUE_VARIABLE_NAME, NOMBRE, parseDouble(ctx.getText().replaceAll("_", "")));
    }
}
