package school.hei.patrimoine.patrilang.visitors;

import static java.util.Objects.isNull;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.OperationTemplateContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.OperationTemplateParamContext;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.OperationTemplateParamValueContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitText;
import static school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor.extractVariableName;
import static school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor.extractVariableType;

import java.util.List;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.patrilang.modele.template.OperationTemplate;
import school.hei.patrimoine.patrilang.modele.template.OperationTemplateParam;

@RequiredArgsConstructor
public class OperationTemplateVisitor
    implements SimpleVisitor<OperationTemplateContext, OperationTemplate> {
  @Override
  public OperationTemplate apply(OperationTemplateContext ctx) {
    var name = visitText(ctx.name);
    var args = visitParams(ctx.operationTemplateParam());
    var content = ctx.operations();

    return new OperationTemplate(name, args, content);
  }

  private static List<OperationTemplateParam> visitParams(OperationTemplateParamContext ctx) {
    if (isNull(ctx)) {
      return List.of();
    }

    return ctx.operationTemplateParamValue().stream()
        .map(OperationTemplateVisitor::visitParam)
        .toList();
  }

  private static OperationTemplateParam visitParam(OperationTemplateParamValueContext ctx) {
    var name = extractVariableName(ctx.variable().getText());
    var type = extractVariableType(ctx.variable().getText());

    return new OperationTemplateParam(name, type);
  }
}
