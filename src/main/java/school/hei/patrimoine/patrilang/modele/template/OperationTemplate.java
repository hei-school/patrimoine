package school.hei.patrimoine.patrilang.modele.template;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.OperationsContext;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import lombok.Builder;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.patrilang.modele.variable.VariableScope;
import school.hei.patrimoine.patrilang.visitors.factory.OperationVisitorFactory;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

@Builder
public record OperationTemplate(
    String name, List<OperationTemplateParam> params, List<OperationsContext> contentCtx)
    implements BiFunction<VariableScope, List<Object>, Set<Possession>> {

  @Override
  public Set<Possession> apply(VariableScope parentScope, List<Object> argValues) {
    var variableVisitor = createNewVariableVisitorWithNewScope(parentScope, argValues);
    var operationVisitor = OperationVisitorFactory.make(variableVisitor);

    return operationVisitor.apply(this.contentCtx, variableVisitor);
  }

  private VariableVisitor createNewVariableVisitorWithNewScope(
      VariableScope parentScope, List<Object> argValues) {
    if (argValues.size() > params.size()) {
      throw new IllegalArgumentException(
          "Erreur au niveau du constructeurs d'opérations name="
              + this.name
              + ". Le nombre d'arguments fournis ne peut pas dépasser celui attendu par le"
              + " template.");
    }

    var variableVisitor = new VariableVisitor(Optional.of(parentScope));
    for (int i = 0; i < argValues.size(); i++) {
      var param = this.params.get(i);
      variableVisitor.addToScope(param.name(), param.type(), argValues.get(i));
    }

    return variableVisitor;
  }
}
