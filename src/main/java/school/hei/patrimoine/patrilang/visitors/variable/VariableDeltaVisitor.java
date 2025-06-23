package school.hei.patrimoine.patrilang.visitors.variable;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.VariableContext;

import java.util.function.BiFunction;
import school.hei.patrimoine.patrilang.modele.variable.Variable;

public interface VariableDeltaVisitor<T> extends BiFunction<T, VariableContext, Variable<T>> {}
