package school.hei.patrimoine.patrilang.visitors;

import static java.util.Objects.isNull;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.OperationTemplateCallArgContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.OperationTemplateCallArgValueContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.OperationTemplateCallContext;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.OPERATION_TEMPLATE;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitText;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.patrilang.modele.template.OperationTemplate;
import school.hei.patrimoine.patrilang.modele.variable.Variable;

@RequiredArgsConstructor
public class OperationTemplateCallVisitor
    implements SimpleVisitor<OperationTemplateCallContext, Set<Possession>> {
  private final VariableVisitor variableVisitor;

  @Override
  public Set<Possession> apply(OperationTemplateCallContext ctx) {
    var parentScope = this.variableVisitor.getVariableScope();
    var argValues = visitArgValues(ctx.operationTemplateCallArg());
    var operationTemplateName = visitText(ctx.templateName);
    Variable<OperationTemplate> operationTemplate =
        parentScope.get(operationTemplateName, OPERATION_TEMPLATE);

    return operationTemplate.value().apply(parentScope, argValues);
  }

  private List<Object> visitArgValues(OperationTemplateCallArgContext ctx) {
    if (isNull(ctx)) {
      return List.of();
    }

    return ctx.operationTemplateCallArgValue().stream().map(this::visitArgValue).toList();
  }

  private Object visitArgValue(OperationTemplateCallArgValueContext ctx) {
    return this.variableVisitor.apply(ctx.variable()).value();
  }
}
