package school.hei.patrimoine.patrilang.visitors;

import static java.util.Objects.isNull;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.IdContext;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitText;

import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

@RequiredArgsConstructor
public class IdVisitor implements SimpleVisitor<IdContext, String> {
  private final VariableVisitor variableVisitor;

  @Override
  public String apply(IdContext ctx) {
    var baseId = visitText(ctx.text());

    if (isNull(ctx.variable())) {
      return baseId;
    }

    var variableValue = this.variableVisitor.apply(ctx.variable());
    return (baseId + variableValue.value()).replaceAll("-", "_");
  }
}
