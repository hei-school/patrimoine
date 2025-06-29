package school.hei.patrimoine.patrilang.visitors.variable;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.VariableContext;

import school.hei.patrimoine.patrilang.modele.variable.Variable;

public interface VariableDeltaVisitor<T>  {
    Variable<T> apply(T baseValue, VariableContext ctx, VariableVisitor visitor);
}
